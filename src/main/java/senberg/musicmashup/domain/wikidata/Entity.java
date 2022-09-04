package senberg.musicmashup.domain.wikidata;

import lombok.Data;

import java.util.Map;

@Data
public class Entity {
    Map<String, Sitelink> sitelinks;
}
