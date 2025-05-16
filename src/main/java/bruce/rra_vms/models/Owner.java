package bruce.rra_vms.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "owners")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    @Column(unique = true)
    private String names;

    @Pattern(regexp = "^\\d{16}$", message = "Invalid Rwandan national ID")
    private String nationalId;

    @Column(unique = true)
    @Pattern(regexp = "^(?:\\+250|0)7[238][0-9]{7}$", message = "Invalid Rwandan phone number")
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "owner")
    private List<Vehicle> vehicles = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<PlateNumber> plates = new ArrayList<>();

    public Owner(String address, String names, String nationalId, String phone) {
        this.address = address;
        this.names = names;
        this.nationalId = nationalId;
        this.phone = phone;
    }
}
