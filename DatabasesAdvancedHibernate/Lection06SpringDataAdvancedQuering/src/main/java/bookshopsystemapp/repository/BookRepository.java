package bookshopsystemapp.repository;

import bookshopsystemapp.domain.entities.AgeRestriction;
import bookshopsystemapp.domain.entities.Book;
import bookshopsystemapp.domain.entities.ReducedBook;
import bookshopsystemapp.domain.entities.ReducedBookImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {


    //zad1
    List<Book> findAllByAgeRestriction(AgeRestriction ageRestriction);


    //zad2
    List<Book> findAllByCopiesLessThan(Integer number);


    //zad3
    List<Book> findAllByPriceLessThanOrPriceGreaterThan(BigDecimal lowLimit, BigDecimal hghLimit);


    //zad4
    List<Book> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate before, LocalDate after);


    //zad5
    List<Book> findAllByReleaseDateBefore(LocalDate date);


    //zad7
    List<Book> findAllByTitleContaining(String pattern);


    //zad8
    @Query(value = "SELECT b FROM Book b JOIN b.author a WHERE a.lastName LIKE :pattern%")
    List<Book> findAllByAuthorLastName(@Param(value = "pattern")String pattern);


    //zad9
    @Query(value = "SELECT b FROM Book b WHERE LENGTH(b.title) > :letterCount")
    List<Book> findAllTitleLengh(@Param(value = "letterCount")Integer letterCount);


    //zad10
    @Query(value = "SELECT CONCAT(a.firstName, ' ', a.lastName, ' - ', SUM(b.copies)) " +
            "FROM Book AS b " +
            "JOIN b.author AS a " +
            "GROUP BY a.id " +
            "ORDER BY SUM(b.copies) DESC ")
    List<Object> getAuthorsAndBooksCopies();


    //zad11
    @Query(value = "SELECT new bookshopsystemapp.domain.entities.ReducedBookImpl(b.title, b.editionType, b.ageRestriction, b.price) " +
            "FROM Book AS b " +
            "WHERE b.title = :title")
    ReducedBook getBookReducedInfo(@Param(value = "title") String title);


    //zad12
    List<Book> findAllByReleaseDateAfter(LocalDate date);
    @Modifying
    @Query(value = "UPDATE Book b " +
            "SET b.copies = (b.copies + :numberOfCopies) " +
            "WHERE b.releaseDate > :date")
    Integer increaseBookCopiesForBooksAfter(
            @Param(value = "date") LocalDate date,
            @Param(value = "numberOfCopies") Integer numberOfCopies);


    //zad13
    @Modifying
    Integer deleteBookByCopiesIsLessThanEqual(Integer number);

    //zad14
    @Procedure(value = "udp_get_number_of_books_by_author")
    Integer getNumberOfBooksFromAuthor(
            @Param("fname") String fname,
            @Param("lname") String lname);




}
