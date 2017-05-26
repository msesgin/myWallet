package com.esgin.mywallet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A UserAccount.
 */
@Entity
@Table(name = "user_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "account_name", length = 20, nullable = false)
    private String accountName;

    @NotNull
    @Column(name = "balance", precision=10, scale=2, nullable = false)
    private BigDecimal balance;

    @ManyToOne
    private User user;

    @ManyToOne
    private Currency currency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public UserAccount accountName(String accountName) {
        this.accountName = accountName;
        return this;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public UserAccount balance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public UserAccount user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Currency getCurrency() {
        return currency;
    }

    public UserAccount currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserAccount userAccount = (UserAccount) o;
        if (userAccount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userAccount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserAccount{" +
            "id=" + getId() +
            ", accountName='" + getAccountName() + "'" +
            ", balance='" + getBalance() + "'" +
            "}";
    }
}
