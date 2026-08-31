package com.faircart.controller;

import com.faircart.dto.comparison.UrlComparisonRequest;
import com.faircart.dto.comparison.UrlComparisonResponse;
import com.faircart.dto.response.ApiResponse;
import com.faircart.service.comparison.UrlComparisonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/compare")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UrlComparisonController {

    private final UrlComparisonService urlComparisonService;

    @PostMapping("/urls")
    public ResponseEntity<ApiResponse<UrlComparisonResponse>> compareProductUrls(
            @Valid @RequestBody UrlComparisonRequest request) {
        UrlComparisonResponse response = urlComparisonService.compareProductUrls(request);
        return ResponseEntity.ok(ApiResponse.success("Successfully analyzed and compared product URLs", response));
    }
}