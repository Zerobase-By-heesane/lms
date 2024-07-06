package com.zerobase.fastlms.banner.controller;

import com.zerobase.fastlms.banner.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ApiBannerController {

    private final BannerService bannerService;
}
