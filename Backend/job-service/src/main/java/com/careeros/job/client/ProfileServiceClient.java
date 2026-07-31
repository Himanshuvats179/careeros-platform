package com.careeros.job.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "profile-service", url = "${app.profile-service.url:http://localhost:8082}")
public interface ProfileServiceClient {

    @GetMapping("/api/v1/profiles/me")
    Map<String, Object> getMyProfile(@RequestHeader("Authorization") String token);

    @GetMapping("/api/v1/profiles/{userId}")
    Map<String, Object> getProfileByUserId(@PathVariable("userId") String userId, @RequestHeader("Authorization") String token);
}
