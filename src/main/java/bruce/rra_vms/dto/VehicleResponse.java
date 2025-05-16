package bruce.rra_vms.dto;

public record VehicleResponse(
        Long id,
        String model,
        String price,
        String ownerEmail,
        String plateNumber
) {
}
