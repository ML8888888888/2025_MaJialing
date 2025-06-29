package com.example.api;

import com.example.core.CoinChangeCalculator;
import com.example.model.CoinChangeRequest;
import com.example.model.CoinChangeResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.List;

@Path("/api/coin-change")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CoinChangeResource {
    private final CoinChangeCalculator calculator;

    public CoinChangeResource() {
        this.calculator = new CoinChangeCalculator();
    }

    @POST
    public Response calculateMinimumCoins(CoinChangeRequest request) {
        try {
            List<BigDecimal> coins = calculator.calculateMinimumCoins(
                    request.getTargetAmount(),
                    request.getAvailableDenominations()
            );

            return Response.ok(new CoinChangeResponse(coins)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    private static class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}