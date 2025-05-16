package bruce.rra_vms.controllers;

import bruce.rra_vms.dto.TransferRequest;
import bruce.rra_vms.dto.VehicleRequest;
import bruce.rra_vms.dto.VehicleResponse;
import bruce.rra_vms.services.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Vehicle Management", description = "APIs for managing vehicles, transfers, and ownership history")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Register a vehicle", description = "Registers a new vehicle with the specified owner and plate number")
    public ResponseEntity<VehicleResponse> registerVehicle(@Valid @RequestBody VehicleRequest request) {
        return ResponseEntity.ok(vehicleService.registerVehicle(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all vehicles", description = "Retrieves all vehicles with pagination")
    public ResponseEntity<Page<VehicleResponse>> getAllVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(vehicleService.getAllVehicles(page, size));
    }

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get vehicles by owner", description = "Retrieves all vehicles owned by a specific owner")
    public ResponseEntity<Page<VehicleResponse>> getVehiclesByOwner(
            @PathVariable Long ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(vehicleService.getVehiclesByOwner(ownerId, page, size));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Search vehicles", description = "Searches for vehicles by chassis number, plate number, or owner's national ID")
    public ResponseEntity<Page<VehicleResponse>> searchVehicles(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(vehicleService.searchVehicles(query, page, size));
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Transfer vehicle ownership", description = "Transfers a vehicle from its current owner to a new owner")
    public ResponseEntity<VehicleResponse> transferVehicle(@Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(vehicleService.transferVehicle(request));
    }

    @GetMapping("/history/{identifier}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get vehicle ownership history", 
               description = "Retrieves the ownership history of a vehicle identified by chassis number or plate number")
    public ResponseEntity<List<Map<String, Object>>> getVehicleOwnershipHistory(@PathVariable String identifier) {
        return ResponseEntity.ok(vehicleService.getVehicleOwnershipHistory(identifier));
    }
}