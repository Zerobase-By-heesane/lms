package com.zerobase.fastlms.banner.model;

import lombok.Data;

@Data
public class BannerInput {
    Long id;
    String bannerTitle;
    String bannerFile;
    String bannerFileURL;
    String bannerLink;
    String bannerOpenType;
    int sortValue;
    String useYn;

    // 추가된 필드들
    String filename;
    String urlFilename;

    // 삭제를 위한
    String idList;
}
