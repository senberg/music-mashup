package senberg.musicmashup.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import senberg.musicmashup.controller.ApplicationException;
import senberg.musicmashup.domain.musicbrainz.Artist;
import senberg.musicmashup.domain.musicbrainz.Relations;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@Service
@Slf4j
public class MusicBrainzService {
    @Value("${musicbrainz.artist-info-url}")
    private String artistInfoURL;

    @Autowired
    RestTemplate restTemplate;

    public Artist get(UUID mbid){
        try {
            return restTemplate.getForObject(artistInfoURL, Artist.class, mbid.toString().toUpperCase());
        } catch (RestClientException e){
            log.info(String.valueOf(e));
            throw new ApplicationException(BAD_GATEWAY, "Unable to retrieve artist information from the Music Brainz api.");
        }
    }

    public String extractWikidataIdFromArtist(Artist artist){
        for (Relations relation : artist.getRelations()) {
            if("wikidata".equalsIgnoreCase(relation.getType())){
                if(relation.getUrl() == null) {
                    throw new ApplicationException(BAD_GATEWAY, "No url in wikidata relation in response from Music Brainz service.");
                } else if (relation.getUrl().getResource() == null) {
                    throw new ApplicationException(BAD_GATEWAY, "No url resource in wikidata relation in response from Music Brainz service.");
                } else if (!relation.getUrl().getResource().contains("/")) {
                    throw new ApplicationException(BAD_GATEWAY, "Invalid url resource in wikidata relation in response from Music Brainz service.");
                } else {
                    String wikidataIdentifier = relation.getUrl().getResource().substring(relation.getUrl().getResource().lastIndexOf('/') + 1);

                    if(wikidataIdentifier.isBlank()){
                        throw new ApplicationException(BAD_GATEWAY, "Missing wikidata identifier in url resource in wikidata relation in response from Music Brainz service.");
                    } else {
                        return wikidataIdentifier;
                    }
                }
            }
        }

        throw new ApplicationException(BAD_GATEWAY, "No wikidata relation in response from Music Brainz service.");
    }
}
