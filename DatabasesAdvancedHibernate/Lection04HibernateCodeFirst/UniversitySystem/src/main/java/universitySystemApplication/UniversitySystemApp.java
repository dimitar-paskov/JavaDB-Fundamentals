package universitySystemApplication;

import universitySystemApplication.entities.Course;
import universitySystemApplication.entities.Student;
import universitySystemApplication.entities.Teacher;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.HashSet;

public class UniversitySystemApp {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("university_system_db");
        EntityManager entityManager =
                entityManagerFactory.createEntityManager();


        Teacher teacher =
                new Teacher("Pesho", "Peshev", "123456789","random@abv.bg", 48.58);

        Student student =
                new Student("Gosho", "Goshev", "123456987", 5.95, 45);


        Course course = new Course();
        course.setName("Java");
        course.setDescription("JavaHibernate");
        course.setStartDate(LocalDate.now());
        course.setTeacher(teacher);
        course.getStudents().add(student);


        try {
            entityManager.getTransaction().begin();

//            entityManager.persist(teacher);
//            entityManager.persist(student);
            entityManager.persist(course);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

    }
}
