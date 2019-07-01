package HospitalApp;

import HospitalApp.entities.Diagnose;
import HospitalApp.entities.Medicament;
import HospitalApp.entities.Patient;
import HospitalApp.entities.Visitation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;

public class HospitalApp {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("hospital_db");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Patient patient1 =
                new Patient("John",
                        "Dow",
                        "Downing St. 10",
                        "random@abv.bg",
                        LocalDate.of(1983,01,19),
                        true);
        Patient patient2 =
                new Patient("Paul",
                        "Verhoven",
                        "Downing St. 13",
                        "random2@abv.bg",
                        LocalDate.of(1983,01,19),
                        true);

        Diagnose migrena = new Diagnose("migrena");
        Diagnose headache = new Diagnose("headache");
        Diagnose miopia = new Diagnose("miopia");

        Medicament analgin = new Medicament("Analgin");
        Medicament aspirin = new Medicament("Aspirin");
        Medicament visin = new Medicament("Visin");

        Visitation visitation1 = new Visitation(LocalDate.now().minusDays(21), "FirstVisitation");
        Visitation visitation2 = new Visitation(LocalDate.now().minusDays(3), "SecondVisitation");
        Visitation visitation3 = new Visitation(LocalDate.now().minusDays(25), "Hello to my Doctor");
        Visitation visitation4 = new Visitation(LocalDate.now().minusDays(2), "Bye to my Doctor");

        patient1.getVisitations().add(visitation1);
        patient1.getVisitations().add(visitation2);
        patient1.getDiagnoses().add(migrena);
        patient1.getDiagnoses().add(miopia);
        patient1.getMedicaments().add(analgin);
        patient1.getMedicaments().add(aspirin);


        patient2.getVisitations().add(visitation3);
        patient2.getVisitations().add(visitation4);
        patient2.getDiagnoses().add(headache);
        patient2.getDiagnoses().add(miopia);
        patient2.getMedicaments().add(analgin);
        patient2.getMedicaments().add(visin);

        visitation1.setPatient(patient1);
        visitation2.setPatient(patient1);
        visitation3.setPatient(patient2);
        visitation4.setPatient(patient2);

        try {
            entityManager.getTransaction().begin();


            entityManager.persist(patient1);
            entityManager.persist(patient2);

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
