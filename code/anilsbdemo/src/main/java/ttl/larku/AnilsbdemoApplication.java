package ttl.larku;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ttl.larku.domain.Student;
import ttl.larku.service.StudentService;

@SpringBootApplication
//@Configuration
//@ComponentScan
//@ComponentScan({"ttl.larku", "org.ttl.anilsbdemo"})
//@EnableAutoConfiguration
public class AnilsbdemoApplication {

   public static void main(String[] args) {
      ApplicationContext context = SpringApplication.run(AnilsbdemoApplication.class, args);
   }

}

@Component
class AnilsRunner implements CommandLineRunner {

   @Autowired
   private StudentService studentService;

   @Override
   public void run(String... args) throws Exception {
      System.out.println("Here we go with Spring Boot");

      List<Student> students = studentService.getAllStudents();
      System.out.println("student: " + students.size());
      students.forEach(System.out::println);
   }
}

//@Component
//class TrackRunner implements CommandLineRunner {
//
//   @Autowired
//   private TrackService trackService;
//
//   @Override
//   public void run(String... args) throws Exception {
//      System.out.println("Here we go with Spring Boot");
//
//      List<Student> tracks = trackService.getAllTracks();
//      System.out.println("student: " + tracks.size());
//      tracks.forEach(System.out::println);
//   }
//}
