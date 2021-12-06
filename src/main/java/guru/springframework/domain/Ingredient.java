package guru.springframework.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by jt on 6/13/17.
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = {"recipe"})
@Entity
public class Ingredient {
    /*
        TODO:  BaseIngredient should only have id, description
            Ingredient have a BaseIngredient along with UofM and amount Fields.
        This would allow us to have a large built-in set of generic indredients

     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private String extendedDescription;

    @OneToOne(fetch = FetchType.EAGER)
    private BaseIngredient baseIngredient;

    private BigDecimal amount;

    @OneToOne(fetch = FetchType.EAGER)
    private UnitOfMeasure uom;

    @ManyToOne
    private Recipe recipe;

    public Ingredient() {
    }

        public Ingredient(BaseIngredient baseIngredient, BigDecimal amount, UnitOfMeasure uom) {
        this.baseIngredient = baseIngredient;
        this.amount = amount;
        this.uom = uom;
    }

//    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom) {
//        this.description = description;
//        this.amount = amount;
//        this.uom = uom;
//    }
//
//    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom, Recipe recipe) {
//        this.description = description;
//        this.amount = amount;
//        this.uom = uom;
//        this.recipe = recipe;
//    }

}
