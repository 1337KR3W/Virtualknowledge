package com.privatebay.virtualknowledge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;
import java.util.List;
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

}
