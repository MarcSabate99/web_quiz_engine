package engine.repository;

import engine.entity.Quiz;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.*;

public interface QuizRepository extends PagingAndSortingRepository<Quiz, Integer> {

    Optional<Quiz> findById(Long aLong);

    @Override
    <S extends Quiz> S save(S entity);

}
