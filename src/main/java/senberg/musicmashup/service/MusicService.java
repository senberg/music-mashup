package senberg.musicmashup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import senberg.musicmashup.domain.music.Album;
import senberg.musicmashup.domain.music.MusicResponse;
import senberg.musicmashup.domain.musicbrainz.Artist;

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
        String wikipediaTitle = wikidataService.getWikipediatitle(wikidataId);
        String description = wikipediaService.getDescription(wikipediaTitle);
        MusicResponse musicResponse = new MusicResponse();
        musicResponse.setMbid(mbid);
        musicResponse.setDescription(description);

        artist.getReleaseGroups().parallelStream().forEach(releaseGroup -> {
            Album album = new Album();
            album.setTitle(releaseGroup.getTitle());
            album.setId(releaseGroup.getId());
            coverArtArchiveService.getImage(releaseGroup.getId()).ifPresent(album::setImage);
            musicResponse.getAlbums().add(album);
        });

        return musicResponse;
    }
}
