package bruce.rra_vms.models;

import bruce.rra_vms.enums.PlateStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "plates")
public class PlateNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^RA[A-Z]\\d{3}[A-Z]$",message = "Invalid plate number")
    private String plateNumber;

    private LocalDate issuedDate;

    @Enumerated(EnumType.STRING)
    private PlateStatus status = PlateStatus.AVAILABLE;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private Owner owner;

    @OneToOne(mappedBy = "plateNumber")
    @JsonIgnore
    private Vehicle vehicle;
}
