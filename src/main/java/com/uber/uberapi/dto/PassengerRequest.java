package com.uber.uberapi.dto;

import com.uber.uberapi.models.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public record PassengerRequest(@NotBlank String username, @NotBlank String password, @NotBlank String name,@NotNull Gender gender,
                               @NotNull LocalDate dob,
                               @NotBlank String phoneNumber) {

}
