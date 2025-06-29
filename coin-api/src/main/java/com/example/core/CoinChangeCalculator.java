package com.example.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CoinChangeCalculator {
    private static final List<BigDecimal> STANDARD_DENOMINATIONS = List.of(
            new BigDecimal("0.01"), new BigDecimal("0.05"), new BigDecimal("0.1"),
            new BigDecimal("0.2"), new BigDecimal("0.5"), new BigDecimal("1"),
            new BigDecimal("2"), new BigDecimal("5"), new BigDecimal("10"),
            new BigDecimal("50"), new BigDecimal("100"), new BigDecimal("1000")
    );

    public List<BigDecimal> calculateMinimumCoins(BigDecimal amount, List<BigDecimal> denominations) {
        validateInput(amount, denominations);

        // Convert to cents to avoid floating point precision issues
        int targetCents = amount.multiply(new BigDecimal("100")).intValueExact();
        List<Integer> denominationCents = new ArrayList<>();

        for (BigDecimal d : denominations) {
            denominationCents.add(d.multiply(new BigDecimal("100")).intValueExact());
        }
        return greedyAlgorithm(targetCents, denominationCents);
    }

    private List<BigDecimal> greedyAlgorithm(int targetCents, List<Integer> denominationCents) {
        // Sort in descending order
        denominationCents.sort(Collections.reverseOrder());

        List<BigDecimal> result = new ArrayList<>();
        int remaining = targetCents;

        for (int coin : denominationCents) {
            while (remaining >= coin) {
                result.add(new BigDecimal(coin).divide(new BigDecimal("100")));
                remaining -= coin;
            }
        }

        if (remaining != 0) {
            throw new IllegalArgumentException("Greedy algorithm failed");
        }

        Collections.sort(result);
        return result;
    }

    private List<BigDecimal> dynamicProgramming(int targetCents, List<Integer> denominationCents) {
        // dp[i] will store the minimum number of coins needed for amount i
        int[] dp = new int[targetCents + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0; // Base case: 0 coins needed for 0 amount

        // To reconstruct the solution
        int[] lastCoin = new int[targetCents + 1];

        for (int i = 1; i <= targetCents; i++) {
            for (int coin : denominationCents) {
                if (coin <= i && dp[i - coin] != Integer.MAX_VALUE && dp[i - coin] + 1 < dp[i]) {
                    dp[i] = dp[i - coin] + 1;
                    lastCoin[i] = coin;
                }
            }
        }

        if (dp[targetCents] == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Cannot make target amount with provided coins");
        }

        // Reconstruct the solution
        List<BigDecimal> result = new ArrayList<>();
        int remaining = targetCents;
        while (remaining > 0) {
            int coin = lastCoin[remaining];
            result.add(new BigDecimal(coin).divide(new BigDecimal("100")));
            remaining -= coin;
        }

        Collections.sort(result);
        return result;
    }

    private void validateInput(BigDecimal amount, List<BigDecimal> denominations) {
        if (amount.compareTo(BigDecimal.ZERO) < 0 || amount.compareTo(new BigDecimal("10000")) > 0) {
            throw new IllegalArgumentException("Target amount must be between 0 and 10,000");
        }

        if (denominations == null || denominations.isEmpty()) {
            throw new IllegalArgumentException("At least one coin denomination must be provided");
        }

        // Validate all denominations are standard
        for (BigDecimal coin : denominations) {
            if (!STANDARD_DENOMINATIONS.contains(coin)) {
                throw new IllegalArgumentException("Invalid coin denomination:" + coin +
                        ". Valid denominations are: " + STANDARD_DENOMINATIONS);
            }
        }
    }
}
