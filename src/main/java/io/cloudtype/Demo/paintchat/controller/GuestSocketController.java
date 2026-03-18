package io.cloudtype.Demo.paintchat.controller;

import javax.servlet.http.HttpSession;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.cloudtype.Demo.paintchat.websocket.GuestMessage;

@Controller
public class GuestSocketController
{
    @MessageMapping("/guest.enter")
    @SendTo("/topic/guest")
    public GuestMessage enter(GuestMessage message)
    {
        GuestMessage response = new GuestMessage();
        response.setNickname(message.getNickname());
        response.setContent(message.getNickname() + " 님이 입장했습니다.");

        return response;
    }
    
    @GetMapping("/wstest")
    public String WebsocketGuestMessageTest(HttpSession session, Model model)
    {
        String guestNickname = (String) session.getAttribute("guestNickname");

        if (guestNickname == null || guestNickname.isBlank() == true)
        {
        	return "redirect:/";
        }

        model.addAttribute("guestNickname", guestNickname);
    	return "test/chat-test";
    }
}
