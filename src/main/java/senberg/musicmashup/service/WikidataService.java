package senberg.musicmashup.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import senberg.musicmashup.controller.ApplicationException;
import senberg.musicmashup.domain.wikidata.Entity;
import senberg.musicmashup.domain.wikidata.Sitelink;
import senberg.musicmashup.domain.wikidata.WikidataResponse;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@Service
@Slf4j
public class WikidataService {
    @Value("${wikidata.entity-info-url}")
    private String entityInfoURL;

    @Autowired
    RestTemplate restTemplate;

    public String getWikipediatitle(String wikidataId){
        WikidataResponse wikidataResponse = get(wikidataId);
        return extractWikipediaTitleFromResponse(wikidataId, wikidataResponse);
    }

    private WikidataResponse get(String id){
        try {
            return restTemplate.getForObject(entityInfoURL, WikidataResponse.class, id);
        } catch (RestClientException e){
            log.info(String.valueOf(e));
            throw new ApplicationException(BAD_GATEWAY, "Unable to retrieve data from the Wikidata api.");
        }
    }

    private String extractWikipediaTitleFromResponse(String wikidataId, WikidataResponse response){
        if(response.getEntities() == null){
            throw new ApplicationException(BAD_GATEWAY, "Missing entities in response from Wikidata.");
        } else if(!response.getEntities().containsKey(wikidataId)){
            throw new ApplicationException(BAD_GATEWAY, "Missing entity with the correct Wikidata id in response from Wikidata.");
        } else {
            Entity entity = response.getEntities().get(wikidataId);

            if(entity.getSitelinks() == null) {
                throw new ApplicationException(BAD_GATEWAY, "Missing sitelinks in response from Wikidata.");
            } else if(entity.getSitelinks().get("enwiki") == null){
                throw new ApplicationException(BAD_GATEWAY, "Missing enwiki sitelink in response from Wikidata.");
            } else {
                Sitelink sitelink = entity.getSitelinks().get("enwiki");

                if(sitelink.getTitle() == null || sitelink.getTitle().isBlank()){
                    throw new ApplicationException(BAD_GATEWAY, "Empty or missing enwiki sitelink title in response from Wikidata.");
                } else {
                    return sitelink.getTitle();
                }
            }
        }
    }
}
