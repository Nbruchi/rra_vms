package bruce.rra_vms.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * DTO for vehicle transfer request
 */
public record TransferRequest(
        @NotNull(message = "Vehicle ID is required")
        Long vehicleId,
        
        @NotNull(message = "New owner ID is required")
        Long newOwnerId,
        
        @NotNull(message = "New plate number ID is required")
        Long newPlateNumberId,
        
        @NotNull(message = "Transfer amount is required")
        @Positive(message = "Transfer amount must be positive")
        BigDecimal amount
) {
}