package com.example.crawler.dovemetric.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DovemetricTableDataRowResponse implements Comparable<DovemetricTableDataRowResponse> {
    private DovemetricTableDataItemResponse cellValuesByColumnId;

    @Override
    public int compareTo(DovemetricTableDataRowResponse o) {
        return (this.getCellValuesByColumnId().toDate().compareTo(o.getCellValuesByColumnId().toDate()));
    }
}
