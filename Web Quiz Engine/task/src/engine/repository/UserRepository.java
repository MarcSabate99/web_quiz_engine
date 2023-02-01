package engine.repository;

import engine.entity.Quiz;
import engine.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Override
    <S extends User> S save(S entity);
    @Override
    Iterable<User> findAllById(Iterable<Long> longs);
}
