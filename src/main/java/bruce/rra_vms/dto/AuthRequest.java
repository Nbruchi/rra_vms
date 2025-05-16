package bruce.rra_vms.dto;

import bruce.rra_vms.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AuthRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        String password,

        @JsonProperty(required = false)
        String names,

        @JsonProperty(required = false)
        @Pattern(regexp = "^\\d{16}$", message = "Invalid Rwandan national ID")
        String nationalId,

        @JsonProperty(required = false)
        @Pattern(regexp = "^(?:\\+250|0)7[238][0-9]{7}$", message = "Invalid Rwandan phone number")
        String phone,

        @JsonProperty(required = false)
        Role role
) {
    public Role role(){
        return role == null ? Role.ROLE_STANDARD : role;
    }
}
