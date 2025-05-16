package bruce.rra_vms.dto;

import bruce.rra_vms.enums.PlateStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record PlateResponse(
        Long id,
        String plateNumber,
        PlateStatus status,
        LocalDate issuedAt,
        @JsonProperty(required = false)
        Long ownerEmail,
        @JsonProperty(required = false)
        Long vehicleModel
) {
}
