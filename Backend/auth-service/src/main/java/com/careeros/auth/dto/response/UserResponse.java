package com.careeros.auth.dto.response;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class UserResponse {

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private Set<String> roles;
    private LocalDateTime createdAt;

    public UserResponse() {}

    public UserResponse(UUID id, String email, String firstName, String lastName, boolean enabled, Set<String> roles, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
        this.roles = roles;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static UserResponseBuilder builder() { return new UserResponseBuilder(); }

    public static class UserResponseBuilder {
        private UUID id;
        private String email;
        private String firstName;
        private String lastName;
        private boolean enabled;
        private Set<String> roles;
        private LocalDateTime createdAt;

        public UserResponseBuilder id(UUID id) { this.id = id; return this; }
        public UserResponseBuilder email(String email) { this.email = email; return this; }
        public UserResponseBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public UserResponseBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public UserResponseBuilder enabled(boolean enabled) { this.enabled = enabled; return this; }
        public UserResponseBuilder roles(Set<String> roles) { this.roles = roles; return this; }
        public UserResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public UserResponse build() {
            return new UserResponse(id, email, firstName, lastName, enabled, roles, createdAt);
        }
    }
}
