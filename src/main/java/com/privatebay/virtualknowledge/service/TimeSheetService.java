package com.privatebay.virtualknowledge.service;

import com.privatebay.virtualknowledge.dto.*;
import com.privatebay.virtualknowledge.entity.*;
import com.privatebay.virtualknowledge.mapper.TimeSheetMapper;
import com.privatebay.virtualknowledge.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TimeSheetService {

	private final TimeSheetRepository timeSheetRepository;
	private final UserRepository userRepository;
	private final ProjectRepository projectRepository;
	private final TimeSheetMapper timeSheetMapper;

	public TimeSheetService(TimeSheetRepository timeSheetRepository, UserRepository userRepository,
			ProjectRepository projectRepository, TimeSheetMapper timeSheetMapper) {
		this.timeSheetRepository = timeSheetRepository;
		this.userRepository = userRepository;
		this.projectRepository = projectRepository;
		this.timeSheetMapper = timeSheetMapper;
	}

	@Transactional(readOnly = true)
	public TimeSheetRequestDTO getTimeSheetByWeek(Long userId, String weekId) {
		LocalDate monday = LocalDate.parse(weekId + "-1", DateTimeFormatter.ISO_WEEK_DATE);
		LocalDate sunday = monday.plusDays(6);

		List<TimeSheet> entries = timeSheetRepository.findByUserIdAndWorkDateBetween(userId, monday, sunday);

		String globalComment = entries.stream().map(TimeSheet::getGlobalComment).filter(c -> c != null && !c.isBlank())
				.findFirst().orElse("");

		return timeSheetMapper.toDTO(userId, weekId, globalComment, entries);
	}

	@Transactional
	public void saveWeek(TimeSheetRequestDTO request) {
		LocalDate monday = LocalDate.parse(request.getWeekId() + "-1", DateTimeFormatter.ISO_WEEK_DATE);
		LocalDate sunday = monday.plusDays(6);

		timeSheetRepository.deleteByUserIdAndWorkDateBetween(request.getUserId(), monday, sunday);

		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new RuntimeException("User not found"));

		for (ProjectTimeRowDTO row : request.getRows()) {
			Project project = projectRepository.findById(row.getPid())
					.orElseThrow(() -> new RuntimeException("Project not found"));

			row.getDays().forEach((dayKey, entry) -> {
				if (isValidEntry(entry)) {
					TimeSheet ts = new TimeSheet();
					ts.setUser(user);
					ts.setProject(project);
					ts.setHours(entry.getHours());
					ts.setComment(entry.getComment());
					ts.setWorkDate(calculateDate(monday, dayKey));
					ts.setGlobalComment(request.getGlobalComment());
					ts.setWeekId(request.getWeekId());
					timeSheetRepository.save(ts);
				}
			});
		}
	}

	private boolean isValidEntry(TimeEntryDTO entry) {
		return entry != null && entry.getHours() != null && entry.getHours().compareTo(BigDecimal.ZERO) >= 0;
	}

	private LocalDate calculateDate(LocalDate monday, String dayKey) {
		return switch (dayKey.toLowerCase()) {
		case "mon" -> monday;
		case "tue" -> monday.plusDays(1);
		case "wed" -> monday.plusDays(2);
		case "thu" -> monday.plusDays(3);
		case "fri" -> monday.plusDays(4);
		case "sat" -> monday.plusDays(5);
		case "sun" -> monday.plusDays(6);
		default -> monday;
		};
	}
}