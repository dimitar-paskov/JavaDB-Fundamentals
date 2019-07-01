package BillingPaymentSystemApplication.entities;

import javax.annotation.Generated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {



    private Integer id;


    public BaseEntity() {

    }

    // UUID generator
    //    @Id
    //    @GeneratedValue(generator = "UUID")
    //    @GenericGenerator(name = "UUID",
    //    strategy = "org.hibernate.id.UUIDGenerator")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
