package com.privatebay.virtualknowledge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.privatebay.virtualknowledge.dto.DepartmentRequestDTO;
import com.privatebay.virtualknowledge.dto.DepartmentResponseDTO;
import com.privatebay.virtualknowledge.entity.Department;
import com.privatebay.virtualknowledge.mapper.DepartmentMapper;
import com.privatebay.virtualknowledge.repository.DepartmentRepository;
import com.privatebay.virtualknowledge.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

	@Mock
	private DepartmentRepository departmentRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private DepartmentMapper departmentMapper;

	@InjectMocks
	private DepartmentService departmentService;

	@Test
	void createDepartment_shouldCreateDepartment_WhenNameIsUnique() {
		DepartmentRequestDTO dto = new DepartmentRequestDTO("Successfull Department");

		when(departmentRepository.findByName(anyString())).thenReturn(List.of());

		when(departmentRepository.save(any(Department.class))).thenAnswer(i -> i.getArgument(0));

		DepartmentResponseDTO expectedDto = new DepartmentResponseDTO();
		expectedDto.setName("Successfull Department");
		when(departmentMapper.toResponseDTO(any(Department.class))).thenReturn(expectedDto);

		DepartmentResponseDTO result = departmentService.createDepartment(dto);

		assertNotNull(result);
		assertEquals("Successfull Department", result.getName());
	}

	@Test
	void createDepartment_ShouldThrowException_WhenRequiredNameFieldIsMissing() {

		DepartmentRequestDTO dto = new DepartmentRequestDTO();

		assertThrows(IllegalArgumentException.class, () -> {
			departmentService.createDepartment(dto);
		});

		verifyNoInteractions(userRepository);
	}

	@Test
	void getAllDepartments_ShouldReturnList_WhenDepartmentsExist() {
		Department dept = new Department();
		when(departmentRepository.findAll()).thenReturn(List.of(dept));
		when(departmentMapper.toResponseDTO(any())).thenReturn(new DepartmentResponseDTO());

		List<DepartmentResponseDTO> result = departmentService.getAllDepartments();

		assertEquals(1, result.size());
		verify(departmentRepository).findAll();
	}

	@Test
	void getDepartment_ShouldThrowException_WhenIdNotFound() {
		when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> departmentService.getDepartment(1L));
	}

	@Test
	void updateDepartment_ShouldUpdate_WhenExists() {
		Department existingDept = new Department();
		when(departmentRepository.findById(1L)).thenReturn(Optional.of(existingDept));
		when(departmentRepository.save(any(Department.class))).thenAnswer(i -> i.getArgument(0));
		when(departmentMapper.toResponseDTO(any())).thenReturn(new DepartmentResponseDTO());

		DepartmentRequestDTO dto = new DepartmentRequestDTO("New Name");
		DepartmentResponseDTO result = departmentService.updateDepartment(1L, dto);

		assertNotNull(result);
		assertEquals("New Name", existingDept.getName());
		verify(departmentRepository).save(existingDept);
	}

	@Test
	void deleteDepartment_ShouldDelete_WhenExists() {

		Long dptoId = 1L;
		Department dpto = new Department("DEPARTMENT FOR TESTING");
		when(departmentRepository.findById(dptoId)).thenReturn(Optional.of(dpto));
		departmentService.deleteDepartment(dptoId);
		verify(departmentRepository).delete(dpto);
		verify(departmentRepository, never()).deleteById(anyLong());
	}

    @Test
    void deleteDepartment_ShouldThrowException_WhenNotExists() {
    	
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> departmentService.deleteDepartment(1L));
        verify(departmentRepository, never()).delete(any(Department.class));
    }

}
