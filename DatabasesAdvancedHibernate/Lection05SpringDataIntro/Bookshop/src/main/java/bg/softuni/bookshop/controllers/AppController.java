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

        //zad1
        this.bookService
                .findAllTiles()
                .forEach(System.out::println);

        //zad2
        this.bookService
                .findAllAuthors()
                .forEach(System.out::println);

        //zad3
        this.authorService
                .getAuthorsByPublishedBooksCount()
                .forEach(System.out::println);

        //zad4
        this.bookService
                .getBooksTitleReleaseDateAndCopiesByAuthorNames("George", "Powell")
                .forEach(System.out::println);

    }


}
