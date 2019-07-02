package bg.softuni.bookshop.controllers;

import bg.softuni.bookshop.services.AuthorService;
import bg.softuni.bookshop.services.BookService;
import bg.softuni.bookshop.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import javax.xml.bind.SchemaOutputResolver;
import java.util.List;

@Controller
public class AppController implements CommandLineRunner {

    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final BookService bookService;

    @Autowired
    public AppController(AuthorService authorService, CategoryService categoryService, BookService bookService) {
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
        this.authorService.seedAuthors();
        this.categoryService.seedCategory();
        this.bookService.seedBooks();

//        allBooks();
        // Comment added
        allAuthors();

    }

    private void allBooks() {
        List<String> titles = this.bookService.findAllTiles();
        titles.forEach(System.out::println);
    }

    private void allAuthors(){
        List<String> authors = this.bookService.findAllAuthors();
        authors.forEach(System.out::println);

    }
}
