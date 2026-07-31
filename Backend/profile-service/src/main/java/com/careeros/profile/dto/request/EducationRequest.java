package com.careeros.profile.dto.request;

import com.careeros.profile.entity.enums.DegreeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class EducationRequest {

    @NotBlank(message = "Institution name is required")
    @Size(max = 150, message = "Institution name must not exceed 150 characters")
    private String institution;

    @NotNull(message = "Degree type is required")
    private DegreeType degreeType;

    @NotBlank(message = "Field of study is required")
    @Size(max = 150, message = "Field of study must not exceed 150 characters")
    private String fieldOfStudy;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    @Size(max = 20, message = "Grade must not exceed 20 characters")
    private String grade;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    public EducationRequest() {}

    public EducationRequest(String institution, DegreeType degreeType, String fieldOfStudy, LocalDate startDate, LocalDate endDate, String grade, String description) {
        this.institution = institution;
        this.degreeType = degreeType;
        this.fieldOfStudy = fieldOfStudy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.grade = grade;
        this.description = description;
    }

    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }

    public DegreeType getDegreeType() { return degreeType; }
    public void setDegreeType(DegreeType degreeType) { this.degreeType = degreeType; }

    public String getFieldOfStudy() { return fieldOfStudy; }
    public void setFieldOfStudy(String fieldOfStudy) { this.fieldOfStudy = fieldOfStudy; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public static EducationRequestBuilder builder() { return new EducationRequestBuilder(); }

    public static class EducationRequestBuilder {
        private String institution;
        private DegreeType degreeType;
        private String fieldOfStudy;
        private LocalDate startDate;
        private LocalDate endDate;
        private String grade;
        private String description;

        public EducationRequestBuilder institution(String institution) { this.institution = institution; return this; }
        public EducationRequestBuilder degreeType(DegreeType degreeType) { this.degreeType = degreeType; return this; }
        public EducationRequestBuilder fieldOfStudy(String fieldOfStudy) { this.fieldOfStudy = fieldOfStudy; return this; }
        public EducationRequestBuilder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public EducationRequestBuilder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public EducationRequestBuilder grade(String grade) { this.grade = grade; return this; }
        public EducationRequestBuilder description(String description) { this.description = description; return this; }

        public EducationRequest build() {
            return new EducationRequest(institution, degreeType, fieldOfStudy, startDate, endDate, grade, description);
        }
    }
}
