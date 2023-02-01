package engine.repository;


import engine.entity.Completion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompletionRepository extends PagingAndSortingRepository<Completion, Long> {
    Page<Completion> findAllByUserEmail(String userEmail, Pageable pageable);
}
