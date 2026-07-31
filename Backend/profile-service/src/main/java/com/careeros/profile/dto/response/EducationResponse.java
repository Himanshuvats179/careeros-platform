package com.careeros.profile.dto.response;

import com.careeros.profile.entity.enums.DegreeType;

import java.time.LocalDate;
import java.util.UUID;

public class EducationResponse {
    private UUID id;
    private String institution;
    private DegreeType degreeType;
    private String fieldOfStudy;
    private LocalDate startDate;
    private LocalDate endDate;
    private String grade;
    private String description;

    public EducationResponse() {}

    public EducationResponse(UUID id, String institution, DegreeType degreeType, String fieldOfStudy, LocalDate startDate, LocalDate endDate, String grade, String description) {
        this.id = id;
        this.institution = institution;
        this.degreeType = degreeType;
        this.fieldOfStudy = fieldOfStudy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.grade = grade;
        this.description = description;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

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

    public static EducationResponseBuilder builder() { return new EducationResponseBuilder(); }

    public static class EducationResponseBuilder {
        private UUID id;
        private String institution;
        private DegreeType degreeType;
        private String fieldOfStudy;
        private LocalDate startDate;
        private LocalDate endDate;
        private String grade;
        private String description;

        public EducationResponseBuilder id(UUID id) { this.id = id; return this; }
        public EducationResponseBuilder institution(String institution) { this.institution = institution; return this; }
        public EducationResponseBuilder degreeType(DegreeType degreeType) { this.degreeType = degreeType; return this; }
        public EducationResponseBuilder fieldOfStudy(String fieldOfStudy) { this.fieldOfStudy = fieldOfStudy; return this; }
        public EducationResponseBuilder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public EducationResponseBuilder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public EducationResponseBuilder grade(String grade) { this.grade = grade; return this; }
        public EducationResponseBuilder description(String description) { this.description = description; return this; }

        public EducationResponse build() {
            return new EducationResponse(id, institution, degreeType, fieldOfStudy, startDate, endDate, grade, description);
        }
    }
}
