package com.esgin.mywallet.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the CardTransactions entity.
 */
public class CardTransactionsDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal amount;

    private Long creditCardNumberId;

    private String creditCardNumberCreditCardNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getCreditCardNumberId() {
        return creditCardNumberId;
    }

    public void setCreditCardNumberId(Long userCardsId) {
        this.creditCardNumberId = userCardsId;
    }

    public String getCreditCardNumberCreditCardNumber() {
        return creditCardNumberCreditCardNumber;
    }

    public void setCreditCardNumberCreditCardNumber(String userCardsCreditCardNumber) {
        this.creditCardNumberCreditCardNumber = userCardsCreditCardNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CardTransactionsDTO cardTransactionsDTO = (CardTransactionsDTO) o;
        if(cardTransactionsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cardTransactionsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CardTransactionsDTO{" +
            "id=" + getId() +
            ", amount='" + getAmount() + "'" +
            "}";
    }
}
