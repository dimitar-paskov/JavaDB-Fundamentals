package bookshopsystemapp.controller;

import bookshopsystemapp.exceptions.BookServiceException;
import bookshopsystemapp.service.AuthorService;
import bookshopsystemapp.service.BookService;
import bookshopsystemapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Controller
public class BookshopController implements CommandLineRunner {

    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final BookService bookService;

    private final Scanner scanner;

    @Autowired
    public BookshopController(AuthorService authorService, CategoryService categoryService, BookService bookService) {
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.bookService = bookService;
        this.scanner = new Scanner(System.in);


    }

    @Override
    public void run(String... strings) throws Exception {
        this.authorService.seedAuthors();
        this.categoryService.seedCategories();
        this.bookService.seedBooks();

        //zad1
//        System.out.println("Enter age restriction type:");
//        AgeRestriction ageRestriction = AgeRestriction.valueOf(scanner.nextLine().toUpperCase());
//        this.bookService
//                .getAllTitlesWithAgeRestriction(ageRestriction)
//                .forEach(System.out::println);
        //zad2
//        this.bookService
//                .getAllTitlesWithCopiesLessThan(500)
//                .forEach(System.out::println);
        //zad3
//        this.bookService
//                .getAllTitlesWithPriceLessOrHigher(BigDecimal.valueOf(5),BigDecimal.valueOf(40) )
//                .forEach(System.out::println);
        //zad4
//        System.out.println("Enter year:");
//        Integer year = Integer.parseInt(scanner.nextLine());
//        LocalDate before = LocalDate.parse(year+"-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        LocalDate after = LocalDate.parse(year+"-12-31", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        this.bookService
//                .getAllTitlesNotReleasedInYear(before, after)
//                .forEach(System.out::println);
        //zad5
//        System.out.println("Enter date:");
//        LocalDate date = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
//        this.bookService
//                .getAllTitlesReleasedBefore(date)
//                .forEach(System.out::println);
        //zad7
//        System.out.println("Enter ending pattern:");
//        String pattern = scanner.nextLine();
//        this.authorService
//                .getAuthorsByFirstNameEndsOn(pattern)
//                .forEach(System.out::println);
        //zad8
//        System.out.println("Enter pattern:");
//        String pattern = scanner.nextLine();
//        this.bookService
//                .getAllTitlesWithAuthorLastNameStartingWith(pattern)
//                .forEach(System.out::println);
        //zad9
//        System.out.println("Enter letterCount:");
//        Integer letterCount = Integer.parseInt(scanner.nextLine());
//        System.out.println(this.bookService
//                .getAllTitlesWithLengthGraterThan(letterCount));
        //zad10
//        this.bookService
//                .getAuthorsAndBookCopiesCount()
//                .forEach(System.out::println);


        //zad11
//        System.out.println("Enter title:");
//        String title = scanner.nextLine();
//        try {
//            System.out.println(this.bookService
//                    .getBookReducedInfoByTitle(title));
//        }catch (BookServiceException e){
//            System.out.println(e.getMessage());
//        }


        //zad12
//        System.out.println("Enter date:");
//        LocalDate date = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd MMM yyyy"));
//        this.bookService
//                .getAllBooksTitlesAfterDate(date)
//                .forEach(System.out::println);
//        System.out.println("Enter the number of copies to increase by:");
//        Integer number = Integer.parseInt(scanner.nextLine());
//        Integer count =
//                this.bookService
//                .increaseCopiesBy(date, number);
//        this.bookService
//                .getAllBooksTitlesAfterDate(date)
//                .forEach(System.out::println);
//        System.out.printf("Total copies added: %d%n", number*count);

        //zad13
//        System.out.println("To delete books enter the number of copies:");
//        Integer number = Integer.parseInt(scanner.nextLine());
//
//        this.bookService
//                .getAllTitlesWithCopiesLessThan(number)
//                .forEach(System.out::println);
//
//        Integer deletedBooksCount =
//                this.bookService
//                        .deleteBooksByCopiesLessThan(number);
//
//        this.bookService
//                .getAllTitlesWithCopiesLessThan(number)
//                .forEach(System.out::println);
//
//        System.out.println("Number of books deleted: " + deletedBooksCount);

        //zad14
        System.out.println("Enter full name:");
        String fullName = scanner.nextLine();
        String firstName = fullName.split("\\s+")[0];
        String lastName = fullName.split("\\s+")[1];
        System.out.printf("%s has written %d books%n",
                fullName,
                this.bookService.getNumberOfBooksByAuthorByStoredProcedure(firstName, lastName));


    }
}
