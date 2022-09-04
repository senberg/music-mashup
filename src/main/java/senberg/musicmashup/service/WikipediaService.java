package senberg.musicmashup.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import senberg.musicmashup.controller.ApplicationException;
import senberg.musicmashup.domain.wikipedia.Page;
import senberg.musicmashup.domain.wikipedia.WikipediaResponse;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@Service
@Slf4j
public class WikipediaService {
    @Value("${wikipedia.page-api-url}")
    private String pageApiURL;

    @Autowired
    RestTemplate restTemplate;

    public WikipediaResponse get(String title){
        try {
            return restTemplate.getForObject(pageApiURL, WikipediaResponse.class, title);
        } catch (RestClientException e){
            log.info(String.valueOf(e));
            throw new ApplicationException(BAD_GATEWAY, "Unable to retrieve data from the Wikipedia api.");
        }
    }

    public String extractDescriptionFromResponse(WikipediaResponse response) {
        if(response.getQuery() == null){
            throw new ApplicationException(BAD_GATEWAY, "Missing query in response from Wikipedia.");
        } else if(response.getQuery().getPages() == null) {
            throw new ApplicationException(BAD_GATEWAY, "Missing pages in response from Wikipedia.");
        } else if(response.getQuery().getPages().isEmpty()) {
            throw new ApplicationException(BAD_GATEWAY, "No pages in response from Wikipedia.");
        } else {
            // Implementation decision; if multiple pages are returned, use one at random instead of throwing an error.
            Page page = response.getQuery().getPages().entrySet().iterator().next().getValue();

            if(page.getExtract() == null){
                // Implementation decision; if description is missing or empty, return it as a an empty description instead of throwing an error.
                return "";
            } else {
                return page.getExtract();
            }
        }
    }
}
