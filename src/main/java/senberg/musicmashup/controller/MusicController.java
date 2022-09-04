package senberg.musicmashup.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import senberg.musicmashup.domain.music.MusicResponse;
import senberg.musicmashup.service.MusicService;

import java.util.UUID;
import java.util.concurrent.Executor;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@Slf4j
public class MusicController {
    @Value("${musiccontroller.timeout}")
    private long timeout;

    @Autowired
    MusicService musicService;
    @Autowired
    Executor musicControllerExecutor;

    @GetMapping("/api/{mbid}")
    public DeferredResult<MusicResponse> get(@PathVariable UUID mbid) {
        DeferredResult<MusicResponse> deferredResult = new DeferredResult<>(timeout);
        musicControllerExecutor.execute(() -> handleRequest(mbid, deferredResult));
        return deferredResult;
    }

    private void handleRequest(UUID mbid, DeferredResult<MusicResponse> deferredResult) {
        try {
            MusicResponse musicResponse = musicService.get(mbid);
            deferredResult.setResult(musicResponse);
        } catch (ApplicationException e){
            deferredResult.setErrorResult(new ResponseEntity<>(e.getReason(), e.getHttpStatus()));
        } catch (Exception e){
            deferredResult.setErrorResult(new ResponseEntity<>(e, INTERNAL_SERVER_ERROR));
        }
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(@SuppressWarnings("unused") IllegalArgumentException e) {
        return new ResponseEntity<>("Invalid parameter. The MusicBrainz Identifier must be a valid UUID.", BAD_REQUEST);
    }
}
