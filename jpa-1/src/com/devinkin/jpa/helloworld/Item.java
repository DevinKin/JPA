package com.devinkin.jpa.helloworld;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "JPA_ITEMS")
@Entity
public class Item {
    private Integer id;
    private String itemName;

    private Set<Category> categories = new HashSet<>();

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "ITEM_NAME")
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @JoinTable(name = "JPA_ITEM_CATEGORY"
            ,joinColumns = {@JoinColumn(name = "ITEM_ID",
            referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "CATEGORY_ID",
            referencedColumnName = "ID")})
    @ManyToMany()
    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
