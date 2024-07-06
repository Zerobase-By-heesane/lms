package com.zerobase.fastlms.banner.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "banner")
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bannerTitle", nullable = false)
    private String title;

    @Column(name = "imageFileName", nullable = false)
    private String imageFileName;

    @Column(name = "imageFileUrl", nullable = false)
    private String imageFileUrl;

    @Column(name = "imageFilePath", nullable = false)
    private String imageFilePath;

    @Column(name = "imageLink", nullable = false)
    private String link;

    @Column(name = "openType", nullable = false)
    private String openType;

    @Column(name = "sort_order", nullable = false)
    private int order;

    @Column(name = "use_yn", nullable = false)
    private boolean useYn;

    @Column(name = "reg_dt", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "udt_dt", nullable = true)
    private LocalDateTime udtDate;
}
