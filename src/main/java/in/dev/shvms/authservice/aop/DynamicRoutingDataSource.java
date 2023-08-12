package in.dev.shvms.authservice.aop;

import in.dev.shvms.authservice.utility.Constants;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.r2dbc.connection.lookup.ConnectionFactoryLookup;
import org.springframework.r2dbc.connection.lookup.ConnectionFactoryLookupFailureException;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@Service
@PropertySource("classpath:application.properties")
public class DynamicRoutingDataSource implements ConnectionFactoryLookup {

    @Value("${spring.r2dbc.url}")
    String r2dbcBaseUrl;
    @Value("${spring.r2dbc.username}")
    String r2dbcUsername;
    @Value("${spring.r2dbc.password}")
    String r2dbcPassword;
    public Map<String, ConnectionFactory> connectionFactoryLookupMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void initialize() {
        ConnectionFactoryOptions options = ConnectionFactoryOptions.parse(r2dbcBaseUrl)
                    .mutate()
                    .option(ConnectionFactoryOptions.USER, r2dbcUsername)
                    .option(ConnectionFactoryOptions.PASSWORD, r2dbcPassword)
                    .build();
        connectionFactoryLookupMap.put(Constants.DEFAULT, ConnectionFactories.get(options));

        // adding 2nd Database this will be accessed when we will pass routing key as 'ais' .
        options = ConnectionFactoryOptions.parse(String.format("%s_%s", r2dbcBaseUrl, "ais"))
                .mutate()
                .option(ConnectionFactoryOptions.USER, r2dbcUsername)
                .option(ConnectionFactoryOptions.PASSWORD, r2dbcPassword)
                .build();
        connectionFactoryLookupMap.put("ais", ConnectionFactories.get(options));
    }

    @Override
    public ConnectionFactory getConnectionFactory(String connectionFactoryName) throws ConnectionFactoryLookupFailureException {
        return connectionFactoryLookupMap.get(connectionFactoryName);
    }

    public void mapNewDataSource(String dbLookupKey) {
        if (Objects.nonNull(dbLookupKey) && !dbLookupKey.strip().equals(Constants.DEFAULT) && !connectionFactoryLookupMap.containsKey(dbLookupKey)) {
            // New create <database-name> should be executed here on MySQL server.
            ConnectionFactoryOptions options = ConnectionFactoryOptions.parse(String.format("%s_%s", r2dbcBaseUrl, dbLookupKey))
                    .mutate()
                    .option(ConnectionFactoryOptions.USER, r2dbcUsername)
                    .option(ConnectionFactoryOptions.PASSWORD, r2dbcPassword)
                    .build();
            connectionFactoryLookupMap.put(Constants.DEFAULT, ConnectionFactories.get(options));
        } else {
            throw new RuntimeException("dbLookupKey already exists!");
        }
    }

    public void unMapDataSource(String dbLookupKey) {
        if (Objects.nonNull(dbLookupKey) && !dbLookupKey.strip().equals(Constants.DEFAULT) && connectionFactoryLookupMap.containsKey(dbLookupKey)) {
            this.connectionFactoryLookupMap.remove(dbLookupKey);
        } else {
            throw new RuntimeException("dbLookupKey doesn't exist!");
        }
    }

}
