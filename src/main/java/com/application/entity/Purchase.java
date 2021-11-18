package com.application.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @NotNull(message = "Type must not be empty")
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;
    @NotNull(message = "Amount must not be empty")
    @Min(value = 0, message = "Amount must be ≧ 0")
    @Column(name = "amount")
    private BigDecimal amount;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    @Temporal(TemporalType.DATE)
    @Column(name = "date_added")
    Date dateAdded;

    @Override
    public String toString() {
        return "Purchases{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", dateAdded=" + dateAdded +
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
