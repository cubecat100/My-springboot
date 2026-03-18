package io.cloudtype.Demo.paintchat.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.cloudtype.Demo.paintchat.domain.GuestMemo;
import io.cloudtype.Demo.paintchat.repository.GuestMemoRepository;
import io.cloudtype.Demo.service.GuestNicknameService;

@Controller
public class DbTestController {
	private final GuestMemoRepository guestMemoRepository;
	private final GuestNicknameService guestNicknameService;

	public DbTestController(GuestMemoRepository guestMemoRepository, GuestNicknameService guestNicknameService) {
		this.guestMemoRepository = guestMemoRepository;
		this.guestNicknameService = guestNicknameService;
	}

	@GetMapping("/db-test")
	public String dbTest(HttpSession session, Model model) {
		String guestNickname = (String) session.getAttribute("guestNickname");

		if (guestNickname == null || guestNickname.isBlank() == true) {
			guestNickname = guestNicknameService.createGuestNickname();
			session.setAttribute("guestNickname", guestNickname);
		}

		model.addAttribute("guestNickname", guestNickname);
		model.addAttribute("memoList", guestMemoRepository.findAllByOrderByIdDesc());

		return "test/db-test";
	}

	@PostMapping("/db-test/save")
	public String saveMemo(@RequestParam("content") String content, HttpSession session) {
		String guestNickname = (String) session.getAttribute("guestNickname");

		if (guestNickname == null || guestNickname.isBlank() == true) {
			guestNickname = guestNicknameService.createGuestNickname();
			session.setAttribute("guestNickname", guestNickname);
		}

		GuestMemo memo = new GuestMemo();
		memo.setGuestNickname(guestNickname);
		memo.setContent(content);
		memo.setCreatedAt(LocalDateTime.now());

		guestMemoRepository.save(memo);

		return "redirect:/db-test";
	}
}
