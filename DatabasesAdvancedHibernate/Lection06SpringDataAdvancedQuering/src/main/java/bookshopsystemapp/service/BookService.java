package bookshopsystemapp.service;

import bookshopsystemapp.domain.entities.AgeRestriction;
import bookshopsystemapp.domain.entities.ReducedBook;
import bookshopsystemapp.exceptions.BookNotFoundException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BookService {

    void seedBooks() throws IOException;
//    void uploadProcedureInDb();

    List<String> getAllBooksTitlesAfter();
    List<String> getAllBooksTitlesAfterDate(LocalDate date);
    Set<String> getAllAuthorsWithBookBefore();

    List<String> getAllTitlesWithAgeRestriction(AgeRestriction ageRestriction);
    List<String> getAllTitlesWithCopiesLessThan(Integer number);
    List<String> getAllTitlesWithPriceLessOrHigher(BigDecimal lowLimit, BigDecimal highLimit);
    List<String> getAllTitlesNotReleasedInYear(LocalDate before, LocalDate after);
    List<String> getAllTitlesReleasedBefore(LocalDate date);
    List<String> getAllTitlesContaining(String pattern);
    List<String> getAllTitlesWithAuthorLastNameStartingWith(String pattern);
    Integer getAllTitlesWithLengthGraterThan(Integer letterCount);
    List<String> getAuthorsAndBookCopiesCount();
    ReducedBook getBookReducedInfoByTitle(String title) throws BookNotFoundException;
    Integer increaseCopiesBy(LocalDate date, Integer number);
    Integer deleteBooksByCopiesLessThan(Integer number);
    Integer getNumberOfBooksByAuthorByStoredProcedure(String fname, String lname);
}
