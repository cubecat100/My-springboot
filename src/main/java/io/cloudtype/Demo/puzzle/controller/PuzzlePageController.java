package io.cloudtype.Demo.puzzle.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.cloudtype.Demo.puzzle.model.PuzzleRoom;
import io.cloudtype.Demo.puzzle.model.PuzzleType;
import io.cloudtype.Demo.puzzle.service.PuzzleLobbyService;

@Controller
public class PuzzlePageController
{
    private final PuzzleLobbyService puzzleLobbyService;

    public PuzzlePageController(PuzzleLobbyService puzzleLobbyService)
    {
        this.puzzleLobbyService = puzzleLobbyService;
    }

    @GetMapping("/puzzles")
    public String puzzleTypes(HttpSession session, Model model)
    {
        String guestNickname = (String) session.getAttribute("guestNickname");

        if (hasNoGuestNickname(guestNickname))
        {
            return "redirect:/";
        }

        model.addAttribute("guestNickname", guestNickname);
        model.addAttribute("puzzleTypes", puzzleLobbyService.findAllTypes());

        return "puzzle/types";
    }

    @GetMapping("/puzzles/{puzzleTypeCode}")
    public String puzzleRooms(@PathVariable String puzzleTypeCode, HttpSession session, Model model, RedirectAttributes redirectAttributes)
    {
        String guestNickname = (String) session.getAttribute("guestNickname");

        if (hasNoGuestNickname(guestNickname))
        {
            return "redirect:/";
        }

        PuzzleType puzzleType = puzzleLobbyService.findType(puzzleTypeCode);

        if (puzzleType == null)
        {
            redirectAttributes.addFlashAttribute("roomErrorMessage", "선택한 퍼즐 종류를 찾을 수 없습니다.");
            return "redirect:/puzzles";
        }

        model.addAttribute("guestNickname", guestNickname);
        model.addAttribute("puzzleType", puzzleType);
        model.addAttribute("roomList", puzzleLobbyService.findRoomsByType(puzzleTypeCode));

        return "puzzle/rooms";
    }

    @PostMapping("/puzzles/{puzzleTypeCode}/rooms")
    public String createPuzzleRoom(@PathVariable String puzzleTypeCode, HttpSession session, RedirectAttributes redirectAttributes)
    {
        String guestNickname = (String) session.getAttribute("guestNickname");

        if (hasNoGuestNickname(guestNickname))
        {
            return "redirect:/";
        }

        PuzzleRoom room = puzzleLobbyService.createRoom(puzzleTypeCode);

        if (room == null)
        {
            redirectAttributes.addFlashAttribute("roomErrorMessage", "이 퍼즐 종류에서는 방을 만들 수 없습니다.");
            return "redirect:/puzzles";
        }

        redirectAttributes.addFlashAttribute("roomCreatedMessage", "새 퍼즐 풀이방이 준비되었습니다.");

        return "redirect:/puzzles/" + puzzleTypeCode + "/rooms/" + room.getRoomId();
    }

    @GetMapping("/puzzles/{puzzleTypeCode}/rooms/{roomId}")
    public String puzzleRoom(@PathVariable String puzzleTypeCode, @PathVariable String roomId, HttpSession session, Model model,
        RedirectAttributes redirectAttributes)
    {
        String guestNickname = (String) session.getAttribute("guestNickname");

        if (hasNoGuestNickname(guestNickname))
        {
            return "redirect:/";
        }

        PuzzleType puzzleType = puzzleLobbyService.findType(puzzleTypeCode);

        if (puzzleType == null)
        {
            redirectAttributes.addFlashAttribute("roomErrorMessage", "선택한 퍼즐 종류를 찾을 수 없습니다.");
            return "redirect:/puzzles";
        }

        PuzzleRoom room = puzzleLobbyService.findRoom(puzzleTypeCode, roomId);

        if (room == null)
        {
            redirectAttributes.addFlashAttribute("roomErrorMessage", "요청한 퍼즐 풀이방을 찾을 수 없습니다.");
            return "redirect:/puzzles/" + puzzleTypeCode;
        }

        List<String> suggestedRoles = puzzleLobbyService.buildSuggestedRoles(puzzleType);

        model.addAttribute("guestNickname", guestNickname);
        model.addAttribute("puzzleType", puzzleType);
        model.addAttribute("room", room);
        model.addAttribute("suggestedRoles", suggestedRoles);

        return "puzzle/room";
    }

    private boolean hasNoGuestNickname(String guestNickname)
    {
        return guestNickname == null || guestNickname.trim().isEmpty();
    }
}
