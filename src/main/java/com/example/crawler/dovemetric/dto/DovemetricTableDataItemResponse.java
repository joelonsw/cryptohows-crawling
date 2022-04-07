package com.example.crawler.dovemetric.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DovemetricTableDataItemResponse {

    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    // Fundraising Round
    private String fldQWN0KwgHI4rRCt;
    public String getFundraisingRound() {
        return fldQWN0KwgHI4rRCt;
    }

    // Date
    private String fldSGmVP3olLGmAID;
    public String getDate() {
        return fldSGmVP3olLGmAID;
    }
    public Date toDate() {
        try {
            if (Objects.isNull(fldSGmVP3olLGmAID)) {
                return FORMATTER.parse("1990-01-01T00:00:00.000Z");
            }
            return FORMATTER.parse(fldSGmVP3olLGmAID);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Date Parser Error");
        }
    }

    // Amount
    private String fldbHI1iWcw6U912R;
    public String getAmount() {
        return fldbHI1iWcw6U912R;
    }

    // Investors
    private String fldLUKChsVOb55YoG;
    public String getInvestors() {
        return fldLUKChsVOb55YoG;
    }

    // Website
    private String fld8ZuXfzyuH1b9Dv;
    public String getWebsite() {
        return fld8ZuXfzyuH1b9Dv;
    }

    // Category
    private String fldZDHNTANZFzpn9s;
    public String getCategory() {
        return fldZDHNTANZFzpn9s;
    }

    // Detail Category
    private String fldeXGuHCgBEZBM38;
    public String getDetailCategory() {
        return fldeXGuHCgBEZBM38;
    }

    // Announcement
    private String fldcDMf8A5D64Ecdf;
    public String getAnnouncement() {
        return fldcDMf8A5D64Ecdf;
    }

    // Description
    private String fldJHMHegLEl2A56n;
    public String getDescription() {
        return fldJHMHegLEl2A56n;
    }
}
