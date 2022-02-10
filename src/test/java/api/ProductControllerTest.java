package api;

import api.controller.ProductController;
import api.model.Product;
import api.util.TimestampHelper;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;


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
@TestMethodOrder(OrderAnnotation.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mvc;

    /**
     * Tests that we can read our writes.
     */
    @Test 
    @Order(1)
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
            .andExpect(status().isCreated());

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
            .andExpect(jsonPath("$.data[*].category").value("apparel"));
    }

    /**
     * Test that we see the same number of writes.
     */
    @Test
    @Order(2)
    public void insertMany() throws Exception {
        int numToInsert = 5;
        for (int i = 1; i <= numToInsert; i++) {
            Product p;

            UUID uuid = UUID.randomUUID();
            List<String> tags = Arrays.asList("sports", "cap"); 
            String createdAt = TimestampHelper.getCurrentDate();
            p = new Product(
                uuid,
                "Hat #" + i,
                "Just a normal hat",
                "Thrasher",
                tags,
                "hats",
                createdAt
            );
    
            mvc.perform(
                post("/v1/products")
                .content(asJsonString(p))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
        } 

        // timeout to ensure that POST request goes through
        TimeUnit.SECONDS.sleep(1);

        mvc.perform(
            get("/v1/products")
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            // NOTE: Offset of +1 is used to account for previous test that inserts a value
            .andExpect(jsonPath("$.data.length()").value(numToInsert + 1));
    }

    /**
     * Tests if we can grab all products in a specific category.
     */
    @Test
    @Order(3)
    public void retrieveAllByCategory() throws Exception {
        Product p;

        UUID uuid = UUID.randomUUID();
        List<String> tags = Arrays.asList("blue", "shirt", "slim fit"); 
        String createdAt = TimestampHelper.getCurrentDate();
        p = new Product(
            uuid,
            "Blue shirt",
            "Blue hugo boss shirt",
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
            .andExpect(status().isCreated());

        // timeout to ensure that POST request goes through
        TimeUnit.SECONDS.sleep(1);

        mvc.perform(
            get("/v1/products?category=apparel")
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].category").value("apparel"))
            .andExpect(jsonPath("$.data[1].category").value("apparel"))
            .andExpect(jsonPath("$.data.length()").value(2));
    }

    /**
     * Tests if we can retrieve a page by various sizes
     */
    @Test
    @Order(4)
    public void retrieveProductPage() throws Exception {
        // Pagination and sizing
        mvc.perform(
            get("/v1/products?page=1&max=5")
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.length()").value(5));
        
        mvc.perform(
            get("/v1/products?max=1")
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.length()").value(1));
    }
    

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}