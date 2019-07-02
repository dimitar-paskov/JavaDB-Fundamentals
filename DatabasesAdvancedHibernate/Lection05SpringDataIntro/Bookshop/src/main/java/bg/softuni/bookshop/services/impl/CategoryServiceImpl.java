package bg.softuni.bookshop.services.impl;

import bg.softuni.bookshop.entities.Category;
import bg.softuni.bookshop.repositories.CategoryRepository;
import bg.softuni.bookshop.services.CategoryService;
import bg.softuni.bookshop.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final static String CATEGORY_FILE_PATH = "src/main/resources/files/categories.txt";


    private final CategoryRepository categoryRepository;
    private final FileUtil fileUtil;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, FileUtil fileUtil) {
        this.categoryRepository = categoryRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedCategory() throws IOException {
        if (this.categoryRepository.count() != 0){
            return;
        }


        String[] categories = this.fileUtil.fileContent(CATEGORY_FILE_PATH);

        for (String info :
                categories) {
            Category category = new Category();
            category.setName(info);

            this.categoryRepository.saveAndFlush(category);

        }

    }
}
