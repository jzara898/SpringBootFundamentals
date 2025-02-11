package ttl.larku.controllers.rest;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;
import ttl.larku.service.ClassService;
import ttl.larku.service.RegistrationService;

@RestController
@RequestMapping("/adminrest/class")
public class RegistrationRestController {
   private RegistrationService regService;
   private ClassService classService;
   private UriCreator uriCreator;

   @Autowired
   public RegistrationRestController(RegistrationService registrationService,
                                     ClassService classService, UriCreator uriCreator) {
      this.regService = registrationService;
      this.classService = classService;
      this.uriCreator = uriCreator;
   }

   @GetMapping
   public ResponseEntity<?> getAllClasses() {
      List<ScheduledClass> classes = classService.getAllScheduledClasses();
      return ResponseEntity.ok().body(RestResultWrapper.ofValue(classes));
   }

   @GetMapping("/{id}")
   public ResponseEntity<?> getScheduledClass(@PathVariable("id") int id) {
      return classService.getScheduledClass(id)
            .map(c -> ResponseEntity.ok(RestResultWrapper.ofValue(c)))
            .orElseGet(() -> {
               RestResultWrapper<ScheduledClass> rr = RestResultWrapper.ofError("ScheduledClass with id: " + id + " not found");
               return ResponseEntity.badRequest().body(rr);
            });
   }

   @GetMapping("/code/{courseCode}")
   public ResponseEntity<?> getScheduledClassPath(@PathVariable("courseCode") String courseCode) {
      List<ScheduledClass> cl = classService.getScheduledClassesByCourseCode(courseCode);
      if (cl == null || cl.isEmpty()) {
         RestResultWrapper<Void> r1 = RestResultWrapper.ofError("ScheduledClass with code: " +
               courseCode + " not found");
         return ResponseEntity.badRequest().body(r1);
      }


      return ResponseEntity.ok(RestResultWrapper.ofValue(cl));
   }


   @PostMapping
   public ResponseEntity<?> addClass(@RequestParam("courseCode") String courseCode,
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                     @RequestParam("startDate") LocalDate startDate,
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                     @RequestParam("endDate") LocalDate endDate) {

      ScheduledClass sc = regService.addNewClassToSchedule(courseCode, startDate, endDate);

      URI uri = uriCreator.getURI(sc.getId());

      return ResponseEntity.created(uri).body(RestResultWrapper.ofValue(sc));
   }

   @RequestMapping(value = "/register", method = RequestMethod.POST)
   public ResponseEntity<?> registerStudent(@RequestParam int studentId, @RequestParam int classId) {
//      var x = regService.getClassService().getScheduledClass(classId)
//            .map(sClass -> {
//               return regService.getStudentService().getStudent(studentId)
//                     .map(student -> {
//                        regService.registerStudentForClass(studentId, sClass.getCourse().getCode(), sClass.getStartDate());
//                        return ResponseEntity.ok(RestResultWrapper.ofValue(student));
//
//                     }).orElseGet(() -> {
//                        RestResultWrapper<Student> rr = RestResultWrapper.ofError("Cannot Register Student Id: "
//                              + studentId + " for Class Id: " + classId);
//                        return ResponseEntity.<Student>badRequest().body(rr);
//                     });
//            }).orElseGet(() -> {
//               RestResultWrapper<Student> rr = RestResultWrapper.ofError("Cannot Register Student Id: "
//                     + studentId + " for Class Id: " + classId);
//               return ResponseEntity.<Student>badRequest().body(rr);
//            });
//
//      return x;

      ScheduledClass sClass = regService.getClassService().getScheduledClass(classId).orElse(null);
      if (sClass != null) {
         Student student = regService.getStudentService().getStudent(studentId).orElse(null);
         if (student != null) {
            regService.registerStudentForClass(studentId, sClass.getCourse().getCode(), sClass.getStartDate());
            return ResponseEntity.ok(RestResultWrapper.ofValue(student));
         }
      }
      var rr = RestResultWrapper.ofError("Cannot Register Student Id: "
            + studentId + " for Class Id: " + classId);
      return ResponseEntity.badRequest().body(rr);
   }

   @RequestMapping(value = "/register/{studentId}/{classId}", method = RequestMethod.POST)
   public ResponseEntity<?> registerStudentWithPath(@PathVariable int studentId, @PathVariable int classId) {

      return registerStudent(studentId, classId);
   }
}
