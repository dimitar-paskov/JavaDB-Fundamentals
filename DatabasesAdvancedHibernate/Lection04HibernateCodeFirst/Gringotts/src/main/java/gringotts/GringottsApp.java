package gringotts;

import gringotts.entity.WizardDeposit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;


public class GringottsApp {
    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("gringotts_db");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            WizardDeposit deposit = new WizardDeposit();
            deposit.setFirstName("Ivan");
            deposit.setLastName("Ivanov");
            // Should throw javax.validation.ConstraintViolationException interpolatedMessage='must be greater than or equal to 0'
            deposit.setAge(-4);
            deposit.setDepositStartDate(LocalDateTime.now());


            entityManager.getTransaction().begin();

            entityManager.persist(deposit);

            entityManager.getTransaction().commit();


        } catch (ConstraintViolationException e) {
//            e.printStackTrace();
            System.out.println("Age shoud be greater than or equal to 0");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            entityManager.close();
            entityManagerFactory.close();
        }


    }
}
