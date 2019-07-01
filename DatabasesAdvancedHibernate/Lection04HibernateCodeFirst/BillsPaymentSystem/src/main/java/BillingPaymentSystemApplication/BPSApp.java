package BillingPaymentSystemApplication;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class BPSApp {
    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("BPS_db");
        EntityManager entityManager = entityManagerFactory.createEntityManager();



    }
}
