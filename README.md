# Music Mashup

## Requirements

- Java 17
- (mvn)
- (git)

## How to clone the project locally:

    git clone https://github.com/senberg/music-mashup.git

## How to build and run project locally:
    
    cd music-mashup
    mvn

## Examples of how to use the API:

    curl localhost/api/cc197bad-dc9c-440d-a5b5-d52ba2e14234
    curl localhost/api/5b11f4ce-a62d-471e-81fc-a69a8278c7da
    curl localhost/api/f90e8b26-9e52-4669-a5c9-e28529c47894
    curl localhost/actuator/prometheus

## TODO / Improvements

    Add tests for all services.
    Add throttling to avoid being blacklisted in the MusicBrainz API.
    Add caching for retrieved data since the external data seldom changes.
    Pagination support, important for retrieving artists with many albums.
    Retrieval of description and cover art simultaneously.
    Compare manual thread pool instead of executor to fetch cover arts.
    Use the wikipedia relation type if available.