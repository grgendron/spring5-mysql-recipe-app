package guru.springframework.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Created by grg 2021-12-05
 */
@Getter
@Setter
@NoArgsConstructor
public class BaseIngredientCommand {
    private Long id;
    private String description;
}
