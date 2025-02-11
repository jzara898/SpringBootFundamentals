package ttl.larku.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Course;

@Service
public class CourseService {

    @Autowired
    private BaseDAO<Course> courseDAO;

    public CourseService() {
        int stop = 0;
    }

    public Course createCourse(String code, String title) {
        Course course = new Course(code, title);
        course = courseDAO.insert(course);

        return course;
    }

    public Course createCourse(Course course) {
        course = courseDAO.insert(course);

        return course;
    }

    public boolean deleteCourse(int id) {
        return courseDAO.delete(id);
    }

    public boolean updateCourse(Course course) {
        return courseDAO.update(course);
    }

    public Course getCourseByCode(String code) {
        List<Course> courses = courseDAO.findAll();
        for (Course course : courses) {
            if (course.getCode().equals(code)) {
                return course;
            }
        }
        return null;
    }

    public Optional<Course> getCourse(int id) {
        return courseDAO.findById(id);
    }

    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    public BaseDAO<Course> getCourseDAO() {
        return courseDAO;
    }

    public void setCourseDAO(BaseDAO<Course> courseDAO) {
        this.courseDAO = courseDAO;
    }
}
