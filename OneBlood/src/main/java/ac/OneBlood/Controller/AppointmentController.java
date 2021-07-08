package ac.OneBlood.Controller;

import ac.OneBlood.Model.Appointment;
import ac.OneBlood.Service.AppointmentService;
import ac.OneBlood.Util.MailService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private MailService mailService;

    @CrossOrigin
    @RequestMapping(value = "/api/appointments", method = RequestMethod.GET)
    public ResponseEntity<?> listAllAppointments() {
        System.out.println(appointmentService.listAllAppointments().toString());
        return new ResponseEntity<>(appointmentService.listAllAppointments(), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/appointment/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAppointmentById(@PathVariable Integer id) {
        Appointment appointment;
        try {
            appointment = appointmentService.getAppointmentById(id);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(EntityModel.of(appointment,
                linkTo(methodOn(AppointmentController.class).getAppointmentById(id)).withSelfRel(),
                linkTo(methodOn(AppointmentController.class).listAllAppointments()).withRel("appointments"),
                linkTo(methodOn(DoctorController.class).listDoctorById(appointment.getFk_doctor_code())).withRel("DoctorAppointment")),
                HttpStatus.OK);
    }


    @CrossOrigin
    @RequestMapping(value = "/api/appointment/donor/{donor_code}", method = RequestMethod.GET)
    public ResponseEntity<?> getAppointmentByDonorCode(@PathVariable String donor_code) throws NotFoundException {
        List<Appointment> appointments;
        try {
            appointments = appointmentService.getAppointmentByDonorCode(donor_code);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/appointment/doctor/{doctor_code}", method = RequestMethod.GET)
    public ResponseEntity<?> getAppointmentByDoctorCode(@PathVariable Integer doctor_code) {
        List<Appointment> appointments;
        try {
            appointments = appointmentService.getAllAppointmentsByDoctorCode(doctor_code);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    //pentru paginare, numarul de progrmari
    @CrossOrigin
    @RequestMapping(value = "/api/appointment/doctor/{doctor_code}/number", method = RequestMethod.GET)
    public ResponseEntity<?> getNumberOfAppointmentByDoctorCode(@PathVariable Integer doctor_code) {
        List<Appointment> appointments;
        try {
            appointments = appointmentService.getAllAppointmentsByDoctorCode(doctor_code).stream().filter(appointment -> appointment.getAppointment_status() != "deleted").collect(Collectors.toList());
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(appointments.size(), HttpStatus.OK);
    }


    @CrossOrigin
    @RequestMapping(value = "/api/appointment/doctor/{doctor_code}/date/{timestamp}", method = RequestMethod.GET)
    public List<Appointment> getAppointmentByDoctorCodeAfterDate(@PathVariable Integer doctor_code, @PathVariable Long timestamp) {
        List<Appointment> appointments;
        try {
            appointments = appointmentService.getAllAppointmentsByDoctorCode(doctor_code)
                    .stream()
                    .filter(appointment -> appointment.getAppointment_date().getTime() > timestamp)
                    .collect(Collectors.toList());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return appointments;
    }


    @CrossOrigin
    //toate programarile unui anumit doctor dintr-o anumita zi, la o anumita ora (data ca timestamp)
    @RequestMapping(value = "/api/appointment/doctor/{doctor_code}/day/{dateIn}/hour/{hour}", method = RequestMethod.GET)
    public ResponseEntity<?> getAppointmentByDoctorPerHour(@PathVariable Integer doctor_code, @PathVariable String dateIn, @PathVariable String hour) throws ParseException {
        List<Appointment> appointments = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date appointmentDate = sdf.parse(dateIn);

        System.out.println(dateIn);
        System.out.println(hour);
        try {
            //toate programarile doctorului din acea zi, la acea ora
            appointments = appointmentService.getAllAppointmentsByDoctorCode(doctor_code).stream()
                    .filter(appointment -> appointment.getAppointment_date().toString().contains(dateIn))
                    .filter(appointment -> appointment.getAppointment_hour().equals(hour))
                    .collect(Collectors.toList());

        } catch (EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }


    @CrossOrigin
    //toate sloturile disponibile ale unui anumit doctor intr-o anumita zi
    @RequestMapping(value = "/api/appointment/doctor/{doctor_code}/day/{dateIn}/hours", method = RequestMethod.GET)
    public ResponseEntity<?> getFREEAppointmentByDoctorPerHour(@PathVariable Integer doctor_code, @PathVariable String dateIn) throws ParseException {
        ArrayList<String> freeSlots = new ArrayList<String>(10);
        List<String> hoursArray = Arrays.asList("08:00:00", "08:30:00", "09:00:00", "09:30:00", "10:00:00", "10:30:00", "11:00:00", "12:00:00", "12:30:00");
        for (String hour : hoursArray) {
            try {
                ArrayList ocupiedSlots = (ArrayList) this.getAppointmentByDoctorPerHour(doctor_code, dateIn, hour).getBody();
                assert ocupiedSlots != null;
                if (ocupiedSlots.size() <= 2) {
                    freeSlots.add(hour);
                }

            } catch (EmptyResultDataAccessException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        System.out.println(freeSlots);
        return new ResponseEntity<>(freeSlots, HttpStatus.OK);
    }


    @CrossOrigin
    @RequestMapping(value = "/api/appointments", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<?> addNewAppointment(@RequestBody Appointment appointment) {
        try {
            appointmentService.getAppointmentById(appointment.getAppointment_id());
        } catch (Exception e) {
            appointmentService.save(appointment);
            return new ResponseEntity<>(appointment, HttpStatus.CREATED);
        }
        appointmentService.save(appointment);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }


    @CrossOrigin
    @RequestMapping(value = "/api/appointment/{status}/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<?> changeAppointmentStatus(@PathVariable String status, @PathVariable Integer id) {
        Appointment appointment;
        try {
            appointment = appointmentService.getAppointmentById(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        appointment.setAppointment_status(status);
        appointmentService.save(appointment);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/appointment/current/{donor_code}", method = RequestMethod.GET)
    public Boolean doesTheDonorHaveAPendingApp(@PathVariable String donor_code) {
        List<Appointment> appointments = null;
        try {
            appointments = appointmentService.getAppointmentByDonorCode(donor_code)
                    .stream()
                    .filter(appointment -> appointment.getAppointment_status().equals("pending"))
                    .collect(Collectors.toList());
            appointments.forEach(x -> System.out.println(x));
        } catch (EmptyResultDataAccessException | NotFoundException e) {
            return false;
        }
        return !appointments.isEmpty();
    }


    @CrossOrigin
    @DeleteMapping("/api/appointment/{id}")
    public ResponseEntity<?> deleteAppointmentByDonor(@PathVariable Integer id) {
        try {
            appointmentService.getAppointmentById(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        appointmentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @RequestMapping(value = "/api/mail/{emailAddress}", method = RequestMethod.PUT)
    public ResponseEntity<?> mail(@RequestBody String emailContent, @PathVariable String emailAddress) {
        try {
            mailService.sendEmail(emailContent, emailAddress);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
