package com.zerobase.fastlms.admin.controller;


import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.admin.model.MemberInput;
import com.zerobase.fastlms.admin.model.MemberParam;
import com.zerobase.fastlms.course.controller.BaseController;
import com.zerobase.fastlms.member.entity.LoginHistory;
import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class AdminMemberController extends BaseController {

    private final MemberService memberService;

    @GetMapping("/admin/member/list.do")
    public String list(Model model, MemberParam parameter) {

        // parameter : user_id;

        parameter.init();

        List<MemberDto> members = memberService.list(parameter);

        // 기존의 memberDTO에 마지막 로그인 시각 추가.
        // 없는 경우, 현재 시간을 넣어준다.
        members.forEach(memberDto -> {
            String lastLoginDt = memberService.getLastLoginHistory(memberDto.getUserId());
            log.info("lastLoginDt : {}", lastLoginDt);
            memberDto.setLastLoginDt(lastLoginDt);
        });

        long totalCount = 0;
        if (members != null && members.size() > 0) {
            totalCount = members.get(0).getTotalCount();
        }
        String queryString = parameter.getQueryString();
        String pagerHtml = getPaperHtml(totalCount, parameter.getPageSize(), parameter.getPageIndex(), queryString);

        model.addAttribute("list", members);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/member/list";
    }

    @GetMapping("/admin/member/detail.do")
    public String detail(Model model, MemberParam parameter) {

        parameter.init();

        MemberDto member = memberService.detail(parameter.getUserId());

        // 마지막 로그인 시각 추가
        List<LoginHistory> loginHistories = memberService.getAllLoginHistoryByUserId(parameter.getUserId());
        for (LoginHistory loginHistory : loginHistories) {
            log.info("loginHistory : {}", loginHistory.toString());
        }
        Collections.reverse(loginHistories);
        model.addAttribute("member", member);
        model.addAttribute("loginHistories", loginHistories);
        return "admin/member/detail";
    }

    @PostMapping("/admin/member/status.do")
    public String status(Model model, MemberInput parameter) {


        boolean result = memberService.updateStatus(parameter.getUserId(), parameter.getUserStatus());

        return "redirect:/admin/member/detail.do?userId=" + parameter.getUserId();
    }


    @PostMapping("/admin/member/password.do")
    public String password(Model model, MemberInput parameter) {


        boolean result = memberService.updatePassword(parameter.getUserId(), parameter.getPassword());

        return "redirect:/admin/member/detail.do?userId=" + parameter.getUserId();
    }


}
