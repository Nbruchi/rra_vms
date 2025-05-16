package bruce.rra_vms.dto;

public record OwnerResponse(
        Long id,
        String names,
        String email,
        String nationalId,
        String userId
) {
}
