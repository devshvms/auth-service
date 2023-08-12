package in.dev.shvms.authservice.mysqltest;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends R2dbcRepository<User, Long> {

}
