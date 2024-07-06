package com.zerobase.fastlms.banner.dto;

import com.zerobase.fastlms.banner.entity.Banner;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Data
public class BannerDto {

    Long id;
    String bannerTitle; // 배너 제목
    String bannerImageFileName; // 파일 이름
    String bannerImageFilePath; // 절대 경로
    String bannerImageFileUrl; // file/~~ 경로
    String bannerLink; // 링크 클릭시 이동할 주소
    String bannerOpenType; // 링크 클릭시 이동할 방식

    LocalDateTime regDt;

    int order;
    boolean useYn;

    long totalCount;
    long seq;

    public static BannerDto of(Banner x) {

        return BannerDto.builder()
                .id(x.getId())
                .bannerTitle(x.getTitle())
                .bannerImageFileName(x.getImageFileName())
                .bannerImageFilePath(x.getImageFilePath())
                .bannerImageFileUrl(x.getImageFileUrl())
                .bannerLink(x.getLink())
                .bannerOpenType(x.getOpenType())
                .order(x.getOrder())
                .useYn(x.isUseYn())
                .build();
    }

    public String getRegDtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return regDt != null ? regDt.format(formatter) : "";
    }
}
