package com.esgin.mywallet.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the UserCards entity.
 */
public class UserCardsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 16, max = 16)
    private String creditCardNumber;

    @NotNull
    @Size(min = 2, max = 2)
    private String lastUsageMonth;

    @NotNull
    @Size(min = 2, max = 2)
    private String lastUsageYear;

    @NotNull
    @Size(min = 3, max = 3)
    private String cvv;

    private Long userId;

    private String userLogin;

    private Long toAccountId;

    private String toAccountAccountName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getLastUsageMonth() {
        return lastUsageMonth;
    }

    public void setLastUsageMonth(String lastUsageMonth) {
        this.lastUsageMonth = lastUsageMonth;
    }

    public String getLastUsageYear() {
        return lastUsageYear;
    }

    public void setLastUsageYear(String lastUsageYear) {
        this.lastUsageYear = lastUsageYear;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
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

        UserCardsDTO userCardsDTO = (UserCardsDTO) o;
        if(userCardsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userCardsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserCardsDTO{" +
            "id=" + getId() +
            ", creditCardNumber='" + getCreditCardNumber() + "'" +
            ", lastUsageMonth='" + getLastUsageMonth() + "'" +
            ", lastUsageYear='" + getLastUsageYear() + "'" +
            ", cvv='" + getCvv() + "'" +
            "}";
    }
}
