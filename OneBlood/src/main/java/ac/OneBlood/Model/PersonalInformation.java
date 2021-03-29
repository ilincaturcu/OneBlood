package ac.OneBlood.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "peronal_information")
public class PersonalInformation {
    public PersonalInformation() {
    }

    @Id
    private Integer CNP;
    private String name;
    private String surname;
    private String mothersName;
    private String fathersName;
    private Date birthdate;
    private String identity_card_series;
    private Integer identity_card_number;
    private Integer phone_number;
    private String sex;
    private String job;

}