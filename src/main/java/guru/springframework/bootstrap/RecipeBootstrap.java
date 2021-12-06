package guru.springframework.bootstrap;

import guru.springframework.domain.*;
import guru.springframework.repositories.BaseIngredientRepository;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by jt on 6/13/17.
 */
@Slf4j
@Component
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private Environment environment;
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final BaseIngredientRepository baseIngredientRepository;

    public RecipeBootstrap(CategoryRepository categoryRepository,
                           RecipeRepository recipeRepository,
                           UnitOfMeasureRepository unitOfMeasureRepository,
                           BaseIngredientRepository baseIngredientRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.baseIngredientRepository = baseIngredientRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        boolean isH2Env = false;
        boolean isDevEnv = false;
        String active = null;
        String[] list = environment.getActiveProfiles();
        for (String prof : list) {
            active = prof;
            isH2Env = "h2".equals(prof);
            isDevEnv = "dev".equals(prof);
            if (isH2Env || isDevEnv) {
                break;
            }
        }
        if (!(isH2Env || isDevEnv)) {
            log.debug("Only 'h2' & 'dev' load Test Data... quitting");
            return;
        }
        log.debug("Active Profile: " + active);
        boolean isEmpty = unitOfMeasureRepository.count() == 0 ||
                categoryRepository.count() == 0 ||
                baseIngredientRepository.count() == 0;
        if (isEmpty) {
            fillUomRepository();
            fillCategoryRepository();
            fillBasicIngredientRepository();
        }
        if (isH2Env) {
            recipeRepository.saveAll(getRecipes());
            log.debug("Loaded 'h2' Test Data");
        } else if (isDevEnv) {
            if (isEmpty) {
                throw new RuntimeException("FATAL: Dev test data problem: \n\tcategory || base_ingredient || unit_of_measure table is empty.");
            }
            if (recipeRepository.count() != 2) {
                recipeRepository.saveAll(getRecipes());
                log.debug("Loaded 'dev' test data.");
            } else {
                log.debug("2 Test recipes found, no need to load more.");
            }
        }
    }


    private List<Recipe> getRecipes() {
        /*
         *  ASSUMES category, unit_of_measure, and base_ingredient table is loaded.
         *  ASSUMES recipe table is empty.
         */
        List<Recipe> recipes = new ArrayList<>(2);
        //get UOMs
        Optional<UnitOfMeasure> eachUomOptional = unitOfMeasureRepository.findByDescription("Each");
        Optional<UnitOfMeasure> tableSpoonUomOptional = unitOfMeasureRepository.findByDescription("Tablespoon");
        Optional<UnitOfMeasure> teaSpoonUomOptional = unitOfMeasureRepository.findByDescription("Teaspoon");
        Optional<UnitOfMeasure> dashUomOptional = unitOfMeasureRepository.findByDescription("Dash");
        Optional<UnitOfMeasure> pintUomOptional = unitOfMeasureRepository.findByDescription("Pint");
        Optional<UnitOfMeasure> cupsUomOptional = unitOfMeasureRepository.findByDescription("Cup");

        //get optionals
        UnitOfMeasure eachUom = eachUomOptional.get();
        UnitOfMeasure tableSpoonUom = tableSpoonUomOptional.get();
        UnitOfMeasure teapoonUom = tableSpoonUomOptional.get();
        UnitOfMeasure dashUom = dashUomOptional.get();
        UnitOfMeasure pintUom = dashUomOptional.get();
        UnitOfMeasure cupsUom = cupsUomOptional.get();
        //get Categories
        Optional<Category> americanCategoryOptional = categoryRepository.findByDescription("American");
        Optional<Category> mexicanCategoryOptional = categoryRepository.findByDescription("Mexican");
        Category americanCategory = americanCategoryOptional.get();
        Category mexicanCategory = mexicanCategoryOptional.get();

        //Yummy Guac
        Recipe guacRecipe = new Recipe();
        guacRecipe.setDescription("Perfect Guacamole");
        guacRecipe.setPrepTime(10);
        guacRecipe.setCookTime(0);
        guacRecipe.setDifficulty(Difficulty.EASY);
        guacRecipe.setDirections("1 Cut avocado, remove flesh: Cut the avocados in half. Remove seed. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon" +
                "\n" +
                "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)" +
                "\n" +
                "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                "4 Cover with plastic and chill to store: Place plastic wrap on the surface of the guacamole cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.\n" +
                "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.\n" +
                "\n" +
                "\n" +
                "Read more: http://www.simplyrecipes.com/recipes/perfect_guacamole/#ixzz4jvpiV9Sd");

        Notes guacNotes = new Notes();
        guacNotes.setRecipeNotes("For a very quick guacamole just take a 1/4 cup of salsa and mix it in with your mashed avocados.\n" +
                "Feel free to experiment! One classic Mexican guacamole has pomegranate seeds and chunks of peaches in it (a Diana Kennedy favorite). Try guacamole with added pineapple, mango, or strawberries.\n" +
                "The simplest version of guacamole is just mashed avocados with salt. Don't let the lack of availability of other ingredients stop you from making guacamole.\n" +
                "To extend a limited supply of avocados, add either sour cream or cottage cheese to your guacamole dip. Purists may be horrified, but so what? It tastes great.\n" +
                "\n" +
                "\n" +
                "Read more: http://www.simplyrecipes.com/recipes/perfect_guacamole/#ixzz4jvoun5ws");

        guacRecipe.setNotes(guacNotes);
        BaseIngredientRepository bir = baseIngredientRepository;
        Optional<BaseIngredient> obi;
        BaseIngredient bi;
        obi = bir.findByDescription("Avacado, ripe");
        bi = obi.get();
        guacRecipe.addIngredient(new Ingredient(bi, new BigDecimal(2), eachUom));
        obi = bir.findByDescription("Salt, Kosher");
        bi = obi.get();
        guacRecipe.addIngredient(new Ingredient(bi, new BigDecimal(".5"), teapoonUom));
        obi = bir.findByDescription("Lime juice, fresh");
        bi = obi.get();
        guacRecipe.addIngredient(new Ingredient(bi, new BigDecimal(2), tableSpoonUom));
        obi = bir.findByDescription("Onion, green, thinly sliced");
        bi = obi.get();
        guacRecipe.addIngredient(new Ingredient(bi, new BigDecimal(2), tableSpoonUom));
        obi = bir.findByDescription("Serrano chiles, stems and seeds removed, minced");
        bi = obi.get();
        guacRecipe.addIngredient(new Ingredient(bi, new BigDecimal(2), eachUom));
        obi = bir.findByDescription("Cilantro");
        bi = obi.get();
        guacRecipe.addIngredient(new Ingredient(bi, new BigDecimal(2), tableSpoonUom));
        obi = bir.findByDescription("Pepper, black, freshly ground");
        bi = obi.get();
        guacRecipe.addIngredient(new Ingredient(bi, new BigDecimal(2), dashUom));
        obi = bir.findByDescription("Tomato, ripe, seeds and pulp removed, chopped");
        bi = obi.get();
        guacRecipe.addIngredient(new Ingredient(bi, new BigDecimal(".5"), eachUom));

        guacRecipe.getCategories().add(americanCategory);
        guacRecipe.getCategories().add(mexicanCategory);
        guacRecipe.setUrl("http://www.simplyrecipes.com/recipes/perfect_guacamole/");
        guacRecipe.setServings(4);
        guacRecipe.setSource("Simply Recipes");
        //add to return list
        recipes.add(guacRecipe);

        //Yummy Tacos
        Recipe tacosRecipe = new Recipe();
        tacosRecipe.setDescription("Spicy Grilled Chicken Taco");
        tacosRecipe.setCookTime(9);
        tacosRecipe.setPrepTime(20);
        tacosRecipe.setDifficulty(Difficulty.MODERATE);

        tacosRecipe.setDirections("1 Prepare a gas or charcoal grill for medium-high, direct heat.\n" +
                "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste. Add the chicken to the bowl and toss to coat all over.\n" +
                "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n" +
                "\n" +
                "\n" +
                "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
                "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high heat. As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\n" +
                "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
                "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula. Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.\n" +
                "\n" +
                "\n" +
                "Read more: http://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/#ixzz4jvtrAnNm");

        Notes tacoNotes = new Notes();
        tacoNotes.setRecipeNotes("We have a family motto and it is this: Everything goes better in a tortilla.\n" +
                "Any and every kind of leftover can go inside a warm tortilla, usually with a healthy dose of pickled jalapenos. I can always sniff out a late-night snacker when the aroma of tortillas heating in a hot pan on the stove comes wafting through the house.\n" +
                "Today’s tacos are more purposeful – a deliberate meal instead of a secretive midnight snack!\n" +
                "First, I marinate the chicken briefly in a spicy paste of ancho chile powder, oregano, cumin, and sweet orange juice while the grill is heating. You can also use this time to prepare the taco toppings.\n" +
                "Grill the chicken, then let it rest while you warm the tortillas. Now you are ready to assemble the tacos and dig in. The whole meal comes together in about 30 minutes!\n" +
                "\n" +
                "\n" +
                "Read more: http://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/#ixzz4jvu7Q0MJ");

        tacosRecipe.setNotes(tacoNotes);
        obi = bir.findByDescription("Ancho Chili Powder");
        bi = obi.get();
        tacosRecipe.addIngredient(new Ingredient(bi, new BigDecimal(2), tableSpoonUom));
        obi = bir.findByDescription("Oregano, dried");
        bi = obi.get();
        tacosRecipe.addIngredient(new Ingredient(bi, new BigDecimal(1), teapoonUom));
        obi = bir.findByDescription("Cumin, dried");
        bi = obi.get();
        tacosRecipe.addIngredient(new Ingredient(bi, new BigDecimal(1), teapoonUom));
        obi = bir.findByDescription("Sugar");
        bi = obi.get();
        tacosRecipe.addIngredient(new Ingredient(bi, new BigDecimal(1), teapoonUom));
        obi = bir.findByDescription("Salt");
        bi = obi.get();
        tacosRecipe.addIngredient(new Ingredient(bi, new BigDecimal(".5"), teapoonUom));
        obi = bir.findByDescription("Garlic, chopped clove");
        bi = obi.get();
        tacosRecipe.addIngredient(new Ingredient(bi, new BigDecimal(1), eachUom));
        obi = bir.findByDescription("Orange zest, grated fine");
        bi = obi.get();
        tacosRecipe.addIngredient(new Ingredient(bi, new BigDecimal(1), tableSpoonUom));
        obi = bir.findByDescription("Orange juice, fresh squeezed");
        bi = obi.get();
        tacosRecipe.addIngredient(new Ingredient(bi, new BigDecimal(3), tableSpoonUom));
        obi = bir.findByDescription("Olive oil");
        bi = obi.get();
        tacosRecipe.addIngredient(new Ingredient(bi, new BigDecimal(2), tableSpoonUom));
        obi = bir.findByDescription("Chicken, boneless thighs");
        bi = obi.get();
        tacosRecipe.addIngredient(new Ingredient(bi, new BigDecimal(4), tableSpoonUom));
        obi = bir.findByDescription("Tortilla, small corn");
        bi = obi.get();
        tacosRecipe.addIngredient(new Ingredient(bi, new BigDecimal(8), eachUom));
        obi = bir.findByDescription("Arugala, baby, chopped");
        bi = obi.get();
        tacosRecipe.addIngredient(new Ingredient(bi, new BigDecimal(3), cupsUom));
        obi = bir.findByDescription("Avacado, sliced");
        bi = obi.get();
        tacosRecipe.addIngredient(new Ingredient(bi, new BigDecimal(2), eachUom));
        obi = bir.findByDescription("Radishes, thinly sliced");
        bi = obi.get();
        tacosRecipe.addIngredient(new Ingredient(bi, new BigDecimal(4), eachUom));
        obi = bir.findByDescription("Tomato, cherry tomatoes, halved");
        bi = obi.get();
        tacosRecipe.addIngredient(new Ingredient(bi, new BigDecimal(".5"), pintUom));
        obi = bir.findByDescription("Onion, red, thinly sliced");
        bi = obi.get();
        tacosRecipe.addIngredient(new Ingredient(bi, new BigDecimal(".25"), eachUom));
        obi = bir.findByDescription("Cilantro, roughly chopped");
        bi = obi.get();
        tacosRecipe.addIngredient(new Ingredient(bi, new BigDecimal(4), eachUom));
        obi = bir.findByDescription("Sour cream, thinned with milk");
        bi = obi.get();
        tacosRecipe.addIngredient(new Ingredient(bi, new BigDecimal(4), cupsUom));
        obi = bir.findByDescription("Lime, cut into wedges");
        bi = obi.get();
        tacosRecipe.addIngredient(new Ingredient(bi, new BigDecimal(4), eachUom));

        tacosRecipe.getCategories().add(americanCategory);
        tacosRecipe.getCategories().add(mexicanCategory);
        tacosRecipe.setUrl("http://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
        tacosRecipe.setServings(4);
        tacosRecipe.setSource("Simply Recipes");

        recipes.add(tacosRecipe);
        return recipes;
    }

    private void fillBasicIngredientRepository() {
        log.debug("entered fillBasicIngredientRepo");
        String[] basicIngredStr = {
                "Ancho Chili Powder",
                "Arugala, baby, chopped",
                "Avacado, ripe",
                "Avacado, sliced",
                "Chicken, boneless thighs",
                "Cilantro, roughly chopped",
                "Cilantro",
                "Cumin, dried",
                "Garlic, chopped clove",
                "Lemon juice, fresh",
                "Lime, cut into wedges",
                "Lime juice, fresh",
                "Olive oil",
                "Onion, green, thinly sliced",
                "Onion, red, minced",
                "Onion, red, thinly sliced",
                "Orange juice, fresh squeezed",
                "Orange zest, grated fine",
                "Oregano, dried",
                "Oregano, fresh",
                "Pepper, black, freshly ground",
                "Radishes, thinly sliced",
                "Salt",
                "Salt, Kosher",
                "Serrano chiles, stems and seeds removed, minced",
                "Sour cream",
                "Sour cream, thinned with milk",
                "Sugar",
                "Tomato, cherry tomatoes, halved",
                "Tomato, ripe, seeds and pulp removed, chopped",
                "Tortilla, small corn",
        };
        for (String bi : basicIngredStr) {
            baseIngredientRepository.save(new BaseIngredient(bi));
        }
    }

    private void fillUomRepository() {
        log.debug("entered fillUomRepo");
        UnitOfMeasure uom;

        uom = new UnitOfMeasure();
        uom.setDescription("Each");
        unitOfMeasureRepository.save(uom);
        uom = new UnitOfMeasure();
        uom.setDescription("Teaspoon");
        unitOfMeasureRepository.save(uom);
        uom = new UnitOfMeasure();
        uom.setDescription("Tablespoon");
        unitOfMeasureRepository.save(uom);
        uom = new UnitOfMeasure();
        uom.setDescription("Cup");
        unitOfMeasureRepository.save(uom);
        uom = new UnitOfMeasure();
        uom.setDescription("Pinch");
        unitOfMeasureRepository.save(uom);
        uom = new UnitOfMeasure();
        uom.setDescription("Ounce");
        unitOfMeasureRepository.save(uom);
        uom = new UnitOfMeasure();
        uom.setDescription("Dash");
        unitOfMeasureRepository.save(uom);
        uom = new UnitOfMeasure();
        uom.setDescription("Pint");
        unitOfMeasureRepository.save(uom);
    }

    private void fillCategoryRepository() {
        log.debug("entered fillCategoryRepo");
        Category cat = new Category();
        cat.setDescription("American");
        categoryRepository.save(cat);
        cat = new Category();
        cat.setDescription("Italian");
        categoryRepository.save(cat);
        cat = new Category();
        cat.setDescription("Mexican");
        categoryRepository.save(cat);
        cat = new Category();
        cat.setDescription("Fast Food");
        categoryRepository.save(cat);
    }


}
