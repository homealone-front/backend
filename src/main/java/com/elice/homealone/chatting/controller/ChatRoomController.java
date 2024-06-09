package com.elice.homealone.chatting.controller;

import com.elice.homealone.chatting.entity.ChatDto;
import com.elice.homealone.chatting.entity.ChatMessage;
import com.elice.homealone.chatting.entity.Chatting;
import com.elice.homealone.chatting.entity.MessageDto;
import com.elice.homealone.chatting.repository.ChatRoomRepository;
import com.elice.homealone.chatting.service.ChatRoomService;
import com.elice.homealone.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin
@RestController
@Tag(name = "ChatRoomController", description = "채팅방 관리 API")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;


    //회원의 모든 채팅방 목록 반환
    @Operation(summary = "회원 채팅방 목록 조회")
    @GetMapping("/chattings")
    public ResponseEntity<List<ChatDto>> chattingRooms(@AuthenticationPrincipal Member member) {

        return ResponseEntity.ok().body(chatRoomService.findChatrooms(member));

    }

    //선택 채팅방 조회
    @Operation(summary = "채팅방 데이터 조회")
    @GetMapping("/chatting/{chatroomId}")
    public ResponseEntity<ChatDto> chatroomInfo(@AuthenticationPrincipal Member member, @PathVariable Long chatroomId) {

        return ResponseEntity.ok().body(chatRoomService.findChatList(member, chatroomId));
    }

    //채팅방 생성
    @Operation(summary = "중고거래 게시판에서 채팅방 개설")
    @PostMapping("/chatting")
    public ResponseEntity<ChatDto> makeChat(@AuthenticationPrincipal Member member, @RequestBody ChatDto chatDto) {

        return ResponseEntity.ok().body(chatRoomService.makeChat(member, chatDto));
    }

    //채팅방 삭제
    @Operation(summary = "채팅방 삭제")
    @DeleteMapping("/chatting/{chatroomId}")
    public void deleteChatroom(@AuthenticationPrincipal Member member, @PathVariable Long chatroomId) {

        chatRoomService.deleteChatroom(member, chatroomId);
    }

}
