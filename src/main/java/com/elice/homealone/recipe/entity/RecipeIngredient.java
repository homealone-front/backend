package com.elice.homealone.recipe.entity;

import com.elice.homealone.common.BaseEntity;
import com.elice.homealone.recipe.dto.RecipeIngredientDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RecipeIngredient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;
    @Column
    private String quantity;
    @Column
    private String unit;
    @Column
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    @Setter
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    @Setter
    private Ingredient ingredient;

    @Builder
    public RecipeIngredient(String name, String quantity, String unit, String note) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.note = note;
    }

    public RecipeIngredientDto toDto() {
        return RecipeIngredientDto.builder()
            .id(this.id)
            .name(this.name)
            .quantity(this.quantity)
            .unit(this.unit)
            .note(this.note)
            .build();
    }
}
