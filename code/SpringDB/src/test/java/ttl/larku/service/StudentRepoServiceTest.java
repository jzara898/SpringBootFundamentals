package ttl.larku.service;

import jakarta.annotation.Resource;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;
import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;
import ttl.larku.sql.SqlScriptBase;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
//Populate your DB.  From Most Expensive to least expensive

//This will make recreate the context after every test.
//In conjunction with appropriate 'schema[-XXX].sql' and 'data[-XXX].sql' files
//it will also drop and recreate the DB before each test.
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)

//Or you can just re-run the sql files before each test method
//@Sql(scripts = { "/ttl/larku/db/createVersionedDB-h2.sql",
//        "/ttl/larku/db/populateVersionedDB-h2.sql" }, executionPhase=ExecutionPhase.BEFORE_TEST_METHOD)

//This next one will roll back the transaction after
//each test, so the database will actually stay the
//same for the next test.
@Transactional
@Tag("integration")
public class StudentRepoServiceTest extends SqlScriptBase {

    private String name1 = "Bloke";
    private String name2 = "Blokess";
    private String newName = "Different Bloke";

    private String code1 = "MATH-101";
    private String code2 = "BOT-202";
    private String badCode = "BADNESS";
    private String title1 = "Intro To Botany";
    private String title2 = "Outtro To Botany";

    private LocalDate startDate1 = LocalDate.parse("2012-10-10");
    private LocalDate startDate2 = LocalDate.parse("2013-10-10");
    private LocalDate endDate1 = LocalDate.parse("2013-05-10");
    private LocalDate endDate2 = LocalDate.parse("2014-05-10");

    private LocalDate badStartDate = LocalDate.parse("2099-12-12");

    private Course course1;
    private Course course2;
    private ScheduledClass class1;
    private ScheduledClass class2;
    private Student student1;
    private Student student2;

    @Autowired
    private StudentService studentService;

    private static Instant start;
    private static Instant staticStart;
    @BeforeAll
    public static void beforeAll() {
        staticStart = Instant.now();
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("Test took:" + start.until(Instant.now(), ChronoUnit.MILLIS) + " ms");
    }

    private boolean first = true;
    @BeforeEach
    public void setup() {
        if(first) {
            start = Instant.now();
            System.out.println("Preamble took:" + staticStart.until(Instant.now(), ChronoUnit.MILLIS) + " ms");
            first = false;
        }
        student1 = new Student(name1);
        student2 = new Student(name2);
        course1 = new Course(code1, title1);
        course2 = new Course(code2, title2);

        class1 = new ScheduledClass(course1, startDate1, endDate1);
        class2 = new ScheduledClass(course2, startDate2, endDate2);

    }

    @Test
//    @Transactional
    public void testGetAll() {
        List<Student> students = studentService.getAllStudents();

        students.forEach(System.out::println);



        //This will throw LIE
        //students.forEach(s -> System.out.println(s + ": class size" + s.getClasses().size()));


        assertEquals(4, students.size());
    }

    @Test
    public void testCreateStudent() {
        Student newStudent = studentService.createStudent(name1);

        Student result = studentService.getStudent(newStudent.getId());

        assertEquals(name1, result.getName());
        assertEquals(5, studentService.getAllStudents().size());
    }

    @Test
    public void testDeleteStudent() {
        Student student1 = studentService.createStudent(name1);
        Student student2 = studentService.createStudent(name2);

        assertEquals(6, studentService.getAllStudents().size());

        studentService.deleteStudent(student1.getId());

        assertEquals(5, studentService.getAllStudents().size());
    }

    @Test
    public void testDeleteNonExistentStudent() {
        Student student1 = studentService.createStudent(name1);
        Student student2 = studentService.createStudent(name2);

        assertEquals(6, studentService.getAllStudents().size());

        //Non existent Id
        studentService.deleteStudent(9999);

        assertEquals(6, studentService.getAllStudents().size());
    }

    @Test
    public void testUpdateStudent() {
        Student student1 = studentService.createStudent(name1);

//        assertEquals(5, studentService.getAllStudents().size());

        student1.setName(name2);
        studentService.updateStudent(student1);

        assertEquals(5, studentService.getAllStudents().size());
        assertEquals(name2, studentService.getStudent(student1.getId()).getName());
    }

    @Test
    public void testGetStudentNameAndClassSize() {
        String result = studentService.getStudentNameAndClassSize(1);

        System.out.println("result: " + result);
    }
}
