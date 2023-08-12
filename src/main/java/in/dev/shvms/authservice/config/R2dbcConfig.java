package in.dev.shvms.authservice.config;

import in.dev.shvms.authservice.aop.CustomerContextHolder;
import in.dev.shvms.authservice.aop.DynamicRoutingDataSource;
import in.dev.shvms.authservice.utility.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
import reactor.core.publisher.Mono;


@Configuration
public class R2dbcConfig {
    @Bean
    public AbstractRoutingConnectionFactory abstractRoutingConnectionFactory(DynamicRoutingDataSource dynamicRoutingDataSource) {
        AbstractRoutingConnectionFactory abstractRoutingConnectionFactory = new AbstractRoutingConnectionFactory() {
            @Override
            protected Mono<Object> determineCurrentLookupKey() {
                return Mono.just(CustomerContextHolder.getCurrentCustomerId());
            }
        };
        abstractRoutingConnectionFactory.setTargetConnectionFactories(dynamicRoutingDataSource.connectionFactoryLookupMap);
        abstractRoutingConnectionFactory.setDefaultTargetConnectionFactory(dynamicRoutingDataSource.getConnectionFactory(Constants.DEFAULT));
        return abstractRoutingConnectionFactory;
    }
}
