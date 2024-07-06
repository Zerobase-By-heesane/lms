package com.zerobase.fastlms.banner.service.impl;

import com.zerobase.fastlms.banner.dto.BannerDto;
import com.zerobase.fastlms.banner.entity.Banner;
import com.zerobase.fastlms.banner.mapper.BannerMapper;
import com.zerobase.fastlms.banner.model.BannerInput;
import com.zerobase.fastlms.banner.model.BannerParam;
import com.zerobase.fastlms.banner.repository.BannerRepository;
import com.zerobase.fastlms.banner.service.BannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class BannerServiceImpl implements BannerService {

    private final BannerMapper bannerMapper;
    private final BannerRepository bannerRepository;

    @Override
    public BannerDto getById(long id) {
        return BannerDto.of(bannerRepository.findById(id));
    }

    @Override
    public List<BannerDto> listByUseYn(BannerParam parameter) {
        return bannerMapper.selectListByUseYn(parameter);
    }

    @Override
    public List<BannerDto> list(BannerParam parameter) {

        long totalCount = bannerMapper.selectListCount(parameter);

        List<BannerDto> list = bannerMapper.selectList(parameter);

        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (BannerDto x : list) {

                log.info("x: {}", x.toString());

                x.setTotalCount(totalCount);
                x.setSeq(totalCount - parameter.getPageStart() - i);
                i++;
            }
        }

        return list;
    }

    @Override
    public boolean add(BannerInput parameter) {

        log.info("=========================BannerInput parameter: {}", parameter);

        LocalDateTime now = LocalDateTime.now();
        // yyyy-MM-dd HH:mm:ss
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String formattedNow = now.format(formatter);

        Banner banner = Banner.builder()
                .title(parameter.getBannerTitle())
                .imageFileName(parameter.getBannerFile().substring(parameter.getBannerFile().lastIndexOf("/") + 1))
                .imageFileUrl(parameter.getBannerFileURL())
                .imageFilePath(parameter.getBannerFile())
                .link(parameter.getBannerLink())
                .openType(parameter.getBannerOpenType())
                .order(parameter.getSortValue())
                .useYn(Objects.equals(parameter.getUseYn(), "true"))
                .regDate(now)
                .build();

        log.info("=========================Banner: {}", banner);


        bannerRepository.save(banner);
        return true;
    }

    @Override
    public boolean set(BannerInput parameter) {

        Banner originBanner = bannerRepository.findById(parameter.getId()).orElseThrow(
                () -> new IllegalArgumentException("수정할 배너 정보가 없습니다.")
        );

        originBanner.setTitle(parameter.getBannerTitle());
        originBanner.setImageFileName(parameter.getBannerFile().substring(parameter.getBannerFile().lastIndexOf("/") + 1));
        originBanner.setImageFileUrl(parameter.getBannerFileURL());
        originBanner.setImageFilePath(parameter.getBannerFile());
        originBanner.setLink(parameter.getBannerLink());
        originBanner.setOpenType(parameter.getBannerOpenType());
        originBanner.setOrder(parameter.getSortValue());
        originBanner.setUseYn(Objects.equals(parameter.getUseYn(), "true"));
        originBanner.setUdtDate(LocalDateTime.now());

        bannerRepository.save(originBanner);

        return true;
    }

    @Override
    public boolean del(String idList) {

        if (!Objects.isNull(idList) && !idList.isEmpty()) {
            String[] ids = idList.split(",");
            for (String x : ids) {
                long id = 0L;
                try {
                    id = Long.parseLong(x);
                } catch (Exception e) {
                }

                if (id > 0) {
                    bannerRepository.deleteById(id);
                }
            }
        }

        return true;
    }
}
