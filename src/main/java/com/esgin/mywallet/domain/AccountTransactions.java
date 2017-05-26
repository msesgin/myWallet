package com.esgin.mywallet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A AccountTransactions.
 */
@Entity
@Table(name = "account_transactions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AccountTransactions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "amount", precision=10, scale=2, nullable = false)
    private BigDecimal amount;

    @ManyToOne
    private User fromUserEmail;

    @ManyToOne
    private User toUserEmail;

    @ManyToOne
    private UserAccount fromAccount;

    @ManyToOne
    private UserAccount toAccount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public AccountTransactions amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public User getFromUserEmail() {
        return fromUserEmail;
    }

    public AccountTransactions fromUserEmail(User user) {
        this.fromUserEmail = user;
        return this;
    }

    public void setFromUserEmail(User user) {
        this.fromUserEmail = user;
    }

    public User getToUserEmail() {
        return toUserEmail;
    }

    public AccountTransactions toUserEmail(User user) {
        this.toUserEmail = user;
        return this;
    }

    public void setToUserEmail(User user) {
        this.toUserEmail = user;
    }

    public UserAccount getFromAccount() {
        return fromAccount;
    }

    public AccountTransactions fromAccount(UserAccount userAccount) {
        this.fromAccount = userAccount;
        return this;
    }

    public void setFromAccount(UserAccount userAccount) {
        this.fromAccount = userAccount;
    }

    public UserAccount getToAccount() {
        return toAccount;
    }

    public AccountTransactions toAccount(UserAccount userAccount) {
        this.toAccount = userAccount;
        return this;
    }

    public void setToAccount(UserAccount userAccount) {
        this.toAccount = userAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountTransactions accountTransactions = (AccountTransactions) o;
        if (accountTransactions.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountTransactions.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccountTransactions{" +
            "id=" + getId() +
            ", amount='" + getAmount() + "'" +
            "}";
    }
}
