package guru.springframework.converters;

import guru.springframework.commands.BaseIngredientCommand;
import guru.springframework.domain.BaseIngredient;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Created by jt on 6/21/17.
 */
@Component
public class BaseIngredientCommandToBaseIngredient implements Converter<BaseIngredientCommand, BaseIngredient> {

    @Nullable
    @Override
    public BaseIngredient convert(BaseIngredientCommand baseIngredientCommand) {
        if (baseIngredientCommand == null) {
            return null;
        }
        final BaseIngredient baseIngredient = new BaseIngredient();
        baseIngredient.setId(baseIngredientCommand.getId());
        baseIngredient.setDescription(baseIngredientCommand.getDescription());
        return baseIngredient;
    }
}
