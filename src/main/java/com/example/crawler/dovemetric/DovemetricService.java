package com.example.crawler.dovemetric;

import com.example.crawler.dovemetric.dto.DovemetricResponse;
import com.example.crawler.dovemetric.dto.DovemetricTableDataItemResponse;
import com.example.crawler.dovemetric.dto.DovemetricTableDataRowResponse;
import com.example.crawler.slack.SlackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.devtools.remote.client.HttpHeaderInterceptor;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DovemetricService {

    private static final String REQUEST_URL = "https://airtable.com/v0.3/application/apppcI0nClUwo3GCb/read";
    private static final String STRINGIFIED_OBJECT_PARAMS = "%7B%22includeDataForTableIds%22%3A%5B%22tbllShMbhc5jooPRb%22%5D%2C%22includeDataForViewIds%22%3A%5B%22viwymTshkJuerbyeT%22%5D%2C%22shouldIncludeSchemaChecksum%22%3Atrue%7D";
    private static final String REQUEST_ID = "reqy9kjnQp9VdVlKF";
    private static final String ACCESS_POLICY = "%7B%22allowedActions%22%3A%5B%7B%22modelClassName%22%3A%22application%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%22%2C%22action%22%3A%22read%22%7D%2C%7B%22modelClassName%22%3A%22application%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%22%2C%22action%22%3A%22readForDetailView%22%7D%2C%7B%22modelClassName%22%3A%22table%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22read%22%7D%2C%7B%22modelClassName%22%3A%22table%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22readData%22%7D%2C%7B%22modelClassName%22%3A%22table%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22readDataForRowCards%22%7D%2C%7B%22modelClassName%22%3A%22view%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22readRowOrder%22%7D%2C%7B%22modelClassName%22%3A%22view%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22readData%22%7D%2C%7B%22modelClassName%22%3A%22view%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22getMetadataForPrinting%22%7D%2C%7B%22modelClassName%22%3A%22row%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22readDataForDetailView%22%7D%2C%7B%22modelClassName%22%3A%22row%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22createBoxDocumentSession%22%7D%2C%7B%22modelClassName%22%3A%22row%22%2C%22modelIdSelector%22%3A%22apppcI0nClUwo3GCb%20*%22%2C%22action%22%3A%22createDocumentPreviewSession%22%7D%5D%2C%22shareId%22%3A%22shrP7uEmnxbv7dUEV%22%2C%22applicationId%22%3A%22apppcI0nClUwo3GCb%22%2C%22generationNumber%22%3A0%2C%22expires%22%3A%222022-04-28T00%3A00%3A00.000Z%22%2C%22signature%22%3A%22efdff063ac6ebc4d0947704b43d9d2ee09b5a695f2900eb696dfee41e91364a6%22%7D";

    private final RestTemplate restTemplate = new RestTemplate();

    private final SlackService slackService;

    private String uriString;

    private String currentFundingRound;

    public DovemetricService(SlackService slackService) {
        this.slackService = slackService;
    }

    @PostConstruct
    private void getInfos() {
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

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(REQUEST_URL)
                .queryParam("stringifiedObjectParams", STRINGIFIED_OBJECT_PARAMS)
                .queryParam("requestId", REQUEST_ID)
                .queryParam("accessPolicy", ACCESS_POLICY)
                .build(false);
        uriString = uriComponents.toUriString();

        DovemetricResponse response = restTemplate.getForObject(uriString, DovemetricResponse.class);
        currentFundingRound = response.getData()
                .getTableData()
                .getRowsAsRecentOrder()
                .get(0)
                .getCellValuesByColumnId()
                .getFundraisingRound();
        log.info("[Dovemetrics] Dovemetrics round setup : " + currentFundingRound);
    }

    @Scheduled(fixedDelay = 1000000)
    public void fetchDovemetric() {
        DovemetricResponse response = restTemplate.getForObject(uriString, DovemetricResponse.class);
        List<DovemetricTableDataRowResponse> rowsAsRecentOrder = response.getData()
                .getTableData()
                .getRowsAsRecentOrder();
        String recentFundraisingRound = rowsAsRecentOrder.get(0)
                .getCellValuesByColumnId()
                .getFundraisingRound();
        if (recentFundraisingRound.equals(currentFundingRound)) {
            log.info("[Dovemetrics] Dovemetrics No Round added");
            return;
        }
        for (DovemetricTableDataRowResponse rowResponse : rowsAsRecentOrder) {
            String fundraisingRound = rowResponse.getCellValuesByColumnId().getFundraisingRound();
            if (currentFundingRound.equals(fundraisingRound)) {
                currentFundingRound = recentFundraisingRound;
                return;
            }
            log.info("[Dovemetrics] newly added round : " + fundraisingRound);
            slackService.sendSlackDeployMessage(generateMessage(rowResponse.getCellValuesByColumnId()));
        }
    }

    private String generateMessage(DovemetricTableDataItemResponse dataItemResponse) {
        String fundraisingRound = "Fundrasing Round : " + dataItemResponse.getFundraisingRound() + "\n";
        String date = "Date : " + dataItemResponse.getDate() + "\n";
        String amount = "Amount : " + dataItemResponse.getAmount() + "\n";
        String investors = "Investors : " + dataItemResponse.getInvestors() + "\n";
        String category = "Category : " + dataItemResponse.getCategory() + "\n";
        String detailCategory = "Detail Category : " + dataItemResponse.getDetailCategory() + "\n";
        String description = "Description : " + dataItemResponse.getDescription() + "\n";
        String website = "Website : " + dataItemResponse.getWebsite() + "\n";
        String announcement = "Announcement : " + dataItemResponse.getAnnouncement() + "\n";
        return fundraisingRound + date + amount + investors + category + detailCategory + detailCategory + description + website + announcement;
    }
}
