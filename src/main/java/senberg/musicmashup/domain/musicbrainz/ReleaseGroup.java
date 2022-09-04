package senberg.musicmashup.domain.musicbrainz;

import lombok.Data;

import java.util.UUID;

@Data
public class ReleaseGroup {
    UUID id;
    String title;
}
