package com.example.crawler.bloomingbit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class BloomingbitResponse {
    private List<BloomingbitItemResponse> item_list;
}
