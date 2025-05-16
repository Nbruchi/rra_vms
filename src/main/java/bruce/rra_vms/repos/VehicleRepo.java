package bruce.rra_vms.repos;

import bruce.rra_vms.models.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle, Long> {
    Page<Vehicle> findByOwnerId(Long ownerId, Pageable pageable);

    Optional<Vehicle> findByPlateNumberId(Long plateNumberId);

    Optional<Vehicle> findByChasisNumber(String chasisNumber);

    @Query("SELECT v FROM Vehicle v WHERE " +
           "v.chasisNumber = :query OR " +
           "v.plateNumber.plateNumber = :query OR " +
           "v.owner.nationalId = :query")
    Page<Vehicle> searchVehicles(@Param("query") String query, Pageable pageable);
}
