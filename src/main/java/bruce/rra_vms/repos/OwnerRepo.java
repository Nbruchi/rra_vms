package bruce.rra_vms.repos;

import bruce.rra_vms.models.Owner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepo extends JpaRepository<Owner, Long> {
    boolean existsByNationalId(String nationalId);

    Page<Owner> findByVehiclesIsNotEmpty(Pageable pageable);

    @Query("SELECT o FROM Owner o WHERE " +
    "o.nationalId LIKE %:query% OR " +
    "o.phone LIKE %:query% OR " +
    "o.user.email LIKE %:query%")
    Page<Owner> search(@Param("query") String query, Pageable pageable);
}
