package ttl.larku.dao;

import java.util.ResourceBundle;
import ttl.larku.dao.inmemory.InMemoryStudentDAO;
import ttl.larku.dao.jpa.JPAStudentDAO;
import ttl.larku.service.StudentService;

public class DAOFactory {

   public static StudentDAO studentDAO() {
      var bundle = ResourceBundle.getBundle("larkUContext");
      String profile = bundle.getString("larku.profile.active");

      switch (profile) {
         case "dev":
            return new InMemoryStudentDAO();
         case "prod":
            return new JPAStudentDAO();
         default:
            throw new RuntimeException("Unknown profile: " + profile);
      }
   }

   public static StudentService studentService() {
      StudentService studentService = new StudentService();

      return studentService;
   }
}
