package com.example.crawler.bloomingbit;

import com.example.crawler.bloomingbit.dto.BloomingbitItemResponse;
import com.example.crawler.bloomingbit.dto.BloomingbitResponse;
import com.example.crawler.slack.SlackService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class BloomingbitService {

    private static final String BLOOMINGBIT_REQUEST_URL = "https://news1.bloomingbit.io/news/wDailyNewsList?&keyword_title=투자";
    private static final String BLOOMINGBIT_NEWS_LINK = "https://bloomingbit.io/news/";
    private static final String BLOOMINGBIT_NEWS_ALARM = "Bloomingbit에서 투자로 검색하여 새로운 기사를 찾았습니다.\n";

    private final RestTemplate restTemplate = new RestTemplate();

    private final SlackService slackService;

    public BloomingbitService(SlackService slackService) {
        this.slackService = slackService;
    }

    private String recentNewsId;

    @PostConstruct
    private void setRecentNewsId() {
        List<BloomingbitItemResponse> newsItems = parseNewsFromRequest();
        recentNewsId = newsItems.get(0).getId_news_feed();
        log.info("Bloomingbit set up newsId : " + recentNewsId);
    }

    private List<BloomingbitItemResponse> parseNewsFromRequest() {
        BloomingbitResponse response = restTemplate.getForObject(BLOOMINGBIT_REQUEST_URL, BloomingbitResponse.class);
        return response.getItem_list();
    }

    @Scheduled(fixedDelay = 300000)
    public void fetchNews() {
        List<BloomingbitItemResponse> newsItems = parseNewsFromRequest();
        String firstNewsId = newsItems.get(0).getId_news_feed();
        if (Objects.equals(recentNewsId, firstNewsId)) {
            return;
        }
        for (BloomingbitItemResponse news : newsItems) {
            String addedNewsId = news.getId_news_feed();
            if (Objects.equals(recentNewsId, addedNewsId)) {
                recentNewsId = firstNewsId;
                break;
            }
            log.info("Bloomingbit newly added newsId : " + addedNewsId);
            slackService.sendSlackDeployMessage(BLOOMINGBIT_NEWS_ALARM + BLOOMINGBIT_NEWS_LINK + addedNewsId);
        }
    }
}
