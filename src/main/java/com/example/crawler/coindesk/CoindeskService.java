package com.example.crawler.coindesk;

import com.example.crawler.coindesk.dto.CoindeskItemResponse;
import com.example.crawler.coindesk.dto.CoindeskResponse;
import com.example.crawler.slack.SlackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Slf4j
public class CoindeskService {

    private static final String COINDESK_REQUEST_A16Z_URL = "https://api.queryly.com/json.aspx?queryly_key=d0ab87fd70264c0a&query=a16z&batchsize=10&sort=date";
    private static final String COINDESK_REQUEST_SEQUOIA_URL = "https://api.queryly.com/json.aspx?queryly_key=d0ab87fd70264c0a&query=sequoia&batchsize=10&sort=date";
    private static final String COINDESK_REQUEST_HASHED_URL = "https://api.queryly.com/json.aspx?queryly_key=d0ab87fd70264c0a&query=hashed&batchsize=10&sort=date";

    private static final String COINDESK_NEWS_LINK = "";
    private static final String COINDESK_NEWS_ALARM = "Coindesk에서 검색하여 새로운 기사를 찾았습니다.\n";

    private final RestTemplate restTemplate = new RestTemplate();

    private final SlackService slackService;

    public CoindeskService(SlackService slackService) {
        this.slackService = slackService;
    }

    private String a16zId;
    private String sequoiaId;
    private String hashedId;

    @PostConstruct
    private void setIds() {
        a16zId = parseA16zNews().get(0).get_id();
        log.info("[Coindesk] A16z set up newsId: " + a16zId);

        sequoiaId = parseSequoiaNews().get(0).get_id();
        log.info("[Coindesk] Sequoia set up newsId: " + sequoiaId);

        hashedId = parseHashedNews().get(0).get_id();
        log.info("[Coindesk] Hashed set up newsId: " + hashedId);
    }

    private List<CoindeskItemResponse> parseA16zNews() {
        CoindeskResponse coindeskResponse = restTemplate.getForObject(COINDESK_REQUEST_A16Z_URL, CoindeskResponse.class);
        return coindeskResponse.getItems();
    }

    private List<CoindeskItemResponse> parseSequoiaNews() {
        CoindeskResponse coindeskResponse = restTemplate.getForObject(COINDESK_REQUEST_SEQUOIA_URL, CoindeskResponse.class);
        return coindeskResponse.getItems();
    }

    private List<CoindeskItemResponse> parseHashedNews() {
        CoindeskResponse coindeskResponse = restTemplate.getForObject(COINDESK_REQUEST_HASHED_URL, CoindeskResponse.class);
        return coindeskResponse.getItems();
    }

    @Scheduled(fixedDelay = 300000)
    public void fetchA16zNews() {
        // A16Z
        List<CoindeskItemResponse> a16zItems = parseA16zNews();
        String a16zFirstId = a16zItems.get(0).get_id();
        if (a16zId.equals(a16zFirstId)) {
            log.info("[Coindesk] A16z No news added");
            return;
        }
        for (CoindeskItemResponse item : a16zItems) {
            String addedId = item.get_id();
            if (a16zId.equals(addedId)) {
                a16zId = a16zFirstId;
                return;
            }
            log.info("[Coindesk] newly added a16z newsId : " + addedId);
            slackService.sendSlackDeployMessage(COINDESK_NEWS_ALARM + item.getTitle() + "\n" + COINDESK_NEWS_LINK + item.getLink());
        }
    }

    @Scheduled(fixedDelay = 300000)
    public void fetchSequoiaNews() {
        // Sequoia
        List<CoindeskItemResponse> sequoiaItems = parseSequoiaNews();
        String sequoiaFirstId = sequoiaItems.get(0).get_id();
        if (sequoiaId.equals(sequoiaFirstId)) {
            log.info("[Coindesk] Sequoia No news added");
            return;
        }
        for (CoindeskItemResponse item : sequoiaItems) {
            String addedId = item.get_id();
            if (sequoiaId.equals(addedId)) {
                sequoiaId = sequoiaFirstId;
                return;
            }
            log.info("[Coindesk] newly added sequoia newsId : " + addedId);
            slackService.sendSlackDeployMessage(COINDESK_NEWS_ALARM + item.getTitle() + "\n" + COINDESK_NEWS_LINK + item.getLink());
        }
    }

    @Scheduled(fixedDelay = 300000)
    public void fetchHashedNews() {
        // Hashed
        List<CoindeskItemResponse> hashedItem = parseHashedNews();
        String hashedFirstId = hashedItem.get(0).get_id();
        if (hashedId.equals(hashedFirstId)) {
            log.info("[Coindesk] Hashed No news added");
            return;
        }
        for (CoindeskItemResponse item : hashedItem) {
            String addedId = item.get_id();
            if (hashedId.equals(addedId)) {
                hashedId = hashedFirstId;
                return;
            }
            log.info("[Coindesk] newly added hashed newsId : " + addedId);
            slackService.sendSlackDeployMessage(COINDESK_NEWS_ALARM + item.getTitle() + "\n" + COINDESK_NEWS_LINK + item.getLink());
        }
    }
}
