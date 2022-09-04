package senberg.musicmashup.domain.music;

import lombok.Data;

import java.util.UUID;

@Data
public class Album {
    String title;
    UUID id;
    String image;
}
