package com.example.devicesApi.devicesApi;

import com.example.devicesApi.devicesApi.repository.DeviceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DevicesApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DevicesApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeviceRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String createDevice(String name, String brand, String state) throws Exception {

        String body = """
                {
                    "name": "%s",
                    "brand": "%s",
                    "state": "%s"
                }
                """.formatted(name, brand, state);

        MvcResult result = mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString())
                .get("id").asText();
    }

    @AfterEach
    void clean() {
        repository.deleteAll();
    }

    @Test
    void create_device_success() throws Exception {
        createDevice("iPhone 15", "Apple", "AVAILABLE");
    }

    @Test
    void get_device_by_id_shouldReturnCorrectDevice() throws Exception {
        String id = createDevice("Galaxy S24", "Samsung", "AVAILABLE");

        mockMvc.perform(get("/devices/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Galaxy S24"))
                .andExpect(jsonPath("$.brand").value("Samsung"))
                .andExpect(jsonPath("$.state").value("AVAILABLE"));
    }

    @Test
    void get_device_by_id_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(get("/devices/non-existent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void get_all_devices_shouldReturnCorrectSize() throws Exception {
        createDevice("A", "Apple", "AVAILABLE");
        createDevice("B", "Samsung", "AVAILABLE");

        mockMvc.perform(get("/devices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void get_by_brand_shouldReturnOnlyMatchingDevices() throws Exception {
        createDevice("A", "Apple", "AVAILABLE");
        createDevice("B", "Samsung", "AVAILABLE");

        mockMvc.perform(get("/devices/brand/Apple"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].brand", everyItem(equalTo("Apple"))))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void get_by_state_shouldReturnOnlyMatchingDevices() throws Exception {
        createDevice("A", "Apple", "AVAILABLE");
        createDevice("B", "Samsung", "IN_USE");

        mockMvc.perform(get("/devices/state/AVAILABLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].state", everyItem(equalTo("AVAILABLE"))))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void update_device_full_shouldReturnUpdatedDevice() throws Exception {
        String id = createDevice("Old", "OldBrand", "AVAILABLE");

        String updateBody = """
                {
                    "name": "New",
                    "brand": "NewBrand",
                    "state": "AVAILABLE"
                }
                """;

        mockMvc.perform(patch("/devices/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("New"))
                .andExpect(jsonPath("$.brand").value("NewBrand"))
                .andExpect(jsonPath("$.state").value("AVAILABLE"));
    }

    @Test
    void update_device_shouldFail_whenInUse() throws Exception {
        String id = createDevice("Device", "Brand", "IN_USE");

        String updateBody = """
                {
                    "name": "New",
                    "brand": "NewBrand"
                }
                """;

        mockMvc.perform(patch("/devices/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isConflict());
    }

    @Test
    void delete_device_success() throws Exception {
        String id = createDevice("ToDelete", "Brand", "AVAILABLE");

        mockMvc.perform(delete("/devices/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_device_shouldFail_whenInUse() throws Exception {
        String id = createDevice("ToDelete", "Brand", "IN_USE");

        mockMvc.perform(delete("/devices/" + id))
                .andExpect(status().isConflict());
    }
}