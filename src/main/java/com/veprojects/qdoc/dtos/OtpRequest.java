package com.veprojects.qdoc.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record OtpRequest(


        @NotBlank(message = "Phone number cannot be blank")
        @Pattern(
             regexp = "\\d{10}",
             message = "Phone number must be exactly 10 digits"
         )
         @NotNull
         String phone,

        @NotBlank(message = "Phone number cannot be blank")
        @Pattern(
                regexp = "\\d{6}",
                message = "Phone number must be exactly 10 digits"
        )
        @NotNull
        String otp


){}


