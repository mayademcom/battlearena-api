package com.mayadem.battlearena.api.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequestDto(
    @NotBlank(message = "Display name cannot be empty.")
    @Size(min = 3, max = 50, message = "Display name must be between 3 and 50 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9_\\- ]+$", message = "Display name can only contain letters, numbers, spaces, underscores, and hyphens.")
    String displayName
) {}
