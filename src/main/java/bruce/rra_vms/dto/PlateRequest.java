package bruce.rra_vms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record PlateRequest(
        @NotBlank(message = "Plate number is required")
        @Pattern(regexp = "^RA[A-Z]\\d{3}[A-Z]$", message = "Invalid plate number format")
        String plateNumber,

        @NotNull(message = "Issue date is required")
        LocalDate issuedAt,

        @NotNull(message = "Owner ID is required")
        Long ownerId
) {
}
