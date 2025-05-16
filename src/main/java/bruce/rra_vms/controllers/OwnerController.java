package bruce.rra_vms.controllers;

import bruce.rra_vms.dto.OwnerRequest;
import bruce.rra_vms.dto.OwnerResponse;
import bruce.rra_vms.dto.PlateRequest;
import bruce.rra_vms.dto.PlateResponse;
import bruce.rra_vms.services.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owners")
@Tag(name = "Owner Management", description = "APIs for managing vehicle owners and their plate numbers")
public class OwnerController {
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OwnerResponse> createOwner(@Valid @RequestBody OwnerRequest request) {
        return ResponseEntity.ok(ownerService.createOwner(request));
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OwnerResponse>> getOwnersWithVehicles(@RequestParam int page,@RequestParam int size){
        return ResponseEntity.ok(ownerService.getOwnersWithVehicles(page,size));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OwnerResponse>> searchOwners(@RequestParam String query, @RequestParam int page,@RequestParam int size){
        return ResponseEntity.ok(ownerService.searchOwners(query, page, size));
    }

    @GetMapping("/{ownerId}/plates")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get owner's plate numbers", description = "Retrieves all plate numbers associated with a specific owner")
    public ResponseEntity<Page<PlateResponse>> getOwnerPlates(@PathVariable Long ownerId, @RequestParam int page, @RequestParam int size){
        return ResponseEntity.ok(ownerService.getOwnerPlates(ownerId, page,size));
    }

    @PostMapping("/plates")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Register a plate number", description = "Registers a new plate number under a specific owner")
    public ResponseEntity<PlateResponse> registerPlateNumber(@Valid @RequestBody PlateRequest request) {
        return ResponseEntity.ok(ownerService.registerPlateNumber(request));
    }
}
