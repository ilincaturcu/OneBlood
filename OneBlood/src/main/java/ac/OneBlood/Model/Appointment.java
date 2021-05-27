package ac.OneBlood.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "appointment")
public class Appointment {
    public Appointment() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appointment_id;
    private String fk_donor_code;
    private Integer fk_doctor_code;
    private Date appointment_date;
    private String appointment_hour;
    private String appointment_status;
}



