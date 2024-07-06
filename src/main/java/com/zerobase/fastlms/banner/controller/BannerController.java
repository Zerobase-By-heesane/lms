package com.zerobase.fastlms.banner.controller;

import com.zerobase.fastlms.banner.dto.BannerDto;
import com.zerobase.fastlms.banner.model.BannerParam;
import com.zerobase.fastlms.banner.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BannerController extends BaseController {

    private final BannerService bannerService;

    @GetMapping("/")
    public String list(Model model, BannerParam bannerParam) {

        List<BannerDto> bannerList = bannerService.listByUseYn(bannerParam);
        model.addAttribute("count", bannerList.size());
        model.addAttribute("bannerList", bannerList);

        return "index";
    }
}
