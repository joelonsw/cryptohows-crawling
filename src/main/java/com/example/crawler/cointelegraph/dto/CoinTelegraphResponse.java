package com.example.crawler.cointelegraph.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CoinTelegraphResponse {
    private List<CoinTelegraphItemResponse> posts;

    public List<CoinTelegraphItemResponse> getPosts() {
        List<CoinTelegraphItemResponse> nullEliminated = new ArrayList<>();
        for (CoinTelegraphItemResponse post : posts) {
            if (!Objects.isNull(post)) {
                nullEliminated.add(post);
            }
        }
        return nullEliminated;
    }
}
