package ac.OneBlood;

import ac.OneBlood.Model.Credentials;
import ac.OneBlood.Repository.CredentialsRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class OneBloodApplication {
	private CredentialsRepository credentialsRepository;
//	@PostConstruct
//	public void initUsers(){
//		List<Credentials> credentialsList = Stream.of(
//				new Credentials(101, "ilincafturcu@gmail.com", "Ilinca-113473"),
//				new Credentials(102, "ilincaturcu@yahoo.com", "Ilinca-123456"),
//				new Credentials(103, "ilinca.turcu@bestis.ro", "Ilinca-BEST")
//
//		).collect(Collectors.toList());
//		credentialsRepository.saveAll(credentialsList);
//	}

//	@PostConstruct
//	public void initUsers(){
//		Credentials credentials = new Credentials( "ilincafturcu@gmail.com", "Ilinca-113473");
//		credentialsRepository.saveAndFlush(credentials);
//	}
	public static void main(String[] args) {
		SpringApplication.run(OneBloodApplication.class, args);
	}

}
