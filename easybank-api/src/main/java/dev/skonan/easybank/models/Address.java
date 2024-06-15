package dev.skonan.easybank.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "addresses")
public class Address extends AbstractEntity {
    private String street;

    @Column(name = "house_number")
    private Integer houseNumber;

    @Column(name = "zip_code")
    private Integer zipCode;

    private String city;

    private String country;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
