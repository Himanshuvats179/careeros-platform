package com.careeros.auth.dto.request;

import com.careeros.auth.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private String firstName;
    private String lastName;

    private Set<Role> roles;

    public RegisterRequest() {}

    public RegisterRequest(String email, String password, String firstName, String lastName, Set<Role> roles) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public static RegisterRequestBuilder builder() { return new RegisterRequestBuilder(); }

    public static class RegisterRequestBuilder {
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private Set<Role> roles;

        public RegisterRequestBuilder email(String email) { this.email = email; return this; }
        public RegisterRequestBuilder password(String password) { this.password = password; return this; }
        public RegisterRequestBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public RegisterRequestBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public RegisterRequestBuilder roles(Set<Role> roles) { this.roles = roles; return this; }

        public RegisterRequest build() {
            return new RegisterRequest(email, password, firstName, lastName, roles);
        }
    }
}
