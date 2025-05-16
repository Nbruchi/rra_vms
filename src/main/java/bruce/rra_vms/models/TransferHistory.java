package bruce.rra_vms.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transfers")
public class TransferHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private LocalDate transferDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "from_owner_id")
    private Owner fromOwner;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "to_owner_id")
    private Owner toOwner;
}
