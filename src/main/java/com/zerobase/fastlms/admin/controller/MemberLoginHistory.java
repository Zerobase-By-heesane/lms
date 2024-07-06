package com.zerobase.fastlms.admin.controller;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberLoginHistory {
    private String userId;
    private LocalDateTime loginDt;
}
