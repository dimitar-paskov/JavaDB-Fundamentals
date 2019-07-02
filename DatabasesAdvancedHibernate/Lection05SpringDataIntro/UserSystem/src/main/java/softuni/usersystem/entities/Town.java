package softuni.usersystem.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "towns")
public class Town extends BaseEntity {

    private String name;
    private Country country;

    private Set<User> citizens;
    private Set<User> born;

    public Town() {
        this.citizens = new HashSet<>();
        this.born = new HashSet<>();
    }

    public Town(String name) {
        this.name = name;
        this.citizens = new HashSet<>();
        this.born = new HashSet<>();
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }



    @OneToMany(mappedBy = "currentTown")
    public Set<User> getCitizens() {
        return citizens;
    }

    public void setCitizens(Set<User> citizens) {
        this.citizens = citizens;
    }

    @OneToMany(mappedBy = "bornTown")
    public Set<User> getBorn() {
        return born;
    }

    public void setBorn(Set<User> born) {
        this.born = born;
    }
}
