package com.elice.homealone.recipe.service;

import com.elice.homealone.recipe.entity.Recipe;
import com.elice.homealone.recipe.repository.RecipeIngredientRepository.RecipeIngredientRepository;
import com.elice.homealone.recipe.dto.RecipeIngredientDto;
import com.elice.homealone.recipe.entity.RecipeIngredient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientService ingredientService;

    public RecipeIngredient createRecipeIngredient(RecipeIngredientDto recipeIngredientDto) {
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
            .name(recipeIngredientDto.getName())
            .quantity(recipeIngredientDto.getQuantity())
            .unit(recipeIngredientDto.getUnit())
            .note(recipeIngredientDto.getNote())
            .build();

        recipeIngredientRepository.save(recipeIngredient);
        ingredientService.addIngredient(recipeIngredient);
        return recipeIngredient;
    }

    public void deleteRecipeIngredient(RecipeIngredient ingredient) {
        recipeIngredientRepository.delete(ingredient);
    }

    public void deleteRecipeIngredientByRecipe(Recipe recipe) {
        recipeIngredientRepository.deleteByRecipe(recipe);
    }

    public void addRecipeIngredients(Recipe recipe, List<RecipeIngredientDto> recipeIngredientDtos) {
        if(recipeIngredientDtos != null) {
            for(RecipeIngredientDto ingredientDto : recipeIngredientDtos) {
                recipe.addIngredients(createRecipeIngredient(ingredientDto));
            }
        }
    }
}
