package com.Lee.bookstore_backend.chatRoom.controller;

import java.security.Principal;
import com.Lee.bookstore_backend.chatRoom.model.MessageBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

/**
 * 消息控制器
 *
 * @author mydlq
 */
@Controller
public class MessageController {

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    /**
     * 点对点发送消息，将消息发送到指定用户
     */
    @MessageMapping("/test")
    public void sendUserMessage(Principal principal, MessageBody messageBody) {
        messageBody.setFrom(principal.getName());
        simpMessageSendingOperations.convertAndSendToUser(messageBody.getTargetUser(), messageBody.getDestination(), messageBody);
    }

}
