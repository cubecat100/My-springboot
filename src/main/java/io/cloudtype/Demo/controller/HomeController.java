package io.cloudtype.Demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.cloudtype.Demo.service.GuestNicknameService;

@Controller
public class HomeController
{
    private final GuestNicknameService guestNicknameService;

    public HomeController(GuestNicknameService guestNicknameService)
    {
        this.guestNicknameService = guestNicknameService;
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model)
    {
        String guestNickname = (String) session.getAttribute("guestNickname");

        if (guestNickname == null || guestNickname.isBlank() == true)
        {
            guestNickname = guestNicknameService.createGuestNickname();
            session.setAttribute("guestNickname", guestNickname);
        }

        model.addAttribute("guestNickname", guestNickname);

        return "index";
    }
}
