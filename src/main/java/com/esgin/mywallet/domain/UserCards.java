package com.esgin.mywallet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A UserCards.
 */
@Entity
@Table(name = "user_cards")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserCards implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 16, max = 16)
    @Column(name = "credit_card_number", length = 16, nullable = false)
    private String creditCardNumber;

    @NotNull
    @Size(min = 2, max = 2)
    @Column(name = "last_usage_month", length = 2, nullable = false)
    private String lastUsageMonth;

    @NotNull
    @Size(min = 2, max = 2)
    @Column(name = "last_usage_year", length = 2, nullable = false)
    private String lastUsageYear;

    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "cvv", length = 3, nullable = false)
    private String cvv;

    @ManyToOne
    private User user;

    @ManyToOne
    private UserAccount toAccount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public UserCards creditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
        return this;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getLastUsageMonth() {
        return lastUsageMonth;
    }

    public UserCards lastUsageMonth(String lastUsageMonth) {
        this.lastUsageMonth = lastUsageMonth;
        return this;
    }

    public void setLastUsageMonth(String lastUsageMonth) {
        this.lastUsageMonth = lastUsageMonth;
    }

    public String getLastUsageYear() {
        return lastUsageYear;
    }

    public UserCards lastUsageYear(String lastUsageYear) {
        this.lastUsageYear = lastUsageYear;
        return this;
    }

    public void setLastUsageYear(String lastUsageYear) {
        this.lastUsageYear = lastUsageYear;
    }

    public String getCvv() {
        return cvv;
    }

    public UserCards cvv(String cvv) {
        this.cvv = cvv;
        return this;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public User getUser() {
        return user;
    }

    public UserCards user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserAccount getToAccount() {
        return toAccount;
    }

    public UserCards toAccount(UserAccount userAccount) {
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
        UserCards userCards = (UserCards) o;
        if (userCards.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userCards.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserCards{" +
            "id=" + getId() +
            ", creditCardNumber='" + getCreditCardNumber() + "'" +
            ", lastUsageMonth='" + getLastUsageMonth() + "'" +
            ", lastUsageYear='" + getLastUsageYear() + "'" +
            ", cvv='" + getCvv() + "'" +
            "}";
    }
}
