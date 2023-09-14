package in.dev.shvms.authservice.mysqltest;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

}
