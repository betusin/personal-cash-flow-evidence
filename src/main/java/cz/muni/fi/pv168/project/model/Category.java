package cz.muni.fi.pv168.project.model;

import javax.persistence.*;

@Entity
@Table(name = "Category")
public class Category extends BaseEntity {
    private String name;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
