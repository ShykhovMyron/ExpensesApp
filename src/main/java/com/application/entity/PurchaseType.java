package com.application.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "types")
public class PurchaseType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @NotEmpty(message = "Type must not be empty")
    @Column(unique = true)
    private String type;

    @ManyToMany(mappedBy = "types")
    private List<User> types;
    @OneToMany(mappedBy = "type")
    private List<Purchase> purchases;

    public PurchaseType() {
    }

    public PurchaseType(String type) {
        this.type = type;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PurchaseType)) return false;

        PurchaseType that = (PurchaseType) o;

        return getType().equals(that.getType());
    }

    @Override
    public int hashCode() {
        return getType().hashCode();
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
