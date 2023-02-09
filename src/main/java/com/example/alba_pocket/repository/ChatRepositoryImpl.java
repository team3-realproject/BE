package com.example.alba_pocket.repository;

import com.example.alba_pocket.dto.ChatRoomListResponseDto;
import com.example.alba_pocket.entity.QUser;
import com.example.alba_pocket.entity.User;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.alba_pocket.entity.QChatMessage.chatMessage;
import static com.example.alba_pocket.entity.QChatRoom.chatRoom;


@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatCustomRepository{
    private final JPAQueryFactory queryFactory;



    @Override
    public List<ChatRoomListResponseDto> chatRoomList(User user) {
        List<ChatRoomListResponseDto> chatRoomList = queryFactory
                .select(Projections.fields(
                        ChatRoomListResponseDto.class,
                        QUser.user.nickname,
                        QUser.user.profileImage,
                        chatMessage.message,
                        chatMessage.createdAt.max().as("createdAt"))
                ).from(chatRoom)
                .leftJoin(chatMessage)
                .on(chatRoom.roomId.eq(chatMessage.roomId))
                .leftJoin(chatMessage)
                .on(chatRoom.toUser.id.eq(chatMessage.user.id))
                .where(chatRoom.user.id.eq(user.getId()))
                .groupBy(QUser.user.nickname)
                .fetch();
        return chatRoomList;
    }


    @Override
    public List<String> getRoomId (Long userId, Long userId2) {
        List<String> getRoomId = queryFactory
                .select(chatRoom.roomId)
                .from(chatRoom)
                .where(chatRoom.user.id.eq(userId)
                        .or(chatRoom.user.id.eq(userId2)))
                .groupBy(chatRoom.roomId)
                .having(chatRoom.roomId.count().gt(1))
                .fetch();
        return getRoomId;
    }
}
