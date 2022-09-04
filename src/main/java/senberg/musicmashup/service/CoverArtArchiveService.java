package senberg.musicmashup.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import senberg.musicmashup.controller.ApplicationException;
import senberg.musicmashup.domain.covertartarchive.CoverArtArchiveResponse;
import senberg.musicmashup.domain.covertartarchive.Image;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@Service
@Slf4j
public class CoverArtArchiveService {
    @Value("${coverartarchive.release-group-api-url}")
    private String releaseGroupApiURL;

    @Autowired
    RestTemplate restTemplate;

    public Optional<CoverArtArchiveResponse> get(UUID mbid){
        try {
            return Optional.ofNullable(restTemplate.getForObject(releaseGroupApiURL, CoverArtArchiveResponse.class, mbid));
        } catch(HttpClientErrorException.NotFound e){
            return Optional.empty();
        } catch(RestClientException e){
            log.info(String.valueOf(e));
            throw new ApplicationException(BAD_GATEWAY, "Unable to retrieve data from the Covert Art Archive api.");
        }
    }

    public String extractImageFromResponse(CoverArtArchiveResponse response) {
        if(response.getImages() == null){
            throw new ApplicationException(BAD_GATEWAY, "Missing images in response from Cover Art Archive.");
        } else if(response.getImages().isEmpty()) {
            throw new ApplicationException(BAD_GATEWAY, "No images in response from Cover Art Archive.");
        } else {
            // Implementation decision; if multiple images are returned, use the first one.
            // Future improvement: prioritize any images where types include Front.
            Image image = response.getImages().get(0);

            if(image.getImage() == null || image.getImage().isBlank()){
                throw new ApplicationException(BAD_GATEWAY, "Missing or empty image link in response from Cover Art Archive.");
            } else {
                return image.getImage();
            }
        }
    }
}
