package api;

import api.controller.ProductController;
import api.model.Product;
import api.util.TimestampHelper;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes=ProductServerApplication.class)
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    /**
     * Tests for read your writes consistency
     */
    @Test 
    public void writeThenRead() throws Exception {
        Product p;

        UUID uuid = UUID.randomUUID();
        List<String> tags = Arrays.asList("red", "shirt", "slim fit"); 
        String createdAt = TimestampHelper.getCurrentDate();
        p = new Product(
            uuid,
            "Red shirt",
            "Red hugo boss shirt",
            "Hugo Boss",
            tags,
            "apparel",
            createdAt
        );

        mvc.perform(
            post("/v1/products")
            .content(asJsonString(p))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        // timeout to ensure that POST request goes through
        TimeUnit.SECONDS.sleep(1);

        mvc.perform(
            get("/v1/products")
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[*].name").value("Red shirt"))
            .andExpect(jsonPath("$.data[*].description").value("Red hugo boss shirt"))
            .andExpect(jsonPath("$.data[*].brand").value("Hugo Boss"))
            .andExpect(jsonPath("$.data[*].tags").exists())
            .andExpect(jsonPath("$.data[*].category").value("apparel"))
            .andReturn().getResponse().getContentAsString();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}