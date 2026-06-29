package com.privatebay.virtualknowledge.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.privatebay.virtualknowledge.dto.ProjectTimeRowDTO;
import com.privatebay.virtualknowledge.dto.TimeSheetRequestDTO;

@Service
public class PdfGeneratorService {

    private static final String[] DAYS = {"mon", "tue", "wed", "thu", "fri", "sat", "sun"};
    private static final String[] HEADERS = {"Project", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Total"};

    public byte[] generateWeeklyReport(TimeSheetRequestDTO data, String username) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
        PdfWriter.getInstance(document, out);

        document.open();

        // --- Fuentes ---
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Color.DARK_GRAY);
        Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.DARK_GRAY);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);
        Font boldBodyFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.BLACK);

        // --- Cabecera ---
        Paragraph title = new Paragraph("VIRTUAL KNOWLEDGE", titleFont);
        title.setSpacingAfter(4);
        document.add(title);

        Paragraph sub = new Paragraph("Weekly Timesheet Report | Week: " + data.getWeekId() + " | Professional: " + username, subTitleFont);
        sub.setSpacingAfter(24);
        document.add(sub);

        // --- Cálculo de Métricas ---
        BigDecimal totalHours = BigDecimal.ZERO;
        Map<String, BigDecimal> projectTotals = new HashMap<>();
        int activeDaysCount = 0;
        BigDecimal[] dailyTotals = new BigDecimal[7];
        for (int i = 0; i < 7; i++) dailyTotals[i] = BigDecimal.ZERO;

        for (ProjectTimeRowDTO row : data.getRows()) {
            BigDecimal rowTotal = BigDecimal.ZERO;
            for (int i = 0; i < 7; i++) {
                var entry = row.getDays().get(DAYS[i]);
                if (entry != null && entry.getHours() != null) {
                    BigDecimal hr = entry.getHours();
                    rowTotal = rowTotal.add(hr);
                    dailyTotals[i] = dailyTotals[i].add(hr);
                }
            }
            totalHours = totalHours.add(rowTotal);
            projectTotals.put(row.getProjectName(), rowTotal);
        }

        for (BigDecimal dayTotal : dailyTotals) {
            if (dayTotal.compareTo(BigDecimal.ZERO) > 0) activeDaysCount++;
        }

        String topProject = projectTotals.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        BigDecimal avgHours = activeDaysCount > 0 
                ? totalHours.divide(BigDecimal.valueOf(activeDaysCount), 2, RoundingMode.HALF_UP) 
                : BigDecimal.ZERO;

        // --- Bloque Visual de Métricas (KPIs) ---
        PdfPTable kpiTable = new PdfPTable(3);
        kpiTable.setWidthPercentage(100);
        kpiTable.setSpacingAfter(20);

        kpiTable.addCell(createKpiCell("TOTAL HOURS", totalHours.toString() + " hrs", boldBodyFont, bodyFont));
        kpiTable.addCell(createKpiCell("TOP PROJECT", topProject, boldBodyFont, bodyFont));
        kpiTable.addCell(createKpiCell("DAILY AVERAGE", avgHours.toString() + " hrs/day", boldBodyFont, bodyFont));
        document.add(kpiTable);

        // --- Tabla de Tiempos Principal ---
        document.add(new Paragraph("Time Breakdown", sectionFont));
        Paragraph space = new Paragraph(""); space.setSpacingAfter(8); document.add(space);

        PdfPTable mainTable = new PdfPTable(9);
        mainTable.setWidthPercentage(100);
        mainTable.setWidths(new float[]{2.5f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1.2f});

        // Estilar Cabecera de la Tabla
        for (String h : HEADERS) {
            PdfPCell cell = new PdfPCell(new Phrase(h, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE)));
            cell.setBackgroundColor(new Color(41, 128, 185)); // Azul corporativo
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(6);
            mainTable.addCell(cell);
        }

        // Renderizar Filas de Datos
        for (ProjectTimeRowDTO row : data.getRows()) {
            mainTable.addCell(new PdfPCell(new Phrase(row.getProjectName(), bodyFont)));
            BigDecimal rowTotal = BigDecimal.ZERO;

            for (String day : DAYS) {
                var entry = row.getDays().get(day);
                BigDecimal hr = (entry != null && entry.getHours() != null) ? entry.getHours() : BigDecimal.ZERO;
                rowTotal = rowTotal.add(hr);
                
                PdfPCell cell = new PdfPCell(new Phrase(hr.compareTo(BigDecimal.ZERO) == 0 ? "-" : hr.toString(), bodyFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                mainTable.addCell(cell);
            }

            PdfPCell totalCell = new PdfPCell(new Phrase(rowTotal.toString(), boldBodyFont));
            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalCell.setBackgroundColor(new Color(245, 247, 250));
            mainTable.addCell(totalCell);
        }
        document.add(mainTable);

        // --- Comentario Global (Pie de página / Cierre) ---
        if (data.getGlobalComment() != null && !data.getGlobalComment().trim().isEmpty()) {
            Paragraph commTitle = new Paragraph("Weekly Global Comments", sectionFont);
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

        document.close();
        return out.toByteArray();
    }

    private PdfPCell createKpiCell(String title, String value, Font titleFont, Font valueFont) {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(10);
        cell.setBackgroundColor(new Color(248, 249, 250));
        cell.setBorderColor(new Color(233, 236, 239));
        
        Paragraph p1 = new Paragraph(title, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, Color.GRAY));
        p1.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p1);
        
        Paragraph p2 = new Paragraph(value, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.DARK_GRAY));
        p2.setAlignment(Element.ALIGN_CENTER);
        p2.setSpacingBefore(4);
        cell.addElement(p2);
        
        return cell;
    }
}