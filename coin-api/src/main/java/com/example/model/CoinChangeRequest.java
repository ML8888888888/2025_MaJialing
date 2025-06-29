package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class CoinChangeRequest {
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("10000.00")
    private BigDecimal targetAmount;

    @NotNull
    private List<BigDecimal> availableDenominations; // 改为可用的面额列表

    @JsonProperty
    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    @JsonProperty
    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    @JsonProperty("availableDenominations")
    public List<BigDecimal> getAvailableDenominations() {
        return availableDenominations;
    }

    @JsonProperty("availableDenominations")
    public void setAvailableDenominations(List<BigDecimal> availableDenominations) {
        this.availableDenominations = availableDenominations;
    }
}
