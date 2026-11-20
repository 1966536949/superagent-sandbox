package com.controller;

import com.annotation.IgnoreAuth;
import com.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查接口
 */
@RestController
public class HealthController {

    /**
     * 返回服务存活状态，供健康检查探针调用。
     * 不校验 Token，服务启动后无需登录即可访问。
     */
    @IgnoreAuth
    @GetMapping("/health")
    public R health() {
        return R.ok().put("status", "UP");
    }
}
