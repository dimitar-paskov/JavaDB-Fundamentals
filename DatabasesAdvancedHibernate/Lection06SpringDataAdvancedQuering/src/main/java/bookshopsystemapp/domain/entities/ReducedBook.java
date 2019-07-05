package bookshopsystemapp.domain.entities;

import java.math.BigDecimal;

public interface ReducedBook {


    String getTitle();
    EditionType getEditionType();
    AgeRestriction getAgeRestriction();
    BigDecimal getPrice();
    void setTitle(String title);
    void setEditionType(EditionType editionType);
    void setAgeRestriction(AgeRestriction ageRestriction);;
    void setPrice(BigDecimal price);
}
