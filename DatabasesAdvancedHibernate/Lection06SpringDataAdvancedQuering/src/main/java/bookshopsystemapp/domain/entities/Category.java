package bookshopsystemapp.domain.entities;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "categories")
public class Category extends BaseEntity {

    private String name;
    private Set<Book> books;

    public Category() {
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @ManyToMany(mappedBy = "categories")
    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
