package senberg.musicmashup.domain.covertartarchive;

import lombok.Data;

import java.util.List;

@Data
public class CoverArtArchiveResponse {
    List<Image> images;
}
