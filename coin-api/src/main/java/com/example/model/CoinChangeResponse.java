package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

public class CoinChangeResponse {
    private List<BigDecimal> coins;

    public CoinChangeResponse(List<BigDecimal> coins) {
        this.coins = coins;
    }

    @JsonProperty
    public List<BigDecimal> getCoins() {
        return coins;
    }
}
