package com.privatebay.virtualknowledge.service;

import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.privatebay.virtualknowledge.dto.ProjectTimeRowDTO;
import com.privatebay.virtualknowledge.dto.TimeEntryDTO;
import com.privatebay.virtualknowledge.dto.TimeSheetRequestDTO;
import com.privatebay.virtualknowledge.entity.Project;
import com.privatebay.virtualknowledge.entity.TimeSheet;
import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.mapper.TimeSheetMapper;
import com.privatebay.virtualknowledge.repository.ProjectRepository;
import com.privatebay.virtualknowledge.repository.TimeSheetRepository;
import com.privatebay.virtualknowledge.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class TimeSheetServiceTest {

	@Mock
	private TimeSheetRepository timeSheetRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private ProjectRepository projectRepository;
	
	@Mock
	private TimeSheetMapper timeSheetMapper;

	@InjectMocks
	private TimeSheetService timeSheetService;

	@Test
	void saveWeek_ShouldDeleteAndSaveNewEntries_WhenRequestIsValid() {

		TimeSheetRequestDTO request = new TimeSheetRequestDTO();
		request.setUserId(1L);
		request.setWeekId("2026-W27");

		Project project = new Project();
		project.setId(10L);

		ProjectTimeRowDTO row = new ProjectTimeRowDTO(10L, "Proyecto", "IT");
		row.addEntry("mon", new TimeEntryDTO(new BigDecimal("8.0"), "Work"));
		request.setRows(List.of(row));

		when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
		when(projectRepository.findById(10L)).thenReturn(Optional.of(project));

		timeSheetService.saveWeek(request);

		verify(timeSheetRepository).deleteByUserIdAndWorkDateBetween(eq(1L), any(LocalDate.class),
				any(LocalDate.class));

		verify(timeSheetRepository, times(1)).save(any(TimeSheet.class));
	}

	@Test
	void getTimeSheetByWeek_ShouldReturnDTO_WhenEntriesExist() {

		Long userId = 1L;
		String weekId = "2026-W27";
		when(timeSheetRepository.findByUserIdAndWorkDateBetween(eq(userId), any(LocalDate.class), any(LocalDate.class)))
				.thenReturn(List.of(new TimeSheet()));

		timeSheetService.getTimeSheetByWeek(userId, weekId);

		verify(timeSheetMapper).toDTO(eq(userId), eq(weekId), anyString(), anyList());
	}
}