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
@Table(name = "appointment")
public class Appointment {
    public Appointment() {
    }

    @Id
    private Integer appointment_id;
    private Integer fk_donor_code;
    private Integer fk_doctor_code;
    private Date appointment_date;
}
