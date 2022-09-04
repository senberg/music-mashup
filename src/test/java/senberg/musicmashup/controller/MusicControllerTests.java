package senberg.musicmashup.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class MusicControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testOk() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/5b11f4ce-a62d-471e-81fc-a69a8278c7da"))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString("\"mbid\":\"5b11f4ce-a62d-471e-81fc-a69a8278c7da\"")));
    }

    @Test
    public void testInvalidId() throws Exception {
        mockMvc.perform(get("/api/random"))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(content().string("Invalid parameter. The MusicBrainz Identifier must be a valid UUID."));
    }

    @Test
    public void testNoId() throws Exception {
        mockMvc.perform(get("/api/"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    public void testEmptyId() throws Exception {
        mockMvc.perform(get("/api//"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    public void testWrongAddress() throws Exception {
        mockMvc.perform(get("/random"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }
}
