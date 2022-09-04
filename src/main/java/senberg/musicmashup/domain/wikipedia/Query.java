package senberg.musicmashup.domain.wikipedia;

import lombok.Data;

import java.util.Map;

@Data
public class Query {
    Map<String, Page> pages;
}
