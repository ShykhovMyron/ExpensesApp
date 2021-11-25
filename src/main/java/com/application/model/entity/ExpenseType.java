package com.application.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "types")
public class ExpenseType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(name = "type",unique = true)
    private String type;

    @ManyToMany(mappedBy = "types")
    private List<Wallet> wallets;
    @OneToMany(mappedBy = "type")
    private List<Expense> expenses;

    public ExpenseType() {
    }

    public ExpenseType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpenseType)) return false;

        ExpenseType that = (ExpenseType) o;

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

    public List<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }
}
