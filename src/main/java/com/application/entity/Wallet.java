package com.application.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "wallet")
public class Wallet {
    @Id
    private Long id;

    @NotNull(message = "Budget must not be empty")
    @Min(value = 0, message = "Budget must be ≧ 0")
    @Column(name = "budget")
    private BigDecimal budget = new BigDecimal(0);

    @Column(name = "balance")
    private BigDecimal balance = new BigDecimal(0);


    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;

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


    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", budget=" + budget +
                ", balance=" + balance +
                '}';
    }
}
