package com.zerobase.fastlms.banner.controller;

import com.zerobase.fastlms.banner.dto.BannerDto;
import com.zerobase.fastlms.banner.model.BannerInput;
import com.zerobase.fastlms.banner.model.BannerParam;
import com.zerobase.fastlms.banner.service.BannerService;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.util.SaveFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class AdminBannerController extends BaseController{

    private final BannerService bannerService;

    @GetMapping("/admin/banner/list.do")
    public String list(Model model, BannerParam bannerParam) {

        bannerParam.init();
        List<BannerDto> bannerList = bannerService.list(bannerParam);

        long totalCount = 0;

        if(!CollectionUtils.isEmpty(bannerList)){
            totalCount = bannerList.get(0).getTotalCount();
            log.info("banner info: {}", bannerList.get(0).toString());
        }

        String queryString = bannerParam.getQueryString();
        String pagerHtml = getPaperHtml(totalCount,bannerParam.getPageSize(),bannerParam.getPageIndex(),queryString);

        model.addAttribute("bannerList", bannerList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/banner/list";
    }

    @GetMapping(value={"/admin/banner/edit.do", "/admin/banner/add.do"})
    public String addOrEdit(Model model,
                            HttpServletRequest request,
                            BannerInput input){
        String[] openTypes = {"새 창에서 열기", "현재 창에서 열기"};

        model.addAttribute("openType",openTypes);

        // true인 경우 수정모드
        boolean editMode = request.getRequestURI().contains("/edit.do");
        BannerDto banner = new BannerDto();

        // 수정모드 인경우,
        if(editMode){
            long id = input.getId();
            BannerDto existBanner = bannerService.getById(id);
            if(existBanner == null){
                model.addAttribute("message","수정할 배너 정보가 없습니다.");
                return "common/error";
            }
            banner = existBanner;
        }

        model.addAttribute("banner",banner);
        model.addAttribute("editMode",editMode);

        return "admin/banner/add";
    }

    @PostMapping(value={"/admin/banner/add.do", "/admin/banner/edit.do"})
    public String addSubmit(Model model,
                            HttpServletRequest request,
                            MultipartFile file,
                            BannerInput input){

        log.info("BannerInput: {}", input.toString());

        String saveFilename = "";
        String urlFilename = "";
        String baseLocalPath = "/Users/heesang/About Develop/zerobase/fastlms/src/main/resources/static/files";
        String baseUrlPath = "/files";
        if(file != null){
            String originalFilename = file.getOriginalFilename();
            log.info("originalFilename: {}", originalFilename);



            String[] arrFilename = SaveFile.getNewSaveFile(baseLocalPath, baseUrlPath, originalFilename);

            saveFilename = arrFilename[0];
            urlFilename = arrFilename[1];

            log.info("##################ADMIN BANNER CONTROLLER##################");
            log.info("saveFilename: {}", saveFilename);
            log.info("urlFilename: {}", urlFilename);

            try {
                File dest = new File(saveFilename);
                File parentDir = dest.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs(); // 부모 디렉토리 생성
                }
                log.info("dest: {}", dest);
                FileCopyUtils.copy(file.getInputStream(),new FileOutputStream(dest));
            } catch (Exception e) {
                log.info("############################ - 1");
                log.info(e.getMessage());
            }
        }

        input.setBannerFile(saveFilename);
        input.setBannerFileURL(urlFilename);

        boolean editMode = request.getRequestURI().contains("/edit.do");

        if (editMode) {
            long id = input.getId();
            BannerDto existCourse = bannerService.getById(id);
            if (existCourse == null) {
                // error 처리
                model.addAttribute("message", "강좌정보가 존재하지 않습니다.");
                return "common/error";
            }

            boolean result = bannerService.set(input);

        } else {
            boolean result = bannerService.add(input);
        }

        return "redirect:/admin/banner/list.do";
    }

    @PostMapping("/admin/banner/delete.do")
    public String del(Model model, HttpServletRequest request
            , BannerInput parameter) {

        log.info("parameter: {}", parameter.toString());

        boolean result = bannerService.del(parameter.getIdList());

        return "redirect:/admin/course/list.do";
    }

}
