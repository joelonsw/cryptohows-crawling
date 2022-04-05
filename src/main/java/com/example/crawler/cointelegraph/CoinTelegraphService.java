package com.example.crawler.cointelegraph;

import com.example.crawler.cointelegraph.dto.CoinTelegraphItemResponse;
import com.example.crawler.cointelegraph.dto.CoinTelegraphRequest;
import com.example.crawler.cointelegraph.dto.CoinTelegraphResponse;
import com.example.crawler.slack.SlackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Slf4j
public class CoinTelegraphService {

    private static final String COINTELEGRAPH_URL = "https://cointelegraph.com/api/v1/content/search/result";

    private static final CoinTelegraphRequest A16Z_REQUEST = new CoinTelegraphRequest("a16z", 1);
    private static final CoinTelegraphRequest SEQUOIA_REQUEST = new CoinTelegraphRequest("sequoia", 1);
    private static final CoinTelegraphRequest HASHED_REQUEST = new CoinTelegraphRequest("hashed", 1);

    private static final String COINTELEGRAPH_NEWS_ALARM = "Cointelegraph에서 검색하여 새로운 기사를 찾았습니다.\n";

    private final RestTemplate restTemplate = new RestTemplate();

    private final SlackService slackService;

    private String a16zId;
    private String sequoiaId;
    private String hashedId;

    public CoinTelegraphService(SlackService slackService) {
        this.slackService = slackService;
    }

    @PostConstruct
    private void setIds() {
        a16zId = parseA16zNews().get(0).getId();
        log.info("[Cointelegraph] A16z set up newsId: " + a16zId);

        sequoiaId = parseSequoiaNews().get(0).getId();
        log.info("[Cointelegraph] Sequoia set up newsId: " + sequoiaId);

        hashedId = parseHashedNews().get(0).getId();
        log.info("[Cointelegraph] Hashed set up newsId: " + hashedId);
    }

    private List<CoinTelegraphItemResponse> parseA16zNews() {
        CoinTelegraphResponse coinTelegraphResponse = restTemplate.postForObject(COINTELEGRAPH_URL, A16Z_REQUEST, CoinTelegraphResponse.class);
        return coinTelegraphResponse.getPosts();
    }

    private List<CoinTelegraphItemResponse> parseSequoiaNews() {
        CoinTelegraphResponse coinTelegraphResponse = restTemplate.postForObject(COINTELEGRAPH_URL, SEQUOIA_REQUEST, CoinTelegraphResponse.class);
        return coinTelegraphResponse.getPosts();
    }

    private List<CoinTelegraphItemResponse> parseHashedNews() {
        CoinTelegraphResponse coinTelegraphResponse = restTemplate.postForObject(COINTELEGRAPH_URL, HASHED_REQUEST, CoinTelegraphResponse.class);
        return coinTelegraphResponse.getPosts();
    }

    @Scheduled(fixedDelay = 300000)
    public void fetchA16zNews() {
        // A16Z
        List<CoinTelegraphItemResponse> a16zItems = parseA16zNews();
        String a16zFirstId = a16zItems.get(0).getId();
        if (a16zId.equals(a16zFirstId)) {
            log.info("[CoinTelegraph] A16z No news added");
            return;
        }
        for (CoinTelegraphItemResponse item : a16zItems) {
            String addedId = item.getId();
            if (a16zId.equals(addedId)) {
                a16zId = a16zFirstId;
                return;
            }
            log.info("[CoinTelegraph] newly added a16z newsId : " + addedId);
            slackService.sendSlackDeployMessage(COINTELEGRAPH_NEWS_ALARM + item.getTitle() + "\n" + item.getUrl());
        }
    }

    @Scheduled(fixedDelay = 300000)
    public void fetchSequoiaNews() {
        // Sequoia
        List<CoinTelegraphItemResponse> sequoiaItems = parseSequoiaNews();
        String sequoiaFirstId = sequoiaItems.get(0).getId();
        if (sequoiaId.equals(sequoiaFirstId)) {
            log.info("[CoinTelegraph] Sequoia No news added");
            return;
        }
        for (CoinTelegraphItemResponse item : sequoiaItems) {
            String addedId = item.getId();
            if (sequoiaId.equals(addedId)) {
                sequoiaId = sequoiaFirstId;
                return;
            }
            log.info("[CoinTelegraph] newly added sequoia newsId : " + addedId);
            slackService.sendSlackDeployMessage(COINTELEGRAPH_NEWS_ALARM + item.getTitle() + "\n" + item.getUrl());
        }
    }

    @Scheduled(fixedDelay = 300000)
    public void fetchHashedNews() {
        // Hashed
        List<CoinTelegraphItemResponse> hashedItem = parseHashedNews();
        String hashedFirstId = hashedItem.get(0).getId();
        if (hashedId.equals(hashedFirstId)) {
            log.info("[CoinTelegraph] Hashed No news added");
            return;
        }
        for (CoinTelegraphItemResponse item : hashedItem) {
            String addedId = item.getId();
            if (hashedId.equals(addedId)) {
                hashedId = hashedFirstId;
                return;
            }
            log.info("[CoinTelegraph] newly added hashed newsId : " + addedId);
            slackService.sendSlackDeployMessage(COINTELEGRAPH_NEWS_ALARM + item.getTitle() + "\n" + item.getUrl());
        }
    }
}
