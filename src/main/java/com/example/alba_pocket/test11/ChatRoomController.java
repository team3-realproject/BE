//package com.example.alba_pocket.test11;
//
//import com.example.alba_pocket.dto.RoomIdResponseDto;
//import com.example.alba_pocket.entity.ChatRoom;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@Controller
//@RequestMapping("/chat")
//public class ChatRoomController {
//
//    private final ChatRoomRepository chatRoomRepository;
//    //채팅방목록 페이지 뷰로이동
//    @GetMapping("/room")
//    public String rooms(Model model) {
//        return "chat/room";
//    }
//    //채팅방리스트 겟요청
//    @GetMapping("/rooms")
//    @ResponseBody
//    public List<RoomIdResponseDto> room() {
//        return chatRoomRepository.findAllRoom();
//    }
//    //채팅방생성
//    @PostMapping("/room")
//    @ResponseBody
//    public ChatRoom createRoom(@RequestParam String name) {
//        return chatRoomRepository.createChatRoom(name);
//    }
//
//    //채팅방입장겟요청
//    @GetMapping("/room/enter/{roomId}")
//    public String roomDetail(Model model, @PathVariable String roomId) {
//        model.addAttribute("roomId", roomId);
//        return "chat/roomdetail";
//    }
//
//    @GetMapping("/room/{roomId}")
//    @ResponseBody
//    public ChatRoom roomInfo(@PathVariable String roomId) {
//        return chatRoomRepository.findRoomById(roomId);
//    }
//}
