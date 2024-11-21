package com.uber.uberapi.dto;

import com.uber.uberapi.models.DriverApprovalStatus;
import com.uber.uberapi.models.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record DriverRequest(@NotBlank String username, @NotBlank String password, @NotBlank String name,
                            @NotNull Gender gender,
                            @NotBlank @Size(min = 10, max = 10) String phoneNumber,
                            @NotNull LocalDate dob, @NotBlank String licenseDetails,
                            @NotNull  DriverApprovalStatus status, @NotNull boolean isAvailable,
                            @NotBlank String activeCity) {
}
