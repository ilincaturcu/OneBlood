package ac.OneBlood.Controller;

import ac.OneBlood.Model.Appointment;
import ac.OneBlood.Service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @RequestMapping(value = "/api/appointments", method = RequestMethod.GET)
    public ResponseEntity<?> listAllAppointments() {
        System.out.println(appointmentService.listAllAppointments().toString());
        return new ResponseEntity<> ( appointmentService.listAllAppointments(), HttpStatus.OK);
    }

    //intoarce cartea in functie de id, impreuna cu legaturile catre self, parinte si autori
    @RequestMapping(value = "/api/appointment/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAppointmentById(@PathVariable Integer id) {
        Appointment appointment;
        try { appointment = appointmentService.getAppointmentById(id); }
        catch(EmptyResultDataAccessException e){ return new ResponseEntity<> ( HttpStatus.BAD_REQUEST); }
        return new ResponseEntity<>( EntityModel.of(appointment,
                linkTo(methodOn(AppointmentController.class).getAppointmentById(id)).withSelfRel(),
                linkTo(methodOn(AppointmentController.class).listAllAppointments()).withRel("appointments"),
                linkTo(methodOn(DoctorController.class).listDoctorById(appointment.getFk_doctor_code())).withRel("DoctorAppointment")),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/api/appointments",method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<?> addNewAppointment(@RequestBody Appointment appointment) {
        //verifici daca exista, daca nu exista il creezi => 201 created
        //daca exista ii faci update 200ok
        try{appointmentService.getAppointmentById(appointment.getAppointment_id());}
        catch (Exception e){
            appointmentService.save(appointment);
            return new ResponseEntity<>(appointment, HttpStatus.CREATED);
        }
        appointmentService.save(appointment);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }
}
