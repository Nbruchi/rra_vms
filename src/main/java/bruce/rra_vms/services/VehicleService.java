package bruce.rra_vms.services;

import bruce.rra_vms.dto.TransferRequest;
import bruce.rra_vms.dto.VehicleRequest;
import bruce.rra_vms.dto.VehicleResponse;
import bruce.rra_vms.enums.PlateStatus;
import bruce.rra_vms.errors.OwnerNotFoundException;
import bruce.rra_vms.errors.PlateNotFoundException;
import bruce.rra_vms.errors.VehicleNotFoundException;
import bruce.rra_vms.models.Owner;
import bruce.rra_vms.models.PlateNumber;
import bruce.rra_vms.models.TransferHistory;
import bruce.rra_vms.models.Vehicle;
import bruce.rra_vms.repos.OwnerRepo;
import bruce.rra_vms.repos.PlateNumberRepo;
import bruce.rra_vms.repos.TransferHistoryRepo;
import bruce.rra_vms.repos.VehicleRepo;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VehicleService {
    private final VehicleRepo vehicleRepo;
    private final OwnerRepo ownerRepo;
    private final PlateNumberRepo plateRepo;
    private final TransferHistoryRepo transferRepo;
    private final ModelMapper modelMapper;

    public VehicleService(VehicleRepo vehicleRepo, OwnerRepo ownerRepo, PlateNumberRepo plateRepo, 
                         TransferHistoryRepo transferRepo, ModelMapper modelMapper) {
        this.vehicleRepo = vehicleRepo;
        this.ownerRepo = ownerRepo;
        this.plateRepo = plateRepo;
        this.transferRepo = transferRepo;
        this.modelMapper = modelMapper;
    }

    /**
     * Register a new vehicle with the specified owner and plate number
     */
    @Transactional
    public VehicleResponse registerVehicle(VehicleRequest request) {
        // Find the owner
        Owner owner = ownerRepo.findById(request.ownerId())
                .orElseThrow(() -> new OwnerNotFoundException("Owner not found with id: " + request.ownerId()));

        // Find the plate number
        PlateNumber plateNumber = plateRepo.findByPlateNumber(request.plateNumber())
                .orElseThrow(() -> new PlateNotFoundException("Plate number not found: " + request.plateNumber()));

        // Verify the plate belongs to the owner and is available
        if (!plateNumber.getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("Plate number does not belong to the specified owner");
        }

        if (plateNumber.getStatus() != PlateStatus.AVAILABLE) {
            throw new IllegalArgumentException("Plate number is already in use");
        }

        // Create the vehicle
        Vehicle vehicle = new Vehicle();
        vehicle.setModel(request.model());
        vehicle.setManufacturer(request.manufacturer());
        vehicle.setManufacturedYear(request.manufacturedYear());
        vehicle.setPrice(request.price());
        vehicle.setChasisNumber(request.chasisNumber());
        vehicle.setOwner(owner);
        vehicle.setPlateNumber(plateNumber);

        // Update plate number status
        plateNumber.setStatus(PlateStatus.IN_USE);
        plateNumber.setVehicle(vehicle);
        plateRepo.save(plateNumber);

        // Save the vehicle
        Vehicle savedVehicle = vehicleRepo.save(vehicle);

        return modelMapper.map(savedVehicle, VehicleResponse.class);
    }

    /**
     * Get all vehicles with pagination
     */
    public Page<VehicleResponse> getAllVehicles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Vehicle> vehicles = vehicleRepo.findAll(pageable);
        return vehicles.map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class));
    }

    /**
     * Get vehicles by owner ID with pagination
     */
    public Page<VehicleResponse> getVehiclesByOwner(Long ownerId, int page, int size) {
        // Verify the owner exists
        if (!ownerRepo.existsById(ownerId)) {
            throw new OwnerNotFoundException("Owner not found with id: " + ownerId);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Vehicle> vehicles = vehicleRepo.findByOwnerId(ownerId, pageable);
        return vehicles.map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class));
    }

    /**
     * Search vehicles by chassis number, plate number, or owner's national ID
     */
    public Page<VehicleResponse> searchVehicles(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Vehicle> vehicles = vehicleRepo.searchVehicles(query, pageable);
        return vehicles.map(vehicle -> modelMapper.map(vehicle, VehicleResponse.class));
    }

    /**
     * Transfer a vehicle from its current owner to a new owner
     */
    @Transactional
    public VehicleResponse transferVehicle(TransferRequest request) {
        // Find the vehicle
        Vehicle vehicle = vehicleRepo.findById(request.vehicleId())
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + request.vehicleId()));

        // Find the new owner
        Owner newOwner = ownerRepo.findById(request.newOwnerId())
                .orElseThrow(() -> new OwnerNotFoundException("New owner not found with id: " + request.newOwnerId()));

        // Find the new plate number
        PlateNumber newPlateNumber = plateRepo.findById(request.newPlateNumberId())
                .orElseThrow(() -> new PlateNotFoundException("New plate number not found with id: " + request.newPlateNumberId()));

        // Verify the new plate belongs to the new owner and is available
        if (!newPlateNumber.getOwner().getId().equals(newOwner.getId())) {
            throw new IllegalArgumentException("New plate number does not belong to the new owner");
        }

        if (newPlateNumber.getStatus() != PlateStatus.AVAILABLE) {
            throw new IllegalArgumentException("New plate number is already in use");
        }

        // Get the current owner and plate number
        Owner currentOwner = vehicle.getOwner();
        PlateNumber currentPlateNumber = vehicle.getPlateNumber();

        // Create a transfer history record
        TransferHistory transferHistory = new TransferHistory();
        transferHistory.setVehicle(vehicle);
        transferHistory.setFromOwner(currentOwner);
        transferHistory.setToOwner(newOwner);
        transferHistory.setAmount(request.amount());
        transferHistory.setTransferDate(LocalDate.now());
        transferRepo.save(transferHistory);

        // Update the current plate number status
        currentPlateNumber.setStatus(PlateStatus.AVAILABLE);
        currentPlateNumber.setVehicle(null);
        plateRepo.save(currentPlateNumber);

        // Update the new plate number status
        newPlateNumber.setStatus(PlateStatus.IN_USE);
        newPlateNumber.setVehicle(vehicle);
        plateRepo.save(newPlateNumber);

        // Update the vehicle with a new owner and plate number
        vehicle.setOwner(newOwner);
        vehicle.setPlateNumber(newPlateNumber);
        Vehicle updatedVehicle = vehicleRepo.save(vehicle);

        return modelMapper.map(updatedVehicle, VehicleResponse.class);
    }

    /**
     * Get the ownership history of a vehicle
     */
    public List<Map<String, Object>> getVehicleOwnershipHistory(String identifier) {
        // Find the vehicle by chassis number or plate number
        Vehicle vehicle;
        try {
            vehicle = vehicleRepo.findByChasisNumber(identifier)
                    .orElse(plateRepo.findByPlateNumber(identifier)
                            .map(PlateNumber::getVehicle)
                            .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with identifier: " + identifier)));
        } catch (Exception e) {
            throw new VehicleNotFoundException("Vehicle not found with identifier: " + identifier);
        }

        // Get all transfer history records for the vehicle
        List<TransferHistory> transferHistories = transferRepo.findByVehicleIdOrderByTransferDateDesc(vehicle.getId());

        // Map transfer history to a more readable format
        return transferHistories.stream()
                .map(history -> Map.of(
                        "transferDate", history.getTransferDate(),
                        "fromOwner", history.getFromOwner().getNames(),
                        "toOwner", history.getToOwner().getNames(),
                        "amount", history.getAmount(),
                        "vehicleDetails", Map.of(
                                "model", vehicle.getModel(),
                                "manufacturer", vehicle.getManufacturer(),
                                "chasisNumber", vehicle.getChasisNumber(),
                                "plateNumber", vehicle.getPlateNumber().getPlateNumber()
                        )
                ))
                .collect(Collectors.toList());
    }
}