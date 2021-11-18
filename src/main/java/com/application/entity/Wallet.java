package com.application.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @NotNull(message = "Budget must not be empty")
    @Min(value = 0, message = "Budget must be ≧ 0")
    @Column(name = "budget")
    private BigDecimal budget = new BigDecimal(0);

    @NotNull(message = "Budget must not be empty")
    @Min(value = 0, message = "Budget must be ≧ 0")
    @Column(name = "balance")
    private BigDecimal balance = new BigDecimal(0);

    @OneToOne(mappedBy = "wallet")
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
