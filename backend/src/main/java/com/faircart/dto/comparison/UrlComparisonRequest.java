package com.faircart.dto.comparison;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlComparisonRequest {

    @NotEmpty(message = "At least 2 product URLs are required for comparison")
    private List<String> urls;

    private String userBudget;
}