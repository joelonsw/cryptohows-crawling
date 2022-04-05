package com.example.crawler.cointelegraph.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CoinTelegraphRequest {
    private String query;
    private Integer page;
}
