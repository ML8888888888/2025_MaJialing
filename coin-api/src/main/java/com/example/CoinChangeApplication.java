package com.example;

import com.example.api.CoinChangeResource;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import java.util.EnumSet;

public class CoinChangeApplication extends Application<CoinChangeConfiguration> {
    public static void main(String[] args) throws Exception {
        new CoinChangeApplication().run(args);
    }

    @Override
    public String getName() {
        return "coin-change-api";
    }

    @Override
    public void initialize(Bootstrap<CoinChangeConfiguration> bootstrap) {
        // Initialization if needed
    }

    @Override
    public void run(CoinChangeConfiguration configuration, Environment environment) {
        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");  // 允许所有来源，生产环境建议限制
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        // 注册你的资源
        final CoinChangeResource resource = new CoinChangeResource();
        environment.jersey().register(resource);
    }
}
