package bruce.rra_vms.repos;

import bruce.rra_vms.models.TransferHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransferHistoryRepo extends JpaRepository<TransferHistory, Long> {
    Page<TransferHistory> findByVehicleId(Long vehicleId, Pageable pageable);

    Page<TransferHistory> findByFromOwnerId(Long ownerId, Pageable pageable);

    Page<TransferHistory> findByToOwnerId(Long ownerId, Pageable pageable);

    Page<TransferHistory> findByTransferDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<TransferHistory> findByVehicleIdOrderByTransferDateDesc(Long vehicleId);
}
