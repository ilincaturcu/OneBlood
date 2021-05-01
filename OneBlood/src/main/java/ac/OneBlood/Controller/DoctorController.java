package ac.OneBlood.Controller;

import ac.OneBlood.Model.Doctor;
import ac.OneBlood.Repository.DoctorRepository;
import ac.OneBlood.Service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class DoctorController {

    @Autowired
    DoctorService doctorService;


    @RequestMapping(value = "/api/doctors", method = RequestMethod.GET)
    public ResponseEntity<?> listDoctors() { return new ResponseEntity<> ( doctorService.listAllDoctors(), HttpStatus.OK); }


    @RequestMapping(value = "/api/doctor/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> listDoctorById(@PathVariable Integer id) {
        Doctor doctor;
        try { doctor = doctorService.getDoctorById(id); }
        catch(EmptyResultDataAccessException e){ return new ResponseEntity<> ( HttpStatus.BAD_REQUEST); }
        return new ResponseEntity<> (EntityModel.of(doctor,
                linkTo(methodOn(DoctorController.class).listDoctorById(id)).withSelfRel(),
                linkTo(methodOn(DoctorController.class).listDoctors()).withRel("doctors")),  HttpStatus.OK);
    }
}
