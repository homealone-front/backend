package com.elice.homealone.recipe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecipeImage is a Querydsl query type for RecipeImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecipeImage extends EntityPathBase<RecipeImage> {

    private static final long serialVersionUID = -208259079L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecipeImage recipeImage = new QRecipeImage("recipeImage");

    public final com.elice.homealone.global.common.QBaseTimeEntity _super = new com.elice.homealone.global.common.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath fileName = createString("fileName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final QRecipe recipe;

    public QRecipeImage(String variable) {
        this(RecipeImage.class, forVariable(variable), INITS);
    }

    public QRecipeImage(Path<? extends RecipeImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecipeImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecipeImage(PathMetadata metadata, PathInits inits) {
        this(RecipeImage.class, metadata, inits);
    }

    public QRecipeImage(Class<? extends RecipeImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recipe = inits.isInitialized("recipe") ? new QRecipe(forProperty("recipe"), inits.get("recipe")) : null;
    }

}
