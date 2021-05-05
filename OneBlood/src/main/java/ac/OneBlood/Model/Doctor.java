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
@Table(name = "doctor")
public class Doctor {
    public Doctor() {
    }

    @Id
    private Integer doctor_code;
    private Integer fk_account_id;
    private String name;
    private String surname;
}
