package com.application.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private BigDecimal budget = new BigDecimal(0);

    @Column
    private BigDecimal balance = new BigDecimal(0);

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "wallet_types",
            joinColumns = @JoinColumn(name = "wallet_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private Set<ExpenseType> types;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "wallet")
    private User user;

    public Wallet(Set<ExpenseType> types) {
        this.types = types;
    }

    public Wallet() {
    }

    public Wallet(BigDecimal budget, BigDecimal balance, Set<ExpenseType> types, User user) {
        this.budget = budget;
        this.balance = balance;
        this.types = types;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Set<ExpenseType> getTypes() {
        return types;
    }

    public void setTypes(Set<ExpenseType> types) {
        this.types = types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wallet)) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(getId(), wallet.getId()) && getBudget().equals(wallet.getBudget()) && getBalance().equals(wallet.getBalance()) && getTypes().equals(wallet.getTypes()) && getUser().equals(wallet.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBudget(), getBalance(), getTypes(), getUser());
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", budget=" + budget +
                ", balance=" + balance +
                '}';
    }
}
