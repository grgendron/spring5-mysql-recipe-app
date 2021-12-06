package guru.springframework.repositories;

import guru.springframework.domain.BaseIngredient;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BaseIngredientRepository extends CrudRepository<BaseIngredient, Long> {
    Optional<BaseIngredient> findByDescription(String description);
}
