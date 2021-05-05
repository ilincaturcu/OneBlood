package ac.OneBlood.Service;

import ac.OneBlood.Model.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


@Service
public class Aggregator {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public void postAccountWithDoctorRole(RestTemplate restTemplate, Credentials credentials, Doctor doctor, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Object> entityCredentials = new HttpEntity<>(credentials, headers);

        try {
            ResponseEntity<Integer> accountId = restTemplate.postForEntity("http://localhost:9090/api/credentials", entityCredentials, Integer.class);
            doctor.setFk_account_id(accountId.getBody());
            HttpEntity<Object> entityDoctor = new HttpEntity<>(doctor, headers);
            restTemplate.put("http://localhost:9090/api/doctor/" + doctor.getDoctor_code(), entityDoctor);
            HttpEntity<Object> entityCredentialsRole = new HttpEntity<>(new CredentialsRole(accountId.getBody(), 2), headers);
            restTemplate.put("http://localhost:9090/api/credentialsrole", entityCredentialsRole);

        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {

            System.out.println("a intrat in catch :(");
            //           if (!HttpStatus.OK.equals(httpClientOrServerExc.getStatusCode())) {
//                ResponseEntity<Carte> resultCarte = restTemplate.postForEntity("http://localhost:9090/api/carti", carte, Carte.class);
//                System.out.println("carte");
//                System.out.println(resultCarte.getStatusCode());
//                System.out.println(resultCarte.getBody().toString());
        }
    }


    public void postAccountWithPacientRole(RestTemplate restTemplate, Credentials credentials, Pacient pacient, PersonalInformation personalInformation, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Object> entityCredentials = new HttpEntity<>(credentials, headers);

        try {
            ResponseEntity<Integer> accountId = restTemplate.postForEntity("http://localhost:9090/api/credentials", entityCredentials, Integer.class);
            pacient.setFk_account_id(accountId.getBody());
            HttpEntity<Object> entityPacient = new HttpEntity<>(pacient, headers);
            restTemplate.put("http://localhost:9090/api/pacient/" + pacient.getDonor_code(), entityPacient);
            HttpEntity<Object> entityCredentialsRole = new HttpEntity<>(new CredentialsRole(accountId.getBody(), 1), headers);
            restTemplate.put("http://localhost:9090/api/credentialsrole", entityCredentialsRole);
            personalInformation.setCNP(pacient.getCNP());
            restTemplate.put("http://localhost:9090/api/personalInformation/" + pacient.getCNP(), personalInformation);

        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {

            System.out.println("a intrat in catch :(");
            //           if (!HttpStatus.OK.equals(httpClientOrServerExc.getStatusCode())) {
//                ResponseEntity<Carte> resultCarte = restTemplate.postForEntity("http://localhost:9090/api/carti", carte, Carte.class);
//                System.out.println("carte");
//                System.out.println(resultCarte.getStatusCode());
//                System.out.println(resultCarte.getBody().toString());
        }
    }
}