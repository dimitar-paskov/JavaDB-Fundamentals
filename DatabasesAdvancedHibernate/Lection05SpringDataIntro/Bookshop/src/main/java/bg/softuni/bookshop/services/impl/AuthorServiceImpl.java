package bg.softuni.bookshop.services.impl;

import bg.softuni.bookshop.entities.Author;
import bg.softuni.bookshop.repositories.AuthorRepository;
import bg.softuni.bookshop.services.AuthorService;
import bg.softuni.bookshop.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthorServiceImpl implements AuthorService{


    private final static String AUTHOR_FILE_PATH = "/media/D/Programming/SoftUni/8.Java DB Fundamentals/DatabasesAdvancedHibernate/Lection05SpringDataIntro/Bookshop/src/main/resources/files/authors.txt";

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
}
