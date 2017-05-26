package com.esgin.mywallet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A CardTransactions.
 */
@Entity
@Table(name = "card_transactions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CardTransactions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "amount", precision=10, scale=2, nullable = false)
    private BigDecimal amount;

    @ManyToOne
    private UserCards creditCardNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public CardTransactions amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UserCards getCreditCardNumber() {
        return creditCardNumber;
    }

    public CardTransactions creditCardNumber(UserCards userCards) {
        this.creditCardNumber = userCards;
        return this;
    }

    public void setCreditCardNumber(UserCards userCards) {
        this.creditCardNumber = userCards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CardTransactions cardTransactions = (CardTransactions) o;
        if (cardTransactions.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cardTransactions.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CardTransactions{" +
            "id=" + getId() +
            ", amount='" + getAmount() + "'" +
            "}";
    }
}
