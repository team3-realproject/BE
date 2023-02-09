package com.example.alba_pocket.repository;


import com.example.alba_pocket.dto.MyPageRequestDto;
import com.example.alba_pocket.dto.PostResponseDto;
import com.example.alba_pocket.entity.Comment;
import com.example.alba_pocket.entity.QUser;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.model.PostSearchKeyword;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.alba_pocket.entity.QComment.comment1;

import static com.example.alba_pocket.entity.QPost.post;
import static com.example.alba_pocket.entity.QLikes.likes;
import static com.example.alba_pocket.entity.QUser.user;




@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostCustomRepository{

    private final JPAQueryFactory queryFactory;

    //  BooleanExpression은 and, or 을 조합해서 새로운 BooleanExpression을 만들 수 있다.
   //  또한, 결과 값이 null일 경우 무시하기 때문에 npe를 방지할 수 있다
    private BooleanExpression titleContains(String title) { //title의 값이 존재하면 조건 추가, null or 빈문자("")일 경우 null 반환
        return StringUtils.hasText(title) ? post.title.contains(title) : null;
    }
    private BooleanExpression contentContains(String content) {
        return StringUtils.hasText(content) ? post.content.contains(content) : null;
    }



    @Override
    public Slice<PostResponseDto> scrollPost(Pageable pageable, User user) {

        List<PostResponseDto> postList = queryFactory
                .select(Projections.fields(
                        PostResponseDto.class,
                        post.id.as("postId"),
                        QUser.user.profileImage,
                        QUser.user.userId,
                        QUser.user.nickname,
                        post.title,
                        post.content,
                        post.imgUrl,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(likes.count())
                                        .from(likes)
                                        .where(likes.post.id.eq(post.id)),
                                "postLikeNum"
                        ),
                        ExpressionUtils.as
                                (JPAExpressions
                                                .selectFrom(likes)
                                                .where(likes.userId.eq(user.getId())
                                                        .and(likes.post.id.eq(post.id)))
                                                .exists(),
                                        "isLikePost"),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(comment1.count())
                                        .from(comment1)
                                        .where(comment1.post.id.eq(post.id)),
                                "commentCount"
                        ),
                        post.category,
                        post.createdAt.as("createAt"),
                        post.modifiedAt
                        )
                ).from(post)
                .join(QUser.user)
                .on(post.user.id.eq(QUser.user.id))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (postList.size() > pageable.getPageSize()) {
            postList.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(postList, pageable, hasNext);
    }

    @Override
    public Slice<PostResponseDto> scrollCategoryPost(Pageable pageable, User user, String category) {
        List<PostResponseDto> postList = queryFactory
                .select(Projections.fields(
                                PostResponseDto.class,
                                post.id.as("postId"),
                                QUser.user.profileImage,
                                QUser.user.userId,
                                QUser.user.nickname,
                                post.title,
                                post.content,
                                post.imgUrl,
                                ExpressionUtils.as(
                                        JPAExpressions
                                                .select(likes.count())
                                                .from(likes)
                                                .where(likes.post.id.eq(post.id)),
                                        "postLikeNum"
                                ),
                                ExpressionUtils.as
                                        (JPAExpressions
                                                        .selectFrom(likes)
                                                        .where(likes.userId.eq(user.getId())
                                                                .and(likes.post.id.eq(post.id)))
                                                        .exists(),
                                                "isLikePost"),
                                ExpressionUtils.as(
                                        JPAExpressions
                                                .select(comment1.count())
                                                .from(comment1)
                                                .where(comment1.post.id.eq(post.id)),
                                        "commentCount"
                                ),
                                post.category,
                                post.createdAt.as("createAt"),
                                post.modifiedAt
                        )
                ).from(post)
                .join(QUser.user)
                .on(post.user.id.eq(QUser.user.id))
                .where(post.category.eq(category))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (postList.size() > pageable.getPageSize()) {
            postList.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(postList, pageable, hasNext);
    }

    @Override
    public Page<PostResponseDto> searchPage(PostSearchKeyword keyword, Pageable pageable, User user) {

        List<PostResponseDto> postList = queryFactory
                .select(Projections.fields(
                                PostResponseDto.class,
                                post.id.as("postId"),
                                QUser.user.profileImage,
                                QUser.user.userId,
                                QUser.user.nickname,
                                post.title,
                                post.content,
                                post.imgUrl,
                                ExpressionUtils.as(
                                        JPAExpressions
                                                .select(likes.count())
                                                .from(likes)
                                                .where(likes.post.id.eq(post.id)),
                                        "postLikeNum"
                                ),
                                ExpressionUtils.as
                                        (JPAExpressions
                                                        .selectFrom(likes)
                                                        .where(likes.userId.eq(user.getId())
                                                                .and(likes.post.id.eq(post.id)))
                                                        .exists(),
                                                "isLikePost"),
                                ExpressionUtils.as(
                                        JPAExpressions
                                                .select(comment1.count())
                                                .from(comment1)
                                                .where(comment1.post.id.eq(post.id)),
                                        "commentCount"
                                ),
                                post.category,
                                post.createdAt.as("createAt"),
                                post.modifiedAt
                        )
                ).from(post)
                .join(QUser.user)
                .on(post.user.id.eq(QUser.user.id))
                .where(
                        titleContains(keyword.getKeyword()).or(contentContains(keyword.getKeyword()))
                )
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset()) //페이지 offset(0부터 시작)
                .limit(pageable.getPageSize()) //페이지 limit(페이지 사이즈)
                .fetch();

        Long count = queryFactory //count 조회
                .select(post.count())
                .from(post)
                .where(
                        titleContains(keyword.getKeyword()).or(contentContains(keyword.getKeyword()))
                )
                .fetchOne();

        return new PageImpl<>(postList, pageable, count); //페이징과 관련된 정보 반환
    }

    @Override
    public Page<PostResponseDto> myLikePostPage(User user, Pageable pageable) {

        List<PostResponseDto> postList = queryFactory
                .select(Projections.fields(
                                PostResponseDto.class,
                                post.id.as("postId"),
                                QUser.user.profileImage,
                                QUser.user.userId,
                                QUser.user.nickname,
                                post.title,
                                post.content,
                                post.imgUrl,
                                ExpressionUtils.as(
                                        JPAExpressions
                                                .select(likes.count())
                                                .from(likes)
                                                .where(likes.post.id.eq(post.id)),
                                        "postLikeNum"
                                ),
                                ExpressionUtils.as
                                        (JPAExpressions
                                                        .selectFrom(likes)
                                                        .where(likes.userId.eq(user.getId())
                                                                .and(likes.post.id.eq(post.id)))
                                                        .exists(),
                                                "isLikePost"),
                                ExpressionUtils.as(
                                        JPAExpressions
                                                .select(comment1.count())
                                                .from(comment1)
                                                .where(comment1.post.id.eq(post.id)),
                                        "commentCount"
                                ),
                                post.category,
                                post.createdAt.as("createAt"),
                                post.modifiedAt
                        )
                ).from(post)
                .join(QUser.user)
                .on(post.user.id.eq(QUser.user.id))
                .join(likes)
                .on(post.id.eq(likes.post.id))
                .where(
                        likes.userId.eq(user.getId())
                )
                .orderBy(likes.id.desc())
                .offset(pageable.getOffset()) //페이지 offset(0부터 시작)
                .limit(pageable.getPageSize()) //페이지 limit(페이지 사이즈)
                .fetch();

        Long count = queryFactory //count 조회
                .select(post.count())
                .from(post)
                .where(
                       post.id.in(
                               JPAExpressions
                               .select(likes.post.id)
                               .from(likes)
                               .where(likes.userId.eq(user.getId()))
                       )
                )
                .fetchOne();
        return new PageImpl<>(postList, pageable, count);
    }



    public Page<PostResponseDto> getMyPage(User user, Pageable pageable) {

        List<PostResponseDto> postList = queryFactory
                .select(Projections.fields(
                                PostResponseDto.class,
                                post.id.as("postId"),
                                QUser.user.profileImage,
                                QUser.user.userId,
                                QUser.user.nickname,
                                post.title,
                                post.content,
                                post.imgUrl,
                                ExpressionUtils.as(
                                        JPAExpressions
                                                .select(likes.count())
                                                .from(likes)
                                                .where(likes.post.id.eq(post.id)),
                                        "postLikeNum"
                                ),
                                ExpressionUtils.as
                                        (JPAExpressions
                                                        .selectFrom(likes)
                                                        .where(likes.userId.eq(user.getId())
                                                                .and(likes.post.id.eq(post.id)))
                                                        .exists(),
                                                "isLikePost"),
                                ExpressionUtils.as(
                                        JPAExpressions
                                                .select(comment1.count())
                                                .from(comment1)
                                                .where(comment1.post.id.eq(post.id)),
                                        "commentCount"
                                ),
                                post.category,
                                post.createdAt.as("createAt"),
                                post.modifiedAt
                        )
                ).from(post)
                .join(QUser.user)
                .on(post.user.id.eq(QUser.user.id))
                .where(
                        post.user.id.eq(user.getId())
                )
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset()) //페이지 offset(0부터 시작)
                .limit(pageable.getPageSize()) //페이지 limit(페이지 사이즈)
                .fetch();

        Long count = queryFactory //count 조회
                .select(post.count())
                .from(post)
                .where(
                        post.user.id.eq(user.getId())
                )
                .fetchOne();
        return new PageImpl<>(postList, pageable, count);
    }

    public PostResponseDto findPostById(User user, Long postId) {
        PostResponseDto postInfo = queryFactory
                .select(Projections.fields(
                                PostResponseDto.class,
                                post.id.as("postId"),
                                QUser.user.profileImage,
                                QUser.user.userId,
                                QUser.user.nickname,
                                post.title,
                                post.content,
                                post.imgUrl,
                                ExpressionUtils.as(
                                        JPAExpressions
                                                .select(likes.count())
                                                .from(likes)
                                                .where(likes.post.id.eq(post.id)),
                                        "postLikeNum"
                                ),
                                ExpressionUtils.as
                                        (JPAExpressions
                                                        .selectFrom(likes)
                                                        .where(likes.userId.eq(user.getId())
                                                                .and(likes.post.id.eq(post.id)))
                                                        .exists(),
                                                "isLikePost"),
                                ExpressionUtils.as(
                                        JPAExpressions
                                                .select(comment1.count())
                                                .from(comment1)
                                                .where(comment1.post.id.eq(post.id)),
                                        "commentCount"
                                ),
                                post.category,
                                post.createdAt.as("createAt"),
                                post.modifiedAt
                        )
                ).from(post)
                .join(QUser.user)
                .on(post.user.id.eq(QUser.user.id))
                .where(
                        post.id.eq(postId)
                )
                .orderBy(post.createdAt.desc())
                .fetchOne();

        return postInfo;
    }



}
