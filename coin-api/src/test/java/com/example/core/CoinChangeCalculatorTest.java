package com.example.core;

import com.example.model.CoinChangeRequest;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CoinChangeCalculatorTest {
    private final CoinChangeCalculator calculator = new CoinChangeCalculator();

    @Test
    void testExample1() {
        // Tests typical case with decimal amount and multiple denominations
        // Verifies correct combination of coins for 7.03 with [0.01, 0.5, 1, 5, 10]
        List<BigDecimal> coins = calculator.calculateMinimumCoins(
                new BigDecimal("7.03"),
                Arrays.asList(
                        new BigDecimal("0.01"),
                        new BigDecimal("0.5"),
                        new BigDecimal("1"),
                        new BigDecimal("5"),
                        new BigDecimal("10")
                )
        );

        assertEquals(Arrays.asList(
                new BigDecimal("0.01"),
                new BigDecimal("0.01"),
                new BigDecimal("0.01"),
                new BigDecimal("1"),
                new BigDecimal("1"),
                new BigDecimal("5")
        ), coins);
    }

    @Test
    void testExample2() {
        // Tests integer amount case with larger denominations
        // Verifies correct combination for 103 with [1, 2, 50]
        List<BigDecimal> coins = calculator.calculateMinimumCoins(
                new BigDecimal("103"),
                Arrays.asList(
                        new BigDecimal("1"),
                        new BigDecimal("2"),
                        new BigDecimal("50")
                )
        );

        assertEquals(Arrays.asList(
                new BigDecimal("1"),
                new BigDecimal("2"),
                new BigDecimal("50"),
                new BigDecimal("50")
        ), coins);
    }

    @Test
    void testInvalidAmount() {
        // Tests negative amount validation
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateMinimumCoins(
                    new BigDecimal("-1"),
                    Arrays.asList(new BigDecimal("1"))
            );
        });

        // Tests amount exceeding maximum limit (10000)
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateMinimumCoins(
                    new BigDecimal("10001"),
                    Arrays.asList(new BigDecimal("1"))
            );
        });
    }

    @Test
    void testInvalidDenomination() {
        // Tests validation of non-standard denomination (0.3)
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateMinimumCoins(
                    new BigDecimal("10"),
                    Arrays.asList(new BigDecimal("0.3"))
            );
        });
    }

    @Test
    void testImpossibleAmount() {
        // Tests case where exact change cannot be made
        // 0.03 cannot be made with 0.05 coins
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateMinimumCoins(
                    new BigDecimal("0.03"),
                    Arrays.asList(new BigDecimal("0.05"))
            );
        });
    }

    @Test
    void testEmptyDenominations() {
        // Tests validation when no denominations are provided
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateMinimumCoins(
                    new BigDecimal("10"),
                    Collections.emptyList()
            );
        });
    }

    @Test
    void testBoundaryAmounts() {
        // Tests minimum valid amount (0)
        assertEquals(List.of(), calculator.calculateMinimumCoins(
                BigDecimal.ZERO,
                List.of(new BigDecimal("1"))
        ));

        // Tests maximum valid amount (10000)
        List<BigDecimal> result = calculator.calculateMinimumCoins(
                new BigDecimal("10000"),
                List.of(new BigDecimal("1000"))
        );
        assertEquals(10, result.size()); // 10 × 1000 coins
        assertEquals(new BigDecimal("1000"), result.get(0));
    }

    @Test
    void testAllSmallestDenominations() {
        // Tests case where only smallest denomination is used
        List<BigDecimal> result = calculator.calculateMinimumCoins(
                new BigDecimal("0.05"),
                List.of(new BigDecimal("0.01"))
        );
        assertEquals(5, result.size()); // Should be 5 × 0.01 coins
    }
}