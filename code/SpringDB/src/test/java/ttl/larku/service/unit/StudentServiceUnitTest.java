package ttl.larku.service.unit;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationEventPublisher;
import ttl.larku.dao.jpahibernate.JPAStudentDAO;
import ttl.larku.domain.Student;
import ttl.larku.domain.Student.Status;
import ttl.larku.service.StudentDaoService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;


/**
 * A straight ahead Unit test.  Only Mockito, no Spring
 */
//@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
//Can use @MockitoSettings to turn on LENIENT mode.
//Then Mockito won't get upset with unused Mocks.
//Probably better to leave it off and get rid of
//unused mocks.
@MockitoSettings(strictness = Strictness.LENIENT)
@Tag("unit")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentServiceUnitTest {

   private String name1 = "Bloke";
   private String name2 = "Blokess";
   private String newName = "Karl Jung";
   private String phoneNumber1 = "290 298 4790";
   private String phoneNumber2 = "3838 939 93939";
   private LocalDate dob1 = LocalDate.of(1988, 10, 7);
   private LocalDate dob2 = LocalDate.of(2010, 10, 7);

   @InjectMocks
   private StudentDaoService studentService;

   @Mock
   private JPAStudentDAO studentDAO;

   @Mock
   private List<Student> mockList;

   @Mock
   private ApplicationEventPublisher publisher;

   private static Instant start;
   private static Instant staticStart;

   @BeforeAll
   public static void beforeAll() {
      staticStart = Instant.now();
   }

   //
   @AfterAll
   public static void afterAll() {
      System.out.println("Test took:" + start.until(Instant.now(), ChronoUnit.MILLIS) + " ms");
   }

   boolean first = true;

   @BeforeEach
   public void setup() {
      if (first) {
         start = Instant.now();
         System.out.println("Preamble took:" + staticStart.until(Instant.now(), ChronoUnit.MILLIS) + " ms");
         first = false;
      }
      studentService.clear();
   }

   @Test
   public void testCreateStudent() {
      Student s = new Student(name1, phoneNumber1, dob1, Status.FULL_TIME);

      Mockito.when(studentDAO.insert(s)).thenReturn(s);
      Mockito.doNothing().when(publisher).publishEvent(any());

      Student newStudent = studentService.createStudent(name1, phoneNumber1, dob1, Status.FULL_TIME);

      Mockito.verify(studentDAO, atMost(1)).insert(s);
      Mockito.verify(publisher, atMostOnce()).publishEvent(any());
   }

   @Test
   public void testDeleteStudent() {
      Student student1 = new Student(name1, phoneNumber1, dob1, Status.FULL_TIME);
      student1.setId(1);

      //Set up Mocks
      Mockito.when(studentDAO.findById(1)).thenReturn(student1);
      Mockito.when(studentDAO.delete(student1)).thenReturn(true);

      //Call and JUnit asserts
      boolean result = studentService.deleteStudent(student1.getId());
      assertTrue(result);

      //Mockito verification
      Mockito.verify(studentDAO).findById(1);
      Mockito.verify(studentDAO, atLeastOnce()).delete(student1);
   }

   @Test
   public void testDeleteNonExistentStudent() {

      Mockito.when(studentDAO.findById(9999)).thenReturn(null);
      //Non existent Id
      boolean result = studentService.deleteStudent(9999);
      assertFalse(result);

      Mockito.verify(studentDAO).findById(9999);
      Mockito.verify(studentDAO, never()).delete(any());
   }

   @Test
   public void testUpdateStudent() {
      Student student1 = new Student(name1, phoneNumber1, dob1, Status.FULL_TIME);
      student1.setId(1);

      //Set up Mocks
      Mockito.when(studentDAO.findById(1)).thenReturn(student1);
      Mockito.when(studentDAO.update(student1)).thenReturn(true);

      //Call and Junit assertions
      boolean result = studentService.updateStudent(student1);
      assertTrue(result);

      //Mockito Verification
      Mockito.verify(studentDAO).findById(1);
      Mockito.verify(studentDAO, atMostOnce()).update(student1);
   }

   @Test
   public void testUpdateNonExistentStudent() {
      Student student1 = new Student(name1, phoneNumber1, dob1, Status.FULL_TIME);
      student1.setId(9999);

      Mockito.when(studentDAO.findById(student1.getId())).thenReturn(null);

      boolean result = studentService.updateStudent(student1);
      assertFalse(result);

      Mockito.verify(studentDAO).findById(9999);
      Mockito.verify(studentDAO, never()).update(student1);
   }

   @Test
   public void testGetByName() {
      Mockito.when(studentDAO.findBy(any())).thenReturn(mockList);

      List<Student> johnnies = studentService.getByName("Johny");

      Mockito.verify(studentDAO).getByName(any());
   }
}
