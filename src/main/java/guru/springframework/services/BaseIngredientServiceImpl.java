package guru.springframework.services;

import guru.springframework.commands.BaseIngredientCommand;
import guru.springframework.converters.BaseIngredientCommandToBaseIngredient;
import guru.springframework.converters.BaseIngredientToBaseIngredientCommand;
import guru.springframework.domain.BaseIngredient;
import guru.springframework.repositories.BaseIngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BaseIngredientServiceImpl implements BaseIngredientService {

    private final BaseIngredientToBaseIngredientCommand baseIngredientToBaseIngredientCommand;
    private final BaseIngredientCommandToBaseIngredient baseIngredientCommandToBaseIngredient;
    private final BaseIngredientRepository baseIngredientRepository;

    public BaseIngredientServiceImpl(BaseIngredientToBaseIngredientCommand baseIngredientToBaseIngredientCommand,
                                     BaseIngredientCommandToBaseIngredient baseIngredientCommandToBaseIngredient,
                                     BaseIngredientRepository baseIngredientRepository) {
        this.baseIngredientToBaseIngredientCommand = baseIngredientToBaseIngredientCommand;
        this.baseIngredientCommandToBaseIngredient = baseIngredientCommandToBaseIngredient;
        this.baseIngredientRepository = baseIngredientRepository;
    }

    @Override
    public List<BaseIngredientCommand> listAllBaseIngredients() {

        return StreamSupport.stream(baseIngredientRepository.findAll()
                        .spliterator(), false)
                .map(baseIngredientToBaseIngredientCommand::convert)
                .collect(Collectors.toList());
    }
    @Override
    public BaseIngredientCommand findById(Long basicIngredientId) {
        Optional<BaseIngredient> obi = baseIngredientRepository.findById(basicIngredientId);
        return null;
    }

    @Override
    public BaseIngredientCommand saveBasicIngredientCommand(BaseIngredientCommand command) {
        return null;
    }
}
