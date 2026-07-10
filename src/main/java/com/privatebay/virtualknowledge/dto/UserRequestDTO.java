package com.privatebay.virtualknowledge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.privatebay.virtualknowledge.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Necessary data in order to create or update a user")
public class UserRequestDTO {

	@Schema(description = "User first name", example = "Víctor Hugo", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank
	@Size(min = 3, max = 64, message = "First name must be between 3 and 64 characters")
	private String firstName;

	@Schema(description = "User last name", example = "López Garrido", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank
	@Size(min = 3, max = 64, message = "Last name must be between 3 and 64 characters")
	private String lastName;

	@Schema(description = "User email", example = "user324@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
	@Email(message = "Email should be valid")
	@NotBlank(message = "First name is mandatory")
	@Size(max = 150)
	private String email;

	@Schema(description = "User password", example = "securePass123", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank
	@Size(min = 3, max = 16)
	private String password;

	@Schema(description = "User role ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull
	private Long roleId;

	@Schema(description = "User department ID", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long departmentId;

	@Schema(description = "User status", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull
	private UserStatus status;

	public UserRequestDTO() {
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}
}