package com.example.crawler.cointelegraph.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CoinTelegraphResponse {
    private List<CoinTelegraphItemResponse> posts;
}
