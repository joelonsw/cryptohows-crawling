package com.example.crawler.dovemetric.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class DovemetricDateResponse {
    private List<DovemetricTableDataResponse> tableDatas;

    public DovemetricTableDataResponse getTableData() {
        return tableDatas.get(0);
    }
}
