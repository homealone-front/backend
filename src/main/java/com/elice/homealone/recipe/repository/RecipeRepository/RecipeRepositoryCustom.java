package com.elice.homealone.recipe.repository.RecipeRepository;

import com.elice.homealone.recipe.entity.Recipe;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeRepositoryCustom {
    Page<Recipe> findRecipes(
        Pageable pageable,
        String userId,
        String title,
        String description,
        List<String> tags
    );
}