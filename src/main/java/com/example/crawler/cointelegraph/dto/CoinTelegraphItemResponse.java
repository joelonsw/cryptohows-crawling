package com.example.crawler.cointelegraph.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CoinTelegraphItemResponse {
    private String id;
    private String url;
    private String title;
    private String flash_date;
}
