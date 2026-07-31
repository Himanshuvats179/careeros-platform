package com.careeros.profile.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @Size(max = 120)
    private String headline;

    @Size(max = 2000)
    private String bio;

    @Pattern(
            regexp = "^[0-9+\\-() ]{8,20}$",
            message = "Invalid phone number"
    )
    private String phone;

    @Size(max = 120)
    private String location;

    @Size(max = 255)
    private String website;

    @Size(max = 255)
    private String linkedinUrl;

    @Size(max = 255)
    private String githubUrl;

    @Min(0)
    @Max(60)
    private Integer experienceYears;
}