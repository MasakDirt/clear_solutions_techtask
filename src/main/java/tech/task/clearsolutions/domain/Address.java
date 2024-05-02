package tech.task.clearsolutions.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Column
    private String city;

    @Column
    private String street;

    @Column
    private String number;

    @Column(name = "apartment_number")
    private String apartmentNumber;

}
