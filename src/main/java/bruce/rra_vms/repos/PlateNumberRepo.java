package bruce.rra_vms.repos;

import bruce.rra_vms.enums.PlateStatus;
import bruce.rra_vms.models.PlateNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlateNumberRepo extends JpaRepository<PlateNumber, Long> {
    Page<PlateNumber> findByOwnerId(Long ownerId, Pageable pageable);

    List<PlateNumber> findByOwnerIdAndStatus(Long ownerId, PlateStatus status);

    Optional<PlateNumber> findByPlateNumber(String plateNumber);

    Page<PlateNumber> findByStatus(PlateStatus status, Pageable pageable);
}
