package ttl.larku.app;

import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ttl.larku.domain.Student;
import ttl.larku.service.StudentService;

public class SpringDemo {

   public static void main(String[] args) {
      SpringDemo demo = new SpringDemo();
      demo.go();
   }

   public void go() {

      ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

      StudentService studentService = context.getBean("studentService", StudentService.class);

      List<Student> students = studentService.getAllStudents();
      System.out.println("student.size: " + students.size());
      for (Student student : students) {
         System.out.println(student);
      }

   }
}
