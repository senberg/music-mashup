package senberg.musicmashup.domain.wikidata;

import lombok.Data;

import java.util.Map;

@Data
public class WikidataResponse {
    Map<String, Entity> entities;
}
