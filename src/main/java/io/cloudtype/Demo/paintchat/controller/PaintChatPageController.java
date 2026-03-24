package io.cloudtype.Demo.paintchat.controller;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.cloudtype.Demo.paintchat.model.PaintChatRoom;
import io.cloudtype.Demo.paintchat.service.PaintChatRoomService;
import io.cloudtype.Demo.paintchat.service.PaintChatStrokeService;

@Controller
public class PaintChatPageController
{
    private final PaintChatRoomService paintChatRoomService;
    private final PaintChatStrokeService paintChatStrokeService;

    public PaintChatPageController(PaintChatRoomService paintChatRoomService, PaintChatStrokeService paintChatStrokeService)
    {
        this.paintChatRoomService = paintChatRoomService;
        this.paintChatStrokeService = paintChatStrokeService;
    }

    @GetMapping("/paintchat/list")
    public String roomList(HttpSession session, Model model)
    {
        String guestNickname = (String) session.getAttribute("guestNickname");

        if (hasNoGuestNickname(guestNickname))
        {
            return "redirect:/";
        }

        model.addAttribute("guestNickname", guestNickname);
        model.addAttribute("roomList", paintChatRoomService.findAll());
        model.addAttribute("dailyCreationLimit", paintChatRoomService.getDailyRoomCreationLimit());
        model.addAttribute("dailyLimitReached", paintChatRoomService.hasReachedDailyCreationLimit());

        return "paintchat/roomlist";
    }

    @PostMapping("/paintchat/rooms")
    public String createRoom(HttpSession session, RedirectAttributes redirectAttributes)
    {
        String guestNickname = (String) session.getAttribute("guestNickname");

        if (hasNoGuestNickname(guestNickname))
        {
            return "redirect:/";
        }

        PaintChatRoom room = paintChatRoomService.createRoom();

        if (room == null)
        {
            redirectAttributes.addFlashAttribute("roomErrorMessage", "오늘 생성 가능한 채팅방 수를 모두 사용했습니다.");
            return "redirect:/paintchat/list";
        }

        redirectAttributes.addFlashAttribute("roomCreatedMessage", "채팅방이 생성되었습니다.");

        return "redirect:/paintchat/" + room.getRoomId();
    }

    @GetMapping("/paintchat/{roomId}")
    public String room(@PathVariable String roomId, HttpSession session, Model model, RedirectAttributes redirectAttributes)
    {
        String guestNickname = (String) session.getAttribute("guestNickname");

        if (hasNoGuestNickname(guestNickname))
        {
            return "redirect:/";
        }

        if (isInvalidRoomId(roomId))
        {
            redirectAttributes.addFlashAttribute("roomErrorMessage", "유효하지 않은 채팅방 ID입니다.");
            return "redirect:/paintchat/list";
        }

        PaintChatRoom room = paintChatRoomService.findByRoomId(roomId);

        if (room == null)
        {
            redirectAttributes.addFlashAttribute("roomErrorMessage", "요청한 채팅방을 찾을 수 없습니다.");
            return "redirect:/paintchat/list";
        }

        if (room.isEnterEnabled() == false)
        {
            redirectAttributes.addFlashAttribute("roomErrorMessage", "현재 입장할 수 없는 채팅방입니다.");
            return "redirect:/paintchat/list";
        }

        model.addAttribute("guestNickname", guestNickname);
        model.addAttribute("roomId", room.getRoomId());
        model.addAttribute("roomName", room.getRoomName());
        model.addAttribute("roomDescription", room.getRoomDescription());
        model.addAttribute("drawEnabled", room.isDrawEnabled());
        model.addAttribute("drawHistory", paintChatStrokeService.findDrawHistory(room.getRoomId()));
        model.addAttribute("maxStrokesPerRoom", paintChatStrokeService.getMaxStrokesPerRoom());

        return "paintchat/room";
    }

    private boolean hasNoGuestNickname(String guestNickname)
    {
        return guestNickname == null || guestNickname.trim().isEmpty();
    }

    private boolean isInvalidRoomId(String roomId)
    {
        try
        {
            UUID.fromString(roomId);
            return false;
        }
        catch (IllegalArgumentException exception)
        {
            return true;
        }
    }
}
