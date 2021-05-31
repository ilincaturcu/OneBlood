package ac.OneBlood.Service;

import ac.OneBlood.Model.*;
import net.minidev.json.JSONObject;
import org.springframework.boot.test.web.client.TestRestTemplate;
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
            restTemplate.put("http://localhost:9090/api/credentialsrole", entityCredentialsRole, String.class);
            personalInformation.setCNP(pacient.getCNP());
            HttpEntity<Object> entityPersonal = new HttpEntity<>(personalInformation, headers);
            restTemplate.put("http://localhost:9090/api/personalInformation/" + pacient.getCNP(), entityPersonal);

        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            System.out.println("a intrat in catch :(");
            if (!HttpStatus.OK.equals(httpClientOrServerExc.getStatusCode())) {
                System.out.println(httpClientOrServerExc.getMessage());
            }

        }
    }



    public String getDonorCodeByCredentials(Credentials credentials, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        String donorCode=null;
        Credentials credentials1=null;

        try {
            credentials1 = new TestRestTemplate().exchange(
                    "http://localhost:9090/api/cont/email/" + credentials.getEmail(), HttpMethod.GET, entity, Credentials.class).getBody();
            donorCode = new TestRestTemplate().exchange(
                    "http://localhost:9090/api/pacient/accountId/" + credentials1.getAccount_id().toString(), HttpMethod.GET, entity, String.class).getBody();

        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            System.out.println("a intrat in catch :(");
            if (!HttpStatus.OK.equals(httpClientOrServerExc.getStatusCode())) {
                System.out.println(httpClientOrServerExc.getMessage());
            }

            return donorCode;
        }
        return donorCode;
    }


    public String postPredonareData(RestTemplate restTemplate, JSONObject predonareData) {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<String> predonareId = null;
        HttpEntity<Object> entity = new HttpEntity<>(predonareData, headers);
        try {
            predonareId = restTemplate.postForEntity("http://localhost:7070/api/predonare", entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            System.out.println("a intrat in catch :(");
        }
        return predonareId.getBody();
    }


    public String postPostdonareData(RestTemplate restTemplate, JSONObject postdonareData) {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<String> postdonareId = null;
        HttpEntity<Object> entity = new HttpEntity<>(postdonareData, headers);
        try {
            postdonareId = restTemplate.postForEntity("http://localhost:7070/api/postdonare", entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            System.out.println("a intrat in catch :(");
        }
        return postdonareId.getBody();
    }


    public ResponseEntity<String> getPostdonareDataByDateAndDonorCode(RestTemplate restTemplate, String date, String donor_code) {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<String> postdonareId = null;
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        try {
            postdonareId = restTemplate.getForEntity("http://localhost:7070/api/postdonare/date/" + date + "/donor_code/" + donor_code, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            System.out.println("a intrat in catch :(");
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postdonareId.getBody(), HttpStatus.OK);
    }


    public ResponseEntity<String> getPredonareDataByDateAndDonorCode(RestTemplate restTemplate, String date, String donor_code) {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<String> predonareId = ResponseEntity.ok("default state");
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        try {
            predonareId = restTemplate.getForEntity("http://localhost:7070/api/predonare/date/" + date + "/donor_code/" + donor_code, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            System.out.println("a intrat in catch :(");
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(predonareId.getBody(), HttpStatus.OK);
    }
}
