package bg.softuni.bookshop.services;

import java.io.IOException;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;
    List<String> findAllTiles();
    List<String> findAllAuthors();
    List<String> getBooksTitleReleaseDateAndCopiesByAuthorNames(String firstName, String lastName);

}
