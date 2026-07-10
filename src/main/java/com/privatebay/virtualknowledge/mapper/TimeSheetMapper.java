package com.privatebay.virtualknowledge.mapper;

import com.privatebay.virtualknowledge.dto.*;
import com.privatebay.virtualknowledge.entity.TimeSheet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mapper(componentModel = "spring")
public interface TimeSheetMapper {

	@Mapping(target = "rows", expression = "java(mapEntriesToRows(entries))")
	@Mapping(target = "weekDates", expression = "java(calculateWeekDates(weekId))")
	TimeSheetRequestDTO toDTO(Long userId, String weekId, String globalComment, List<TimeSheet> entries);

	default List<LocalDate> calculateWeekDates(String weekId) {
		if (weekId == null)
			return new ArrayList<>();

		LocalDate monday = LocalDate.parse(weekId + "-1", DateTimeFormatter.ISO_WEEK_DATE);

		return IntStream.range(0, 7).mapToObj(monday::plusDays).collect(Collectors.toList());
	}

	default List<ProjectTimeRowDTO> mapEntriesToRows(List<TimeSheet> entries) {
		if (entries == null)
			return new ArrayList<>();

		Map<Long, ProjectTimeRowDTO> rowsMap = new HashMap<>();

		for (TimeSheet ts : entries) {
			Long pid = ts.getProject().getId();

			rowsMap.putIfAbsent(pid,
					new ProjectTimeRowDTO(pid, ts.getProject().getName(), ts.getProject().getDepartment().getName()));

			TimeEntryDTO entry = new TimeEntryDTO(ts.getHours(), ts.getComment());
			String dayKey = ts.getWorkDate().getDayOfWeek().name().substring(0, 3);
			rowsMap.get(pid).addEntry(dayKey, entry);
		}

		return new ArrayList<>(rowsMap.values());
	}
}