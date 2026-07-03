package com.privatebay.virtualknowledge.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.List;
import org.springframework.stereotype.Service;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.privatebay.virtualknowledge.dto.ProjectTimeRowDTO;
import com.privatebay.virtualknowledge.dto.TimeEntryDTO;
import com.privatebay.virtualknowledge.dto.TimeSheetRequestDTO;

@Service
public class PdfGeneratorService {

	private static final String[] DAYS = { "sun", "mon", "tue", "wed", "thu", "fri", "sat" };
	private static final String[] HEADERS = { "Project", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Total" };
	
	private static final Map<String, String> DAY_LABELS = Map.of("sun", "Sunday", "mon", "Monday", "tue", "Tuesday",
			"wed", "Wednesday", "thu", "Thursday", "fri", "Friday", "sat", "Saturday");

	public byte[] generateWeeklyReport(TimeSheetRequestDTO data, String username) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4, 36, 36, 54, 36);
		PdfWriter.getInstance(document, out);

		document.open();

		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Color.DARK_GRAY);
		Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
		Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.DARK_GRAY);
		Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);
		Font boldBodyFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.BLACK);
		Font deptFont = FontFactory.getFont(FontFactory.HELVETICA, 7, Color.GRAY);
		Font subHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, Color.WHITE);

		Paragraph title = new Paragraph("VIRTUAL KNOWLEDGE", titleFont);
		title.setSpacingAfter(4);
		document.add(title);

		Paragraph sub = new Paragraph(
				"Weekly Timesheet Report | Week: " + data.getWeekId() + " | Professional: " + username, subTitleFont);
		sub.setSpacingAfter(24);
		document.add(sub);

		String[] calculatedDates = calculateWeekDates(data.getWeekId());

		double totalHours = 0;
		String topProject = "N/A";
		double maxHours = -1;

		for (ProjectTimeRowDTO row : data.getRows()) {
			double rowTotal = 0;
			for (String day : DAYS) {
				TimeEntryDTO entry = row.getDays().get(day);
				if (entry != null && entry.getHours() != null) {
					rowTotal += entry.getHours().doubleValue();
				}
			}
			totalHours += rowTotal;

			if (rowTotal > maxHours) {
				maxHours = rowTotal;
				topProject = row.getProjectName();
			}
		}

		double dailyAverage = (totalHours > 0) ? (totalHours / 7.0) : 0;

		PdfPTable kpiTable = new PdfPTable(3);
		kpiTable.setWidthPercentage(100);
		kpiTable.setSpacingAfter(20);

		Color cardBg = new Color(248, 249, 250);
		Color cardBorder = new Color(220, 224, 230);

		addKpiCell(kpiTable, "TOTAL HOURS", String.format("%.2f hrs", totalHours), cardBg, cardBorder);
		addKpiCell(kpiTable, "TOP PROJECT", topProject, cardBg, cardBorder);
		addKpiCell(kpiTable, "DAILY AVERAGE", String.format("%.2f hrs/day", dailyAverage), cardBg, cardBorder);

		document.add(kpiTable);

		document.add(new Paragraph("Time Breakdown", sectionFont));
		Paragraph space = new Paragraph("");
		space.setSpacingAfter(8);
		document.add(space);

		PdfPTable mainTable = new PdfPTable(9);
		mainTable.setWidthPercentage(100);
		mainTable.setWidths(new float[] { 2.5f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1.2f });
		mainTable.setSplitRows(true);

		Color corporateBlue = new Color(41, 128, 185);
		Color borderColor = new Color(180, 180, 180);

		for (String h : HEADERS) {
			PdfPCell cell = new PdfPCell(
					new Phrase(h, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE)));
			cell.setBackgroundColor(corporateBlue);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(4);
			cell.setBorderColor(borderColor);

			if (h.equals("Total")) {
				cell.setRowspan(2);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setPadding(6);
			} else {
				cell.setBorderWidthBottom(0);
			}
			mainTable.addCell(cell);
		}

		PdfPCell deptHeaderCell = new PdfPCell(
				new Phrase("Departments", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, Color.WHITE)));
		deptHeaderCell.setBackgroundColor(corporateBlue);
		deptHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		deptHeaderCell.setPaddingLeft(6);
		deptHeaderCell.setPaddingBottom(4);
		deptHeaderCell.setBorderColor(borderColor);
		deptHeaderCell.setBorderWidthTop(0);
		mainTable.addCell(deptHeaderCell);

		for (String dateStr : calculatedDates) {
			PdfPCell dateCell = new PdfPCell(new Phrase(dateStr, subHeaderFont));
			dateCell.setBackgroundColor(corporateBlue);
			dateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dateCell.setPaddingBottom(4);
			dateCell.setBorderColor(borderColor);
			dateCell.setBorderWidthTop(0);
			mainTable.addCell(dateCell);
		}

		for (ProjectTimeRowDTO row : data.getRows()) {
			Phrase projectPhrase = new Phrase();
			projectPhrase.add(new Chunk(row.getProjectName(), bodyFont));

			String department = (row.getDepartmentName() != null && !row.getDepartmentName().isEmpty())
					? row.getDepartmentName()
					: "General";

			projectPhrase.add(new Chunk("\n" + department, deptFont));

			PdfPCell pCell = new PdfPCell(projectPhrase);
			pCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pCell.setPadding(6);
			pCell.setBorderColor(borderColor);
			mainTable.addCell(pCell);

			BigDecimal rowTotal = BigDecimal.ZERO;

			for (String day : DAYS) {
				TimeEntryDTO entry = row.getDays().get(day);
				BigDecimal hr = (entry != null && entry.getHours() != null) ? entry.getHours() : BigDecimal.ZERO;
				rowTotal = rowTotal.add(hr);

				PdfPCell cell = new PdfPCell(
						new Phrase(hr.compareTo(BigDecimal.ZERO) == 0 ? "-" : hr.toString(), bodyFont));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBorderColor(borderColor);
				mainTable.addCell(cell);
			}

			PdfPCell totalCell = new PdfPCell(new Phrase(rowTotal.toString() + "h", boldBodyFont));
			totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			totalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			totalCell.setBackgroundColor(new Color(245, 247, 250));
			totalCell.setBorderColor(borderColor);
			mainTable.addCell(totalCell);
		}
		document.add(mainTable);

		boolean hasDailyComments = false;
		List<Paragraph> dailyCommentsList = new ArrayList<>();

		for (ProjectTimeRowDTO row : data.getRows()) {
			if (row.getDays() == null)
				continue;

			for (String day : DAYS) {
				TimeEntryDTO entry = row.getDays().get(day);
				if (entry != null && entry.getComment() != null && !entry.getComment().trim().isEmpty()) {
					hasDailyComments = true;
					String dateForDay = calculatedDates[Arrays.asList(DAYS).indexOf(day)];

					String textFormat = String.format("•  %s - %s (%s): %s", row.getProjectName(),
							DAY_LABELS.getOrDefault(day, day).substring(0, 3), dateForDay, entry.getComment().trim());

					Paragraph pComment = new Paragraph(textFormat, bodyFont);
					pComment.setIndentationLeft(12);
					pComment.setSpacingAfter(3);
					dailyCommentsList.add(pComment);
				}
			}
		}

		if (data.getGlobalComment() != null && !data.getGlobalComment().trim().isEmpty()) {
			Paragraph commTitle = new Paragraph("Weekly Global Comment", sectionFont);
			commTitle.setSpacingBefore(20);
			commTitle.setSpacingAfter(6);
			document.add(commTitle);

			PdfPTable commTable = new PdfPTable(1);
			commTable.setWidthPercentage(100);
			PdfPCell commCell = new PdfPCell(new Paragraph(data.getGlobalComment(), bodyFont));
			commCell.setPadding(10);
			commCell.setBackgroundColor(new Color(250, 250, 250));
			commCell.setBorderColor(new Color(220, 224, 230));
			commTable.addCell(commCell);
			document.add(commTable);
		}

		if (hasDailyComments) {
			Paragraph dailyCommTitle = new Paragraph("Daily Project Comments", sectionFont);
			dailyCommTitle.setSpacingBefore(20);
			dailyCommTitle.setSpacingAfter(8);
			document.add(dailyCommTitle);

			PdfPTable commentsTable = new PdfPTable(1);
			commentsTable.setWidthPercentage(95);

			for (Paragraph p : dailyCommentsList) {
				PdfPCell cell = new PdfPCell(p);
				cell.setBorder(PdfPCell.NO_BORDER);
				cell.setPaddingBottom(5);
				commentsTable.addCell(cell);
			}
			document.add(commentsTable);
		}

		document.close();
		return out.toByteArray();
	}

	private String[] calculateWeekDates(String weekId) {
		String[] dates = new String[7];
		try {
			String[] parts = weekId.split("-W");
			int year = Integer.parseInt(parts[0]);
			int week = Integer.parseInt(parts[1]);

			// ISO-8601
			WeekFields weekFields = WeekFields.of(Locale.getDefault());
			LocalDate date = LocalDate.of(year, 2, 1).with(weekFields.weekOfYear(), week).with(weekFields.dayOfWeek(),
					1);

			LocalDate current = date.minusDays(1);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

			for (int i = 0; i < 7; i++) {
				dates[i] = current.format(formatter);
				current = current.plusDays(1);
			}
		} catch (Exception e) {
			Arrays.fill(dates, "----/--/--");
		}
		return dates;
	}

	private void addKpiCell(PdfPTable table, String label, String value, Color bg, Color border) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(bg);
		cell.setBorderColor(border);
		cell.setPadding(10);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);

		Paragraph pLabel = new Paragraph(label, FontFactory.getFont(FontFactory.HELVETICA, 8, Color.GRAY));
		pLabel.setAlignment(Element.ALIGN_CENTER);

		Paragraph pValue = new Paragraph(value, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.DARK_GRAY));
		pValue.setAlignment(Element.ALIGN_CENTER);
		pValue.setSpacingBefore(5);

		cell.addElement(pLabel);
		cell.addElement(pValue);
		table.addCell(cell);
	}
}