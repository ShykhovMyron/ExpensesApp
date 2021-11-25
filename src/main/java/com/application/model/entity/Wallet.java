package com.application.model.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "budget")
    private BigDecimal budget = new BigDecimal(0);

    @Column(name = "balance")
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
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", budget=" + budget +
                ", balance=" + balance +
                '}';
    }
}
