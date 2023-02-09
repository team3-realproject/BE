package com.example.alba_pocket.repository;

import com.example.alba_pocket.dto.CommentResponseDto;
import com.example.alba_pocket.dto.MypageCommentResponseDto;
import com.example.alba_pocket.entity.Comment;
import com.example.alba_pocket.entity.QUser;
import com.example.alba_pocket.entity.User;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import static com.example.alba_pocket.entity.QComment.comment1;
import static com.example.alba_pocket.entity.QCommentLikes.commentLikes;
import static com.example.alba_pocket.entity.QPost.post;
import static com.example.alba_pocket.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentResponseDto> commentList(User user, Long postId) {
        List<CommentResponseDto> commentList=queryFactory
                .select(Projections.fields(
                        CommentResponseDto.class,
                        comment1.id.as("commentId"),
                        comment1.comment,
                        QUser.user.userId,
                        QUser.user.nickname,
                        ExpressionUtils.as
                        (JPAExpressions
                                .select(commentLikes.count())
                                .from(commentLikes)
                                .where(commentLikes.comment.id.eq(comment1.id)),
                        "commentLikeNum"),
                        ExpressionUtils.as
                        (JPAExpressions
                                .select(commentLikes.id)
                                .from(commentLikes)
                                .where(commentLikes.userId.eq(user.getId())
                                        .and(commentLikes.comment.id.eq(comment1.id)))
                                .exists(),
                                "isLikeComment"),
                        comment1.createdAt.as("createAt"),
                        QUser.user.profileImage)
                ).from(comment1)
                .join(QUser.user)
                .on(comment1.user.id.eq(QUser.user.id))
                .where(comment1.post.id.eq(postId))
                .orderBy(comment1.createdAt.asc())
                .fetch();
        return commentList;
    }

    @Override
    public Page<MypageCommentResponseDto> myCommentPostPage(User user, Pageable pageable) {
        List<MypageCommentResponseDto> commentList = queryFactory
                .select(Projections.fields(
                        MypageCommentResponseDto.class,
                        post.title,
                        post.id.as("postId"),
                        comment1.id.as("commentId"),
                        comment1.comment,
                        QUser.user.userId,
                        QUser.user.nickname,
                        ExpressionUtils.as
                                (JPAExpressions
                                                .select(commentLikes.count())
                                                .from(commentLikes)
                                                .where(commentLikes.comment.id.eq(comment1.id)),
                                        "commentLikeNum"),
                        ExpressionUtils.as
                                (JPAExpressions
                                                .select(commentLikes.id)
                                                .from(commentLikes)
                                                .where(commentLikes.userId.eq(user.getId())
                                                        .and(commentLikes.comment.id.eq(comment1.id)))
                                                .exists(),
                                        "isLikeComment"),
                        comment1.createdAt.as("createAt"),
                        QUser.user.profileImage
                ))
                .from(comment1)
                .join(QUser.user)
                .on(comment1.user.id.eq(QUser.user.id))
                .join(post)
                .on(comment1.post.id.eq(post.id))
                .where(comment1.user.id.eq(user.getId()))
                .orderBy(comment1.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        Long count = queryFactory //count 조회
                .select(comment1.count())
                .from(comment1)
                .where(
                        comment1.user.id.eq(user.getId())
                )
                .fetchOne();
        return new PageImpl<>(commentList, pageable, count);
    }
}
