package senberg.musicmashup.domain.music;

import lombok.Data;

import java.util.*;

@Data
public class MusicResponse {
    UUID mbid;
    String description;
    List<Album> albums = Collections.synchronizedList(new LinkedList<>());
}
