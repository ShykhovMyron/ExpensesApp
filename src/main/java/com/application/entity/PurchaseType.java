package com.application.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "types")
public class PurchaseType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;
    @Column(unique = true)
    private String type;
    @ManyToMany(mappedBy = "types")
    private List<User> types;

    public PurchaseType() {
    }

    public PurchaseType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<User> getTypes() {
        return types;
    }

    public void setTypes(List<User> types) {
        this.types = types;
    }
}
