package bookshopsystemapp.service;

import bookshopsystemapp.domain.entities.*;
import bookshopsystemapp.exceptions.BookNotFoundException;
import bookshopsystemapp.repository.AuthorRepository;
import bookshopsystemapp.repository.BookRepository;
import bookshopsystemapp.repository.CategoryRepository;
import bookshopsystemapp.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final static String BOOKS_FILE_PATH =
            "src" + File.separator +
                    "main" + File.separator +
                    "resources" + File.separator +
                    "files" + File.separator +
                    "books.txt";

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final FileUtil fileUtil;

    @Autowired
    public BookServiceImpl(
            BookRepository bookRepository,
            AuthorRepository authorRepository,
            CategoryRepository categoryRepository,
            FileUtil fileUtil) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedBooks() throws IOException {
        if (this.bookRepository.count() != 0) {
            return;
        }

        String[] booksFileContent = this.fileUtil.getFileContent(BOOKS_FILE_PATH);
        for (String line : booksFileContent) {
            String[] lineParams = line.split("\\s+");

            Book book = new Book();
            book.setAuthor(this.getRandomAuthor());

            EditionType editionType = EditionType.values()[Integer.parseInt(lineParams[0])];
            book.setEditionType(editionType);

            LocalDate releaseDate = LocalDate.parse(lineParams[1], DateTimeFormatter.ofPattern("d/M/yyyy"));
            book.setReleaseDate(releaseDate);

            int copies = Integer.parseInt(lineParams[2]);
            book.setCopies(copies);

            BigDecimal price = new BigDecimal(lineParams[3]);
            book.setPrice(price);

            AgeRestriction ageRestriction = AgeRestriction.values()[Integer.parseInt(lineParams[4])];
            book.setAgeRestriction(ageRestriction);

            StringBuilder title = new StringBuilder();
            for (int i = 5; i < lineParams.length; i++) {
                title.append(lineParams[i]).append(" ");
            }

            book.setTitle(title.toString().trim());

            Set<Category> categories = this.getRandomCategories();
            book.setCategories(categories);

            this.bookRepository.saveAndFlush(book);
        }
    }

    @Override
    public List<String> getAllBooksTitlesAfter() {
        return this.bookRepository
                .findAllByReleaseDateAfter(LocalDate.parse("2000-12-31"))
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllBooksTitlesAfterDate(LocalDate date) {
        return this.bookRepository
                .findAllByReleaseDateAfter(date)
                .stream()
                .map(b-> String.format("%s %d", b.getTitle(), b.getCopies()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllTitlesWithAgeRestriction(AgeRestriction ageRestriction) {

        return this.bookRepository
                .findAllByAgeRestriction(ageRestriction)
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());

    }

    @Override
    public List<String> getAllTitlesWithCopiesLessThan(Integer number) {
        return this.bookRepository
                .findAllByCopiesLessThan(number)
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());

    }

    @Override
    public List<String> getAllTitlesWithPriceLessOrHigher(BigDecimal lowLimit, BigDecimal highLimit) {
        return this.bookRepository
                .findAllByPriceLessThanOrPriceGreaterThan(lowLimit, highLimit)
                .stream()
                .map(b -> String.format("%s - %.2f ", b.getTitle(), b.getPrice()))
                .collect(Collectors.toList());

    }

    @Override
    public List<String> getAllTitlesNotReleasedInYear(LocalDate before, LocalDate after) {

        return this.bookRepository
                .findAllByReleaseDateBeforeOrReleaseDateAfter(before, after)
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllTitlesReleasedBefore(LocalDate date) {
        return this.bookRepository
                .findAllByReleaseDateBefore(date)
                .stream()
                .map(b -> String.format("%s -> %s -> %s", b.getTitle(), b.getEditionType(), b.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllTitlesContaining(String pattern) {
        return this.bookRepository
                .findAllByTitleContaining(pattern)
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllTitlesWithAuthorLastNameStartingWith(String pattern) {
        return this.bookRepository
                .findAllByAuthorLastName(pattern)
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }

    @Override
    public Integer getAllTitlesWithLengthGraterThan(Integer letterCount) {
        return this.bookRepository
                .findAllTitleLengh(letterCount)
                .size();
    }

    @Override
    public Set<String> getAllAuthorsWithBookBefore() {
        return this.bookRepository
                .findAllByReleaseDateBefore(LocalDate.parse("1990-01-01"))
                .stream()
                .map(b -> String.format("%s %s", b.getAuthor().getFirstName(), b.getAuthor().getLastName()))
                .collect(Collectors.toSet());
    }

    @Override
    public List<String> getAuthorsAndBookCopiesCount() {
        return this.bookRepository
                .getAuthorsAndBooksCopies()
                .stream()
                .map(obj -> (String) obj)
                .collect(Collectors.toList());
    }

    @Override
    public ReducedBook getBookReducedInfoByTitle(String title) throws BookNotFoundException {

        ReducedBook reducedBook = null;

        reducedBook = this.bookRepository.getBookReducedInfo(title);

        if (reducedBook == null) {

            throw new BookNotFoundException("Book Not Found");
        }

        return reducedBook;

    }

    @Override
    public Integer increaseCopiesBy(LocalDate date, Integer number) {
        return this.bookRepository.increaseBookCopiesForBooksAfter(date, number);
    }

    @Override
    public Integer deleteBooksByCopiesLessThan(Integer number) {
//        int count = this.bookRepository.prepareDeleteBookByCopiesLessThan(number);
//        System.out.println(count);
        return this.bookRepository.deleteBookByCopiesIsLessThanEqual(number);
    }

    @Override
    public Integer getNumberOfBooksByAuthorByStoredProcedure(String fname, String lname) {
        return this.bookRepository.getNumberOfBooksFromAuthor(fname, lname);
    }


    private Author getRandomAuthor() {
        Random random = new Random();

        int randomId = random.nextInt((int) (this.authorRepository.count() - 1)) + 1;

        return this.authorRepository.findById(randomId).orElse(null);
    }

    private Set<Category> getRandomCategories() {
        Set<Category> categories = new LinkedHashSet<>();

        Random random = new Random();
        int length = random.nextInt(5);

        for (int i = 0; i < length; i++) {
            Category category = this.getRandomCategory();

            categories.add(category);
        }

        return categories;
    }

    private Category getRandomCategory() {
        Random random = new Random();

        int randomId = random.nextInt((int) (this.categoryRepository.count() - 1)) + 1;

        return this.categoryRepository.findById(randomId).orElse(null);
    }
}
