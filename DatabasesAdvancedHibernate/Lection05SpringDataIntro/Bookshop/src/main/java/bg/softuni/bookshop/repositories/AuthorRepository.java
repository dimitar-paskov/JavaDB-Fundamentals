package bg.softuni.bookshop.repositories;

import bg.softuni.bookshop.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @Query(value =
            "SELECT a.id, a.first_name, a.last_name\n" +
                    "FROM authors a\n" +
                    "JOIN books b\n" +
                    "ON a.id = b.author_id\n" +
                    "GROUP BY a.id\n" +
                    "ORDER BY count(*) DESC;", nativeQuery = true)
    List<Author> findAuthorsByOrderOfPublishedBooksDesc();



}
