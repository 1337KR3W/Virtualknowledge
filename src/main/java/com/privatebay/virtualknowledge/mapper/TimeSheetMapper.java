package com.privatebay.virtualknowledge.mapper;

import com.privatebay.virtualknowledge.dto.*;
import com.privatebay.virtualknowledge.entity.TimeSheet;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class TimeSheetMapper {

	public TimeSheetRequestDTO toDTO(Long userId, String weekId, String globalComment, List<TimeSheet> entries) {
		Map<Long, ProjectTimeRowDTO> rowsMap = new HashMap<>();

		for (TimeSheet ts : entries) {
			Long pid = ts.getProject().getId();
			rowsMap.putIfAbsent(pid,
					new ProjectTimeRowDTO(pid, ts.getProject().getName(), ts.getProject().getDepartment().getName()));

			TimeEntryDTO entry = new TimeEntryDTO(ts.getHours(), ts.getComment());
			String dayKey = ts.getWorkDate().getDayOfWeek().name().substring(0, 3);
			rowsMap.get(pid).addEntry(dayKey, entry);
		}

		TimeSheetRequestDTO dto = new TimeSheetRequestDTO();
		dto.setWeekId(weekId);
		dto.setUserId(userId);
		dto.setGlobalComment(globalComment);
		dto.setRows(new ArrayList<>(rowsMap.values()));
		return dto;
	}
}