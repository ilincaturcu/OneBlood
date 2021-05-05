package ac.OneBlood.Model;

import lombok.*;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Component
public class DoctorRegister {
    public Doctor doctor;
    public Credentials credentials;
}
