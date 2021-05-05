package ac.OneBlood.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;

@AllArgsConstructor
@Builder
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
    private Integer self_exclusion_form_id;
    private Timestamp created_at;
}
