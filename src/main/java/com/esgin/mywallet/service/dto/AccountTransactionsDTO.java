package com.esgin.mywallet.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the AccountTransactions entity.
 */
public class AccountTransactionsDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal amount;

    private Long fromUserEmailId;

    private String fromUserEmailLogin;

    private Long toUserEmailId;

    private String toUserEmailLogin;

    private Long fromAccountId;

    private String fromAccountAccountName;

    private Long toAccountId;

    private String toAccountAccountName;

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

    public Long getFromUserEmailId() {
        return fromUserEmailId;
    }

    public void setFromUserEmailId(Long userId) {
        this.fromUserEmailId = userId;
    }

    public String getFromUserEmailLogin() {
        return fromUserEmailLogin;
    }

    public void setFromUserEmailLogin(String userLogin) {
        this.fromUserEmailLogin = userLogin;
    }

    public Long getToUserEmailId() {
        return toUserEmailId;
    }

    public void setToUserEmailId(Long userId) {
        this.toUserEmailId = userId;
    }

    public String getToUserEmailLogin() {
        return toUserEmailLogin;
    }

    public void setToUserEmailLogin(String userLogin) {
        this.toUserEmailLogin = userLogin;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long userAccountId) {
        this.fromAccountId = userAccountId;
    }

    public String getFromAccountAccountName() {
        return fromAccountAccountName;
    }

    public void setFromAccountAccountName(String userAccountAccountName) {
        this.fromAccountAccountName = userAccountAccountName;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long userAccountId) {
        this.toAccountId = userAccountId;
    }

    public String getToAccountAccountName() {
        return toAccountAccountName;
    }

    public void setToAccountAccountName(String userAccountAccountName) {
        this.toAccountAccountName = userAccountAccountName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountTransactionsDTO accountTransactionsDTO = (AccountTransactionsDTO) o;
        if(accountTransactionsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountTransactionsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccountTransactionsDTO{" +
            "id=" + getId() +
            ", amount='" + getAmount() + "'" +
            "}";
    }
}
