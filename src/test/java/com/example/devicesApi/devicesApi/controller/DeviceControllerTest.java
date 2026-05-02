package com.example.devicesApi.devicesApi.controller;


import com.example.devicesApi.devicesApi.entity.DeviceEntity;
import com.example.devicesApi.devicesApi.service.DeviceService;
import com.example.devicesApi.model.Device;
import com.example.devicesApi.model.DeviceRequest;
import com.example.devicesApi.model.DeviceState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DeviceControllerTest {

    @Mock
    private DeviceService service;

    @InjectMocks
    private DeviceController controller;

    @Test
    void createDevice_shouldReturnCreatedDevice() {
        DeviceRequest request = DeviceRequest.builder()
                .name("iPhone 15")
                .brand("Apple")
                .state(DeviceState.AVAILABLE)
                .build();

        DeviceEntity entity = DeviceEntity.builder()
                .id("id1")
                .name("iPhone 15")
                .brand("Apple")
                .state(DeviceState.AVAILABLE)
                .creationTime(OffsetDateTime.now())
                .build();

        when(service.create(request)).thenReturn(entity);

        ResponseEntity<Device> response = controller.createDevice(request);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody());
        assertEquals("iPhone 15", response.getBody().getName());
        verify(service).create(request);
    }

    @Test
    void getAllDevices_shouldReturnAll() {
        DeviceEntity d1 = DeviceEntity.builder()
                .id("id1")
                .name("A")
                .brand("Apple")
                .state(DeviceState.AVAILABLE)
                .creationTime(OffsetDateTime.now())
                .build();

        DeviceEntity d2 = DeviceEntity.builder()
                .id("id2")
                .name("B")
                .brand("Samsung")
                .state(DeviceState.AVAILABLE)
                .creationTime(OffsetDateTime.now())
                .build();

        when(service.getAll()).thenReturn(List.of(d1, d2));

        ResponseEntity<List<Device>> response = controller.getAllDevices();

        assertEquals(200, response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(service).getAll();
    }
}