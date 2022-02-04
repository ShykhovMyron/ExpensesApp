package com.application.model.entity;

import com.application.config.ExpensesConfig;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column
    private Date dateAdded;

    @ManyToOne
    @JoinTable(name = "expenses_types",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private ExpenseType type;

    @Column
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
    public void setDateAdded(String dateAdded, ExpensesConfig config) throws ParseException {
        this.dateAdded = new SimpleDateFormat(config.getInputDateFormat(),
                Locale.ENGLISH).parse(dateAdded);
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
