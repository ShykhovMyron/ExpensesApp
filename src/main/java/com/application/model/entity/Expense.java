package com.application.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_added")
    private Date dateAdded;

    @ManyToOne
    @JoinTable(name = "expenses_types",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private ExpenseType type;

    @NotNull(message = "Amount must not be empty")
    @Min(value = 0, message = "Amount must be ≧ 0")
    @Column(name = "amount")
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    public Expense() {
    }

    public Expense(Date dateAdded, ExpenseType type, BigDecimal amount, User user) {
        this.dateAdded = dateAdded;
        this.type = type;
        this.amount = amount;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
//                ", dateAdded=" + dateAdded +
                ", type=" + type +
                ", amount=" + amount +
                ", user=" + user +
                '}';
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
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

    public ExpenseType getType() {
        return type;
    }

    public void setType(ExpenseType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Expense)) return false;
        Expense expense = (Expense) o;
        return Objects.equals(getId(), expense.getId()) &&
//                getDateAdded().equals(expense.getDateAdded()) &&
                getType().equals(expense.getType()) &&
                getAmount().equals(expense.getAmount()) &&
                getUser().equals(expense.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),
//                getDateAdded(),
                getType(),
                getAmount(),
                getUser());
    }
}
