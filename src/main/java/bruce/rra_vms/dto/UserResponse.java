package bruce.rra_vms.dto;

import bruce.rra_vms.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String names;
    private String nationalId;
    private String phone;
    private Role role;
    @JsonProperty(required = false)
    private String ownerId;
}
