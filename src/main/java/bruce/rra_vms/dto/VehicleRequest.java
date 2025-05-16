package bruce.rra_vms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record VehicleRequest(
        @NotNull(message = "Owner ID is required")
        Long ownerId,

        @NotBlank(message = "Model is required")
        String model,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price,

        @NotBlank(message = "Manufacturer is required")
        String manufacturer,

        @NotBlank(message = "Manufactured year is required")
        String manufacturedYear,

        @NotBlank(message = "Plate number is required")
        @Pattern(regexp = "^RA[A-Z]\\d{3}[A-Z]$", message = "Invalid plate number format")
        String plateNumber,

        @NotBlank(message = "Chassis number is required")
        @Pattern(regexp = "^AN\\d{5}$", message = "Invalid chassis number format")
        String chasisNumber
) {
}
