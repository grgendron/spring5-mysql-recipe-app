package guru.springframework.services;

import guru.springframework.commands.BaseIngredientCommand;

import java.util.List;

public interface BaseIngredientService {
    List<BaseIngredientCommand> listAllBaseIngredients();

    BaseIngredientCommand findById(Long id);

    BaseIngredientCommand saveBasicIngredientCommand(BaseIngredientCommand command);

    //void deleteById(Long recipeId, Long idToDelete);
}
