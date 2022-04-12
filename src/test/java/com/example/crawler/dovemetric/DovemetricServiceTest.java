package com.example.crawler.dovemetric;

import com.example.crawler.dovemetric.dto.DovemetricResponse;
import com.example.crawler.dovemetric.dto.DovemetricTableDataRowResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.devtools.remote.client.HttpHeaderInterceptor;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class DovemetricServiceTest {

    private final String requestFullUrl = "https://airtable.com/v0.3/application/apppcI0nClUwo3GCb/read?stringifiedObjectParams=%7B%22includeDataForTableIds%22%3A%5B%22tbllShMbhc5jooPRb%22%5D%2C%22includeDataForViewIds%22%3A%5B%22viwymTshkJuerbyeT%22%5D%2C%22shouldIncludeSchemaChecksum%22%3Atrue%7D&requestId=reqy9kjnQp9VdVlKF&accessPolicy=%7B%22allowedActions%22%3A%5B%7B%22modelClassName%22%3A%22application%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%22%2C%22action%22%3A%22read%22%7D%2C%7B%22modelClassName%22%3A%22application%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%22%2C%22action%22%3A%22readForDetailView%22%7D%2C%7B%22modelClassName%22%3A%22table%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22read%22%7D%2C%7B%22modelClassName%22%3A%22table%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22readData%22%7D%2C%7B%22modelClassName%22%3A%22table%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22readDataForRowCards%22%7D%2C%7B%22modelClassName%22%3A%22view%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22readRowOrder%22%7D%2C%7B%22modelClassName%22%3A%22view%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22readData%22%7D%2C%7B%22modelClassName%22%3A%22view%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22getMetadataForPrinting%22%7D%2C%7B%22modelClassName%22%3A%22row%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22readDataForDetailView%22%7D%2C%7B%22modelClassName%22%3A%22row%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22createBoxDocumentSession%22%7D%2C%7B%22modelClassName%22%3A%22row%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22createDocumentPreviewSession%22%7D%5D%2C%22shareId%22%3A%22shrP7uEmnxbv7dUEV%22%2C%22applicationId%22%3A%22apppcI0nClUwo3GCb%22%2C%22generationNumber%22%3A0%2C%22expires%22%3A%222022-04-28T00%3A00%3A00.000Z%22%2C%22signature%22%3A%22efdff063ac6ebc4d0947704b43d9d2ee09b5a695f2900eb696dfee41e91364a6%22%7D";

    private final String requestUrl = "https://airtable.com/v0.3/application/apppcI0nClUwo3GCb/read";
    private final String stringifiedObjectParams = "%7B%22includeDataForTableIds%22%3A%5B%22tbllShMbhc5jooPRb%22%5D%2C%22includeDataForViewIds%22%3A%5B%22viwymTshkJuerbyeT%22%5D%2C%22shouldIncludeSchemaChecksum%22%3Atrue%7D";
    private final String requestId = "reqy9kjnQp9VdVlKF";
    private final String accessPolicy = "%7B%22allowedActions%22%3A%5B%7B%22modelClassName%22%3A%22application%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%22%2C%22action%22%3A%22read%22%7D%2C%7B%22modelClassName%22%3A%22application%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%22%2C%22action%22%3A%22readForDetailView%22%7D%2C%7B%22modelClassName%22%3A%22table%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22read%22%7D%2C%7B%22modelClassName%22%3A%22table%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22readData%22%7D%2C%7B%22modelClassName%22%3A%22table%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22readDataForRowCards%22%7D%2C%7B%22modelClassName%22%3A%22view%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22readRowOrder%22%7D%2C%7B%22modelClassName%22%3A%22view%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22readData%22%7D%2C%7B%22modelClassName%22%3A%22view%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22getMetadataForPrinting%22%7D%2C%7B%22modelClassName%22%3A%22row%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22readDataForDetailView%22%7D%2C%7B%22modelClassName%22%3A%22row%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22createBoxDocumentSession%22%7D%2C%7B%22modelClassName%22%3A%22row%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22createDocumentPreviewSession%22%7D%5D%2C%22shareId%22%3A%22shrP7uEmnxbv7dUEV%22%2C%22applicationId%22%3A%22apppcI0nClUwo3GCb%22%2C%22generationNumber%22%3A0%2C%22expires%22%3A%222022-04-28T00%3A00%3A00.000Z%22%2C%22signature%22%3A%22efdff063ac6ebc4d0947704b43d9d2ee09b5a695f2900eb696dfee41e91364a6%22%7D";

    private final RestTemplate restTemplate = new RestTemplate();

    @BeforeEach
    void setUp() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HttpHeaderInterceptor("Host", "airtable.com"));
        interceptors.add(new HttpHeaderInterceptor("x-airtable-client-queue-time", "62.400000005960464"));
        interceptors.add(new HttpHeaderInterceptor("x-airtable-application-id", "apppcI0nClUwo3GCb"));
        interceptors.add(new HttpHeaderInterceptor("x-airtable-inter-service-client", "webClient"));
        interceptors.add(new HttpHeaderInterceptor("x-airtable-inter-service-client-code-version", "7019ab336c6d5ba622f00d61047e6e8d082118f2"));
        interceptors.add(new HttpHeaderInterceptor("x-airtable-page-load-id", "pglMki4RsniCMp1T7"));
        interceptors.add(new HttpHeaderInterceptor("X-Requested-With", "XMLHttpRequest"));
        interceptors.add(new HttpHeaderInterceptor("x-time-zone", "Asia/Seoul"));
        interceptors.add(new HttpHeaderInterceptor("x-user-locale", "ko"));
        restTemplate.setInterceptors(interceptors);

        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);
    }

    @Test
    void StringToUri() {

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(requestUrl)
                .queryParam("stringifiedObjectParams", stringifiedObjectParams)
                .queryParam("requestId", requestId)
                .queryParam("accessPolicy", accessPolicy)
                .build(false);
        String uriString = uriComponents.toUriString();
        assertThat(uriString).isEqualTo(requestFullUrl);

        DovemetricResponse response = restTemplate.getForObject(uriString, DovemetricResponse.class);
        List<DovemetricTableDataRowResponse> rows = response.getData().getTableData().getRowsAsRecentOrder();
        for (DovemetricTableDataRowResponse row : rows) {
            System.out.println(row.getCellValuesByColumnId().getFundraisingRound() + " @ " + row.getCellValuesByColumnId().getDate());
        }

        DovemetricTableDataRowResponse firstRow = rows.get(0);
        String date = firstRow.getCellValuesByColumnId().getDate();
        for (DovemetricTableDataRowResponse row : rows) {
            if (Objects.isNull(row.getCellValuesByColumnId().getDate())) {
                continue;
            }
            if (row.getCellValuesByColumnId().getDate().equals(firstRow.getCellValuesByColumnId().getDate())) {
                System.out.println("Equal!-------------");
                System.out.println(row.getCellValuesByColumnId().getFundraisingRound() + " @ " + row.getCellValuesByColumnId().getDate());
            }
        }
    }

    @Test
    void StringToDate() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String dateStringBefore = "2022-03-30T00:00:00.000Z";
        Date dateBefore = formatter.parse(dateStringBefore);

        String dateStringLater = "2022-03-31T00:00:00.000Z";
        Date dateLater = formatter.parse(dateStringLater);

        int i = dateBefore.compareTo(dateLater);
        System.out.println("i = " + i);
    }

    @Test
    void compareDate() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String dateStringBefore = "2022-03-30T00:00:00.000Z";
        Date dateBefore = formatter.parse(dateStringBefore);

        String dateStringLater = "2022-03-31T00:00:00.000Z";
        Date dateLater = formatter.parse(dateStringLater);

        String dateStringLater2 = "2022-03-31T00:00:00.000Z";
        Date dateLater2 = formatter.parse(dateStringLater2);

        assertThat(dateLater.after(dateBefore)).isTrue();
        assertThat(dateLater.equals(dateLater2)).isTrue();
    }
}
