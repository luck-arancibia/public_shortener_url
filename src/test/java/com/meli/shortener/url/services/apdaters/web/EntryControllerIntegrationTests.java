package com.meli.shortener.url.services.apdaters.web;

import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.shortener.url.services.application.port.in.CreateEntry;
import com.meli.shortener.url.services.application.port.in.DestroyEntry;
import com.meli.shortener.url.services.application.port.in.LoadEntry;
import com.meli.shortener.url.services.application.port.in.UpdateEntry;
import com.meli.shortener.url.services.domain.UrlEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
@AutoConfigureMockMvc
public class EntryControllerIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private LoadEntry loadEntry;

  @MockBean
  private CreateEntry createEntry;

  @MockBean
  private UpdateEntry updateEntry;

  @MockBean
  private DestroyEntry destroyEntry;

  // Test for GET /{hash} - success scenario
  @Test
  public void testGetAndRedirectSuccess() throws Exception {
    String hash = "exampleHash";
    UrlEntry urlEntry = UrlEntry.builder()
        .id("1")
        .url("https://example.com")
        .hash(hash)
        .shortUrl("shortUrl123")
        .active(true)
        .build();
    when(loadEntry.findByHashForRedirect(hash)).thenReturn(urlEntry);

    mockMvc.perform(get("/" + hash))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("https://example.com"));
  }

  // Test for GET /{hash} - error scenario
  @Test
  public void testGetAndRedirectNotFound() throws Exception {
    String hash = "invalidHash";
    when(loadEntry.findByHashForRedirect(hash))
        .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));

    mockMvc.perform(get("/" + "invalidHash"))
        .andExpect(status().isNotFound());
  }

  // Test for POST /entry - success scenario
  @Test
  public void testPostEntrySuccess() throws Exception {
    UrlRequestDto request = new UrlRequestDto("https://example.com");
    UrlEntry urlEntry = UrlEntry.builder()
        .id("1")
        .url("https://example.com")
        .hash("exampleHash")
        .shortUrl("shortUrl123")
        .active(true)
        .build();
    when(createEntry.postByUrl(anyString())).thenReturn(urlEntry);

    mockMvc.perform(post("/entry")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.url").value("https://example.com"))
        .andExpect(jsonPath("$.short_url").value("shortUrl123"))
        .andExpect(jsonPath("$.active").value(true));
  }

  // Test for POST /entry - error scenario (invalid URL)
  @Test
  public void testPostEntryInvalidUrl() throws Exception {
    UrlRequestDto request = new UrlRequestDto("invalid-url.as.");
    mockMvc.perform(post("/entry").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  // Test for PUT /entry - success scenario
  @Test
  public void testPutEntrySuccess() throws Exception {
    EntryDto request = new EntryDto("1", "https://updated.com", true);
    UrlEntry updatedEntry = UrlEntry.builder()
        .id("1")
        .url("https://updated.com")
        .hash("updatedHash")
        .shortUrl("shortUrl123")
        .active(true)
        .build();
    when(updateEntry.update(anyString(), anyString(), anyBoolean())).thenReturn(updatedEntry);

    mockMvc.perform(put("/entry")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.url").value("https://updated.com"))
        .andExpect(jsonPath("$.active").value(true));
  }

  // Test for PUT /entry - error scenario (not found)
  @Test
  public void testPutEntryNotFound() throws Exception {
    EntryDto request = new EntryDto("nonExistentId", "https://updated.com", true);
    when(updateEntry.update(anyString(), anyString(), anyBoolean())).thenReturn(null);

    mockMvc.perform(put("/entry")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  // Test for DELETE /entry - success scenario
  @Test
  public void testDeleteEntrySuccess() throws Exception {
    String id = "1";
    doNothing().when(destroyEntry).destroy(id);

    mockMvc.perform(delete("/entry")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(id)))
        .andExpect(status().isOk());
  }


  // DTOs for testing
  @Data
  @AllArgsConstructor
  static class UrlRequestDto {
    private String url;

  }

  @Data
  @AllArgsConstructor
  static class EntryDto {
    private String id;
    private String url;
    private Boolean active;
    // getters and setters
  }
}
