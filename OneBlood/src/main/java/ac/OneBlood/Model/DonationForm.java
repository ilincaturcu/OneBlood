package ac.OneBlood.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "donation_form")
public class DonationForm {
    public DonationForm() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer donation_form_id;
    private String fk_donor_code;
    private String id_analize_pre_donare;
    private String id_analize_post_donare;
    private Timestamp created_at;
}
