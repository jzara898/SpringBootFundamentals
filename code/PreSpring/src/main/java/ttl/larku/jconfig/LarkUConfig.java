package ttl.larku.jconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import ttl.larku.dao.BaseDAO;
import ttl.larku.dao.inmemory.InMemoryStudentDAO;
import ttl.larku.domain.Student;
import ttl.larku.service.StudentService;

@Configuration
@ComponentScan({"ttl.larku"})
public class LarkUConfig {

   /*
    <bean id="inMemoryDAO" class="ttl.larku.dao.inmemory.InMemoryStudentDAO"/>
   */

   @Bean
   public BaseDAO<Student> studentDAO() {
      var dao = new InMemoryStudentDAO();
      return dao;
   }

   /*
    <bean id="studentService" class="ttl.larku.service.StudentService" >
        <property name="studentDAO" ref="inMemoryDAO"/>
    </bean>
    */

   @Bean
   public StudentService studentService() {
      StudentService studentService = new StudentService();

      var dao = studentDAO();

      studentService.setStudentDAO(dao);

      return studentService;
   }
}
