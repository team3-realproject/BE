package com.example.alba_pocket.repository;

import com.example.alba_pocket.dto.PostReadResponseDto;
import com.example.alba_pocket.dto.QPostReadResponseDto;
import com.example.alba_pocket.entity.Post;

import com.example.alba_pocket.entity.QPost;
import com.example.alba_pocket.model.PostSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostCustomRepository {

    private final JPAQueryFactory queryFactory;

    public List<PostReadResponseDto> searchByWhere(PostSearchCondition condition)  {
        return queryFactory
                .select(new QPostReadResponseDto(
                        post.title,
                        post.content))
                .from(post)
                .where(      //where절의 특징으로 콤마(,)를 사용하면 and 조건으로 처리 된다. 만약 null이면 해당 조건은 제외 된다.
                        titleEq(condition.getTitle()),
                        contentEq(condition.getContent())
                )
                .fetch();
    }

    //  BooleanExpression은 and, or 을 조합해서 새로운 BooleanExpression을 만들 수 있다.
   //  또한, 결과 값이 null일 경우 무시하기 때문에 npe를 방지할 수 있다
    private BooleanExpression titleEq(String title) { //title의 값이 존재하면 조건 추가, null or 빈문자("")일 경우 null 반환
        return StringUtils.hasText(title) ? post.title.eq(title) : null;
    }
    private BooleanExpression contentEq(String content) {
        return StringUtils.hasText(content) ? post.content.eq(content) : null;
    }
}
