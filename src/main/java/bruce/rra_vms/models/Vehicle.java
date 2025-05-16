package bruce.rra_vms.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;
    private String manufacturer;
    private String manufacturedYear;
    private BigDecimal price;

    @Pattern(regexp = "^AN\\d{5}$", message = "Invalid chasis number")
    private String chasisNumber;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private Owner owner;

    @OneToOne
    @JoinColumn(name = "plate_number_id")
    private PlateNumber plateNumber;

    @OneToMany(mappedBy = "vehicle")
    private List<TransferHistory> transfers = new ArrayList<>();
}
