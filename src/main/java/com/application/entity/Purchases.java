package com.application.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "purchases")
public class Purchases {
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
    private Long amount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    public Purchases(Long amount, Type type, User user) {
        this.type = type;
        this.amount = amount;
        this.user = user;
    }

    public Purchases() {
    }

    @Override
    public String toString() {
        return "Purchases{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", user=" + user +
                '}';
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

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
