package guru.springframework.converters;

import guru.springframework.commands.BaseIngredientCommand;
import guru.springframework.domain.BaseIngredient;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Created by jt on 6/21/17.
 */
@Component
public class BaseIngredientToBaseIngredientCommand implements Converter<BaseIngredient, BaseIngredientCommand> {

    private final UnitOfMeasureToUnitOfMeasureCommand converter;

    public BaseIngredientToBaseIngredientCommand(UnitOfMeasureToUnitOfMeasureCommand converter) {
        this.converter = converter;
    }

    @Synchronized
    @Nullable
    @Override
    public BaseIngredientCommand convert(BaseIngredient baseIngredient) {
        if (baseIngredient == null) {
            return null;
        }

        BaseIngredientCommand baseIngredientCommand = new BaseIngredientCommand();
        baseIngredientCommand.setId(baseIngredient.getId());
        baseIngredientCommand.setDescription(baseIngredient.getDescription());
         return baseIngredientCommand;
    }
}
