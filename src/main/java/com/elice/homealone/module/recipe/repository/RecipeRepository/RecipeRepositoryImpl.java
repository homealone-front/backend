package com.elice.homealone.module.recipe.repository.RecipeRepository;

import com.elice.homealone.module.recipe.entity.QRecipe;
import com.elice.homealone.module.recipe.entity.Recipe;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecipeRepositoryImpl implements RecipeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QRecipe qRecipe = QRecipe.recipe;

    @Override
    public List<Recipe> findRecipes(Pageable pageable, Long memberId, String title,
                                    String description, List<String> tags) {

        // 레시피 엔티티를 선택하고 where을 통해 검색 조건을 적용하여 레시피 리스트를 가져옴
        return jpaQueryFactory
            .selectFrom(qRecipe)
            .where(
                containsTitle(title),
                containsDescription(description),
                containsMemberId(memberId)
            )
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    private BooleanExpression containsTitle(String title) {
        if(title == null) {
            return null;
        }
        return QRecipe.recipe.title.contains(title);
    }

    private BooleanExpression containsDescription(String description) {
        if(description == null) {
            return null;
        }
        return QRecipe.recipe.description.contains(description);
    }

    private BooleanExpression containsMemberId(Long memberId) {
        if(memberId == null) {
            return null;
        }
        return QRecipe.recipe.member.id.eq(memberId);
    }

    // 정렬을 위한 메소드 (공통으로 뺴야하지 않을까?)
    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        for(Sort.Order order : sort) {
            OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(
                order.isAscending() ? Order.ASC : Order.DESC,
                Expressions.stringPath(order.getProperty())
            );
            orderSpecifiers.add(orderSpecifier);
        }
        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }

    public Long countRecipes(
        Long memberId,
        String title,
        String description,
        List<String> tags) {
        return jpaQueryFactory
            .select(qRecipe.count())
            .from(qRecipe)
            .where(
                containsTitle(title),
                containsDescription(description),
                containsMemberId(memberId)
            )
            .fetchOne();
    }
    // TODO : userId와 tags에 대한 처리 로직 추가 필요
}