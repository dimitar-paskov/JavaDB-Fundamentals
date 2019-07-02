package bg.softuni.bookshop.services.impl;

import bg.softuni.bookshop.entities.Author;
import bg.softuni.bookshop.repositories.AuthorRepository;
import bg.softuni.bookshop.services.AuthorService;
import bg.softuni.bookshop.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService{


    private final static String AUTHOR_FILE_PATH = "src/main/resources/files/authors.txt";

    private final AuthorRepository authorRepository;
    private final FileUtil fileUtil;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, FileUtil fileUtil) {
        this.authorRepository = authorRepository;
        this.fileUtil = fileUtil;
    }


    @Override
    public void seedAuthors() throws IOException {

        if (this.authorRepository.count()!=0){
            return;
        }

        String[] authors = this.fileUtil.fileContent(AUTHOR_FILE_PATH);

        for (String s :
                authors) {
            String[] params = s.split("\\s+");
            Author author = new Author();
            author.setFirstName(params[0]);
            author.setLastName(params[1]);

            this.authorRepository.saveAndFlush(author);
        }



    }

    @Override
    public List<String> getAuthorsByPublishedBooksCount() {
        return this.authorRepository
                .findAuthorsByOrderOfPublishedBooksDesc()
                .stream()
                .map(author -> String.format("%s %s - %d books",
                        author.getFirstName(), author.getLastName(), author.getBooks().size()))
                .collect(Collectors.toList());
    }
}
