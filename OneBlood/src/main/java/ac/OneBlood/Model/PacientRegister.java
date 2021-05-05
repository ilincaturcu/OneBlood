package ac.OneBlood.Model;

import lombok.*;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Component
public class PacientRegister {
    public Pacient pacient;
    public Credentials credentials;
    public  PersonalInformation personalInformation;
}
