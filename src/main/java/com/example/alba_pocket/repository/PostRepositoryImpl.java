package com.example.alba_pocket.repository;


import com.example.alba_pocket.dto.CommentResponseDto;
import com.example.alba_pocket.dto.MypageCommentResponseDto;
import com.example.alba_pocket.dto.PostResponseDto;
import com.example.alba_pocket.entity.Post;
import com.example.alba_pocket.entity.User;
import com.example.alba_pocket.entity.Likes;
import com.example.alba_pocket.entity.Comment;
import com.example.alba_pocket.model.PostSearchKeyword;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.alba_pocket.entity.QComment.comment1;
import static com.example.alba_pocket.entity.QPost.post;
import static com.example.alba_pocket.entity.QLikes.likes;



@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostCustomRepository{

    private final JPAQueryFactory queryFactory;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;

    private final CommentLikesRepository commentLikesRepository;



//    public  List<Post> search(PostSearchKeyword keyword)  {
//        return queryFactory
//                .selectFrom(post)
//                .where(      //where절의 특징으로 콤마(,)를 사용하면 and 조건으로 처리 된다. 만약 null이면 해당 조건은 제외 된다.
//                        titleContains(keyword.getKeyword()).or(contentContains(keyword.getKeyword()))
//                )
//                .fetch();
//    }

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
        List<Post> result = queryFactory
                .selectFrom(post)
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        List<PostResponseDto> postList = new ArrayList<>();
        for (Post findPost : result) {
            boolean isLike = false;
            if (user != null) {
                isLike = likesRepository.existsByUserIdAndPostId(user.getId(), findPost.getId());
            }
            int likeCount = likesRepository.countByPostId(findPost.getId());
            int commentCount = commentRepository.countByPostId(findPost.getId());
            postList.add(new PostResponseDto(findPost, isLike, likeCount, commentCount));
        }
        boolean hasNext = false;
        if (result.size() > pageable.getPageSize()) {
            postList.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(postList, pageable, hasNext);
    }

    @Override
    public Slice<PostResponseDto> scrollCategoryPost(Pageable pageable, User user, String category) {
        List<Post> result = queryFactory
                .selectFrom(post)
                .where(post.category.eq(category))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        List<PostResponseDto> postList = new ArrayList<>();
        for (Post findPost : result) {
            boolean isLike = false;
            if (user != null) {
                isLike = likesRepository.existsByUserIdAndPostId(user.getId(), findPost.getId());
            }
            int likeCount = likesRepository.countByPostId(findPost.getId());
            int commentCount = commentRepository.countByPostId(findPost.getId());
            postList.add(new PostResponseDto(findPost, isLike, likeCount, commentCount));
        }
        boolean hasNext = false;
        if (result.size() > pageable.getPageSize()) {
            postList.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(postList, pageable, hasNext);
    }

    @Override
    public Page<PostResponseDto> searchPage(PostSearchKeyword keyword, Pageable pageable, User user) {
        List<Post> content = queryFactory // 페이징을 사용한 데이터 조회
                .selectFrom(post)
                .where(
                        titleContains(keyword.getKeyword()).or(contentContains(keyword.getKeyword()))
                )
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset()) //페이지 offset(0부터 시작)
                .limit(pageable.getPageSize()) //페이지 limit(페이지 사이즈)
                .fetch();

        List<PostResponseDto> postList = new ArrayList<>();
        for ( Post post : content) {
            boolean isLike = false;
            if(user!=null)
                isLike = likesRepository.existsByUserIdAndPostId(user.getId(),post.getId());
            int likeCount = likesRepository.countByPostId(post.getId());
            int commentCount = commentRepository.countByPostId(post.getId());
            postList.add(new PostResponseDto(post, isLike, likeCount, commentCount));
        }

        Long count = queryFactory //count 조회
                .select(post.count())
                .from(post)
                .where(
                        titleContains(keyword.getKeyword()).or(contentContains(keyword.getKeyword()))
                )
                .fetchOne();

        return new PageImpl<>(postList, pageable, count); //페이징과 관련된 정보 반환
    }

    public Page<PostResponseDto> myLikePostPage(User user, Pageable pageable) {
        List<Long>  likePosts= queryFactory
                .select(likes.post.id)
                .from(likes)
                .where(
                        likes.userId.eq(user.getId())
                )
                .orderBy(likes.id.desc())
                .fetch();

        List<Post> userLikePosts = queryFactory
                .selectFrom(post)
                .where(
                        post.id.in(likePosts)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<PostResponseDto> postList = new ArrayList<>();
        for ( Post post : userLikePosts) {
        int likeCount = likesRepository.countByPostId(post.getId());
        int commentCount = commentRepository.countByPostId(post.getId());
        postList.add(new PostResponseDto(post, true, likeCount, commentCount));
        }

        Long count = queryFactory //count 조회
                .select(post.count())
                .from(post)
                .where(
                       post.id.in(likePosts)
                )
                .fetchOne();
        return new PageImpl<>(postList, pageable, count);
    }


    public Page<MypageCommentResponseDto> myCommentPostPage(User user, Pageable pageable) {
        List<Comment> userCommentPosts = queryFactory
                .selectFrom(comment1)
                .where(
                        comment1.user.id.eq(user.getId())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<MypageCommentResponseDto> commentList = new ArrayList<>();
        for ( Comment comment : userCommentPosts) {
            int likeCount = commentLikesRepository.countByCommentId(comment.getId());
            boolean isLikeComment = commentLikesRepository.existsByUserIdAndCommentId(user.getId(), comment.getId());
            commentList.add(new MypageCommentResponseDto(comment, isLikeComment,likeCount) );
        }

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
