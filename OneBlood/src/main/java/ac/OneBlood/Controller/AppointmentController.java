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

import javax.mail.MessagingException;
import java.io.IOException;
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
    //intoarce cartea in functie de id, impreuna cu legaturile catre self, parinte si autori
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
            appointments = appointmentService.getAppointmentByDoctorCode(doctor_code);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }


    @CrossOrigin
    @RequestMapping(value = "/api/appointment/doctor/{doctor_code}/date/{timestamp}", method = RequestMethod.GET)
    public List<Appointment> getAppointmentByDoctorCodeAfterDate(@PathVariable Integer doctor_code, @PathVariable Long timestamp) {
        List<Appointment> appointments;
        try {
            appointments = appointmentService.getAppointmentByDoctorCode(doctor_code)
                    .stream()
                    .filter(appointment -> appointment.getAppointment_date().getTime() > timestamp)
                    .collect(Collectors.toList());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return appointments;
    }

//
//    //toate programarile unui anumit doctor dintr-o anumita zi (data ca timestamp)
//    @RequestMapping(value = "/api/appointment/doctor/{doctor_code}/day/{timestamp}", method = RequestMethod.GET)
//    public ResponseEntity<?> getAppointmentByDoctorCodeAfterDate2(@PathVariable Integer doctor_code, @PathVariable Long timestamp) throws ParseException {
//        List<Appointment> appointments = null;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//        Date date = new Date(timestamp * 1000);
//        // Date date = sdf.parse(day);
//        String formattedDate = sdf.format(date);
//
////        how to get hour from timestamp
//        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm");
//        String formattedDate2 = sdf2.format(date);
//        System.out.println(formattedDate2);
//        Map<String, Integer> appointmentsAvailabe = appointmentService.initializeAppoinmentsForToday();
//        try {
//            //toate programarile doctorului din acea zi
//            appointments = appointmentService.getAppointmentByDoctorCode(doctor_code)
//                    .stream()
//                    .filter(appointment -> sdf.format(appointment.getAppointment_date()).equals(formattedDate))
//                    .collect(Collectors.toList());
//
//            appointments.stream().map(appointment -> {
//                String key = sdf2.format(appointment.getAppointment_date());
////                if (appointmentsAvailabe.containsKey(key))
//                    appointmentsAvailabe.put(key, appointmentsAvailabe.get(key) + 1);
//                return appointmentsAvailabe;
//            }).collect(Collectors.toList());
//
//            System.out.println(appointmentsAvailabe.values());
//
//        } catch (EmptyResultDataAccessException e) {
//            return new ResponseEntity<>(appointments, HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>(appointments, HttpStatus.OK);
//    }
//


    @CrossOrigin
    //toate programarile unui anumit doctor dintr-o anumita zi, la o anumita ora (data ca timestamp)
    @RequestMapping(value = "/api/appointment/doctor/{doctor_code}/day/{dateIn}/hour/{hour}", method = RequestMethod.GET)
    public ResponseEntity<?> getAppointmentByDoctorPerHour(@PathVariable Integer doctor_code, @PathVariable String dateIn, @PathVariable String hour) throws ParseException {
        List<Appointment> appointments = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date appointmentDate = sdf.parse(dateIn);
//        Date date = new Date(timestamp * 1000);
        //Date date = new Date(dateIn);
        //String appointmentDate = sdf.format(date);
//        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm:ss");
//        String appointmentHour = sdf2.format(date);

        System.out.println(dateIn);
        System.out.println(hour);
        try {
            //toate programarile doctorului din acea zi, la acea ora
            appointments = appointmentService.getAppointmentByDoctorCode(doctor_code).stream()
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
        //verifici daca exista, daca nu exista il creezi => 201 created
        //daca exista ii faci update 200ok
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
        //verifici daca exista, daca nu exista il creezi => 201 created
        //daca exista ii faci update 200ok
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
            appointments.forEach(x-> System.out.println(x));
        } catch (EmptyResultDataAccessException | NotFoundException e) {
            return false;
        }
        if (appointments.isEmpty())
            return false;
        else return true;
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
        } catch (Exception e ) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
