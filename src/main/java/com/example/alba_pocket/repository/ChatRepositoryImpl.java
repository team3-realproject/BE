package com.example.alba_pocket.repository;

import com.example.alba_pocket.dto.ChatRoomListResponseDto;
import com.example.alba_pocket.dto.LastMessageResponseDto;
import com.example.alba_pocket.entity.Post;
import com.example.alba_pocket.entity.QUser;
import com.example.alba_pocket.entity.User;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.alba_pocket.entity.QUser.user;
import static com.example.alba_pocket.entity.QChatMessage.chatMessage;
import static com.example.alba_pocket.entity.QChatRoom.chatRoom;


@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatCustomRepository{
    private final JPAQueryFactory queryFactory;
    private final UserRepository userRepository;


    @Override
    public List<ChatRoomListResponseDto> chatRoomList(User user) {
//        List<LastMessageResponseDto> lastMessageInfo = queryFactory
//                .select(Projections.fields(
//                        LastMessageResponseDto.class,
//                                chatMessage.user.id.as("userId"),
//                                chatMessage.message,
//                                chatMessage.createdAt)
//                        ).from(chatMessage)
//                        .orderBy(chatMessage.createdAt.desc())
//                        .limit(1)
//                        .fetch();
        List<ChatRoomListResponseDto> chatRoomList = queryFactory
                .select(Projections.fields(
                        ChatRoomListResponseDto.class,
                        QUser.user.nickname,
                        QUser.user.profileImage,
                        chatMessage.message,
                        chatMessage.createdAt.max().as("createdAt"))
                ).from(chatRoom)
                .leftJoin(QUser.user)
                .on(chatRoom.toUser.id.eq(QUser.user.id))
                .leftJoin(chatMessage)
                .on(chatRoom.toUser.id.eq(chatMessage.user.id))
                .where(chatRoom.user.id.eq(user.getId()))
                .groupBy(QUser.user.nickname)
                .fetch();
        return chatRoomList;
    }
}
