package in.dev.shvms.authservice.aop;


import in.dev.shvms.authservice.utility.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;


@Component
public class CustomerContextFilter implements WebFilter {
    Logger logger = LoggerFactory.getLogger(CustomerContextFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        long startTime = System.currentTimeMillis();
        String routingLookupKey = exchange.getRequest().getHeaders().getOrDefault(Constants.ROUTING_LOOKUP_KEY, List.of(Constants.DEFAULT)).get(0);
        logger.debug("Fetched header X-lookup: {}", routingLookupKey);
        CustomerContextHolder.setCurrentCustomerId(routingLookupKey);
        return chain.filter(exchange).doOnSuccess(aVoid -> {
            CustomerContextHolder.clearCurrentCustomerId();
            logger.info(Constants.ENDPOINT_RESPONSE_TIME, exchange.getRequest().getMethod(), exchange.getRequest().getPath(), System.currentTimeMillis() - startTime);
        });
    }
}
