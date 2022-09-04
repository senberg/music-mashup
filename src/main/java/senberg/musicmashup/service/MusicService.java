package senberg.musicmashup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import senberg.musicmashup.domain.music.Album;
import senberg.musicmashup.domain.music.MusicResponse;
import senberg.musicmashup.domain.musicbrainz.Artist;
import senberg.musicmashup.domain.wikidata.WikidataResponse;
import senberg.musicmashup.domain.wikipedia.WikipediaResponse;

import java.util.UUID;

@Service
public class MusicService {
    @Autowired
    MusicBrainzService musicBrainzService;
    @Autowired
    WikidataService wikidataService;
    @Autowired
    WikipediaService wikipediaService;
    @Autowired
    CoverArtArchiveService coverArtArchiveService;

    public MusicResponse get(UUID mbid){
        Artist artist = musicBrainzService.get(mbid);
        String wikidataId = musicBrainzService.extractWikidataIdFromArtist(artist);
        WikidataResponse wikidataResponse = wikidataService.get(wikidataId);
        String wikipediaTitle = wikidataService.extractWikipediaTitleFromResponse(wikidataId, wikidataResponse);
        WikipediaResponse wikipediaResponse = wikipediaService.get(wikipediaTitle);
        String description = wikipediaService.extractDescriptionFromResponse(wikipediaResponse);
        MusicResponse musicResponse = new MusicResponse();
        musicResponse.setMbid(mbid);
        musicResponse.setDescription(description);

        artist.getReleaseGroups().parallelStream().forEach(releaseGroup -> {
            Album album = new Album();
            album.setTitle(releaseGroup.getTitle());
            album.setId(releaseGroup.getId());
            coverArtArchiveService.get(releaseGroup.getId()).ifPresent(response -> album.setImage(coverArtArchiveService.extractImageFromResponse(response)));
            musicResponse.getAlbums().add(album);
        });

        return musicResponse;
    }
}
