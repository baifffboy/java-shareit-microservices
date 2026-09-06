package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CreateCommentRequest;
import ru.practicum.shareit.item.dto.CreateItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemClient itemClient;

    private CreateItemRequest validCreateRequest;
    private UpdateItemRequest validUpdateRequest;
    private CreateCommentRequest validCommentRequest;

    @BeforeEach
    void setUp() {
        validCreateRequest = new CreateItemRequest();
        validCreateRequest.setName("Test Item");
        validCreateRequest.setDescription("Test Description");
        validCreateRequest.setAvailable(true);

        validUpdateRequest = new UpdateItemRequest();
        validUpdateRequest.setName("Updated Item");
        validUpdateRequest.setDescription("Updated Description");
        validUpdateRequest.setAvailable(false);

        validCommentRequest = new CreateCommentRequest();
        validCommentRequest.setText("Great item!");
    }

    @Test
    void shouldCreateItemWithValidData() throws Exception {
        when(itemClient.createItem(anyLong(), any(CreateItemRequest.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCreateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestWhenItemNameEmpty() throws Exception {
        CreateItemRequest invalidRequest = new CreateItemRequest();
        invalidRequest.setName("");
        invalidRequest.setDescription("Test Description");
        invalidRequest.setAvailable(true);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenItemDescriptionEmpty() throws Exception {
        CreateItemRequest invalidRequest = new CreateItemRequest();
        invalidRequest.setName("Test Item");
        invalidRequest.setDescription("");
        invalidRequest.setAvailable(true);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenItemAvailableIsNull() throws Exception {
        CreateItemRequest invalidRequest = new CreateItemRequest();
        invalidRequest.setName("Test Item");
        invalidRequest.setDescription("Test Description");
        invalidRequest.setAvailable(null);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateItemWithValidData() throws Exception {
        when(itemClient.updateItem(anyLong(), anyLong(), any(UpdateItemRequest.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateCommentWithValidData() throws Exception {
        when(itemClient.createComment(anyLong(), anyLong(), any(CreateCommentRequest.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCommentRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestWhenCommentTextEmpty() throws Exception {
        CreateCommentRequest invalidRequest = new CreateCommentRequest();
        invalidRequest.setText("");

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetItem() throws Exception {
        when(itemClient.getItem(anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetUserItems() throws Exception {
        when(itemClient.getUserItems(anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void shouldSearchItems() throws Exception {
        when(itemClient.searchItems(anyString()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/items/search")
                        .param("text", "test"))
                .andExpect(status().isOk());
    }
}
