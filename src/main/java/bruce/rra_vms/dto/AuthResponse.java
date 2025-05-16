package bruce.rra_vms.dto;

import bruce.rra_vms.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthResponse(
        Long id,
        String email,
        Role role,
        @JsonProperty(required = false)
        String token

) {
}
