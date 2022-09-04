package senberg.musicmashup.domain.musicbrainz;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Artist {
    @JsonProperty("release-groups")
    private List<ReleaseGroup> releaseGroups;
    private List<Relations> relations;
}
