package ac.OneBlood.Model;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;

@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
@Entity
@Table(name = "pacient")
public class Pacient {
    public Pacient() {
    }
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer pacient_id;
    private BigInteger CNP;
    private String donor_code;
    private Integer fk_account_id;
    private String self_exclusion_form_id;
    private Timestamp created_at;
    private String status;
}
/*enum Status {
    PROGRESS,
    PENDING,
    CANCELLED,
    COMPLETED
}*/