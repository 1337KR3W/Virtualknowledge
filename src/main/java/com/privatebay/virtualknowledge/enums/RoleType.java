package com.privatebay.virtualknowledge.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum RoleType {
	ROLE_USER, ROLE_ADMIN, ROLE_MANAGER
}
