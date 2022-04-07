package com.example.crawler.dovemetric.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class DovemetricTableDataResponse {
    private List<DovemetricTableDataRowResponse> rows;

    public List<DovemetricTableDataRowResponse> getRowsAsRecentOrder() {
        return rows.stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }
}
