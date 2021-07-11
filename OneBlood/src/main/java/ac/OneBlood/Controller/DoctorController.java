package ac.OneBlood.Controller;

import ac.OneBlood.Model.Doctor;
import ac.OneBlood.Service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @RequestMapping(value = "/api/doctors", method = RequestMethod.GET)
    public ResponseEntity<?> listDoctors() {
        return new ResponseEntity<>(doctorService.listAllDoctors(), HttpStatus.OK);
    }
//intoarce informatiile despre doctor pe baza id ului sau, cu legaturi catre ceilalti doctori
    @RequestMapping(value = "/api/doctor/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> listDoctorById(@PathVariable Integer id) {
        Doctor doctor;
        try {
            doctor = doctorService.getDoctorById(id);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(EntityModel.of(doctor,
                linkTo(methodOn(DoctorController.class).listDoctorById(id)).withSelfRel(),
                linkTo(methodOn(DoctorController.class).listDoctors()).withRel("doctors")), HttpStatus.OK);
    }

    //creeaza o noua entitate de doctor
    @RequestMapping(value = "/api/doctor/{doctor_code}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<?> addDoctor(@RequestBody Doctor doctor) {
        try {
            doctorService.getDoctorById(doctor.getDoctor_code());
        } catch (Exception e) {
            doctorService.save(doctor);
            return new ResponseEntity<>(doctor, HttpStatus.CREATED);
        }
        doctorService.save(doctor);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    //afiseaza doctului pe baza id ului
    @RequestMapping(value = "/api/doctor/doctor_code/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> listDoctorCodeById(@PathVariable String id) {
        Doctor doctor;
        try {
            doctor = doctorService.getDoctorByAccountId(Integer.parseInt(id));
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(doctor.getDoctor_code(), HttpStatus.OK);
    }
}
