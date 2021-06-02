package ac.OneBlood.Model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Date;

@AllArgsConstructor
@Builder
@Getter
@ToString
@Setter
@Entity
@Table(name = "personal_information")
public class PersonalInformation {
    public PersonalInformation() {
    }

    @Id
    private BigInteger CNP;
    private String name;
    private String surname;
    private String mothers_name;
    private String fathers_name;
    private Date birthdate;
    private String identity_card_series;
    private Integer identity_card_number;
    private Integer phone_number;
    private String sex;
    private String job;

}
