package bruce.rra_vms.dto;

public record OwnerRequest(
        String names,
        String nationalId,
        String address,
        String phone,
        Long userId
) {
}
