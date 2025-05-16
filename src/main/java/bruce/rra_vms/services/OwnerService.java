package bruce.rra_vms.services;

import bruce.rra_vms.dto.OwnerRequest;
import bruce.rra_vms.dto.OwnerResponse;
import bruce.rra_vms.dto.PlateRequest;
import bruce.rra_vms.dto.PlateResponse;
import bruce.rra_vms.enums.PlateStatus;
import bruce.rra_vms.errors.OwnerNotFoundException;
import bruce.rra_vms.errors.UserNotFoundException;
import bruce.rra_vms.models.Owner;
import bruce.rra_vms.models.PlateNumber;
import bruce.rra_vms.models.User;
import bruce.rra_vms.repos.OwnerRepo;
import bruce.rra_vms.repos.PlateNumberRepo;
import bruce.rra_vms.repos.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {
    private final ModelMapper modelMapper;
    private final OwnerRepo ownerRepo;
    private final UserRepo userRepo;
    private final PlateNumberRepo plateRepo;

    public OwnerService(ModelMapper modelMapper, OwnerRepo ownerRepo, UserRepo userRepo, PlateNumberRepo plateRepo) {
        this.modelMapper = modelMapper;
        this.ownerRepo = ownerRepo;
        this.userRepo = userRepo;
        this.plateRepo = plateRepo;
    }

    public OwnerResponse createOwner(OwnerRequest request){
        if (ownerRepo.existsByNationalId(request.nationalId())){
            throw new IllegalArgumentException("Owner already exists");
        }

        User user = userRepo.findById(request.userId()).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + request.userId()));

        Owner owner = new Owner(request.address(), request.names(), request.nationalId(),request.phone());
        owner.setUser(user);

        Owner savedOwner =ownerRepo.save(owner);

        return modelMapper.map(savedOwner, OwnerResponse.class);
    }

    public Page<OwnerResponse> getOwnersWithVehicles(int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Owner> owners = ownerRepo.findByVehiclesIsNotEmpty(pageable);
        return owners.map(owner -> modelMapper.map(owner, OwnerResponse.class));
    }

    public Page<OwnerResponse> searchOwners(String query, int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Owner> owners = ownerRepo.search(query, pageable);
        return owners.map(owner -> modelMapper.map(owner, OwnerResponse.class));
    }

    public Page<PlateResponse> getOwnerPlates(Long ownerId, int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<PlateNumber> ownerPlates = plateRepo.findByOwnerId(ownerId, pageable);
        return ownerPlates.map(plate -> modelMapper.map(plate, PlateResponse.class));
    }

    public PlateResponse registerPlateNumber(PlateRequest request) {
        Owner owner = ownerRepo.findById(request.ownerId())
                .orElseThrow(() -> new OwnerNotFoundException("Owner not found with id: " + request.ownerId()));

        PlateNumber plateNumber = new PlateNumber();
        plateNumber.setPlateNumber(request.plateNumber());
        plateNumber.setIssuedDate(request.issuedAt());
        plateNumber.setStatus(PlateStatus.AVAILABLE);
        plateNumber.setOwner(owner);

        PlateNumber savedPlate = plateRepo.save(plateNumber);

        return modelMapper.map(savedPlate, PlateResponse.class);
    }
}
