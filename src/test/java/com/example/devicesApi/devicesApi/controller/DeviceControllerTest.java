package com.example.devicesApi.devicesApi.controller;

import com.example.devicesApi.devicesApi.entity.DeviceEntity;
import com.example.devicesApi.devicesApi.service.DeviceService;
import com.example.devicesApi.model.Device;
import com.example.devicesApi.model.DeviceRequest;
import com.example.devicesApi.model.DeviceState;
import com.example.devicesApi.model.DeviceUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        assertNotNull(response.getBody());
        assertEquals("iPhone 15", response.getBody().getName());

        verify(service).create(request);
    }

    @Test
    void getAllDevices_shouldReturnAll() {
        DeviceEntity e1 = DeviceEntity.builder().id("id1").name("A").brand("Apple").state(DeviceState.AVAILABLE).build();
        DeviceEntity e2 = DeviceEntity.builder().id("id2").name("B").brand("Samsung").state(DeviceState.AVAILABLE).build();

        when(service.getAll()).thenReturn(List.of(e1, e2));
        ResponseEntity<List<Device>> response = controller.getAllDevices();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(service).getAll();
    }

    @Test
    void getDeviceById_shouldReturnDevice() {
        DeviceEntity entity = DeviceEntity.builder()
                .id("id1")
                .name("Galaxy S24")
                .brand("Samsung")
                .state(DeviceState.AVAILABLE)
                .build();

        when(service.getById("id1")).thenReturn(entity);

        ResponseEntity<Device> response = controller.getDeviceById("id1");

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("Galaxy S24", response.getBody().getName());

        verify(service).getById("id1");
    }

    @Test
    void updateDevice_shouldReturnUpdatedDevice() {
        DeviceUpdateRequest request = DeviceUpdateRequest.builder()
                .name("NewName")
                .brand("NewBrand")
                .build();

        DeviceEntity updated = DeviceEntity.builder()
                .id("id1")
                .name("NewName")
                .brand("NewBrand")
                .state(DeviceState.AVAILABLE)
                .build();

        when(service.update("id1", request)).thenReturn(updated);

        ResponseEntity<Device> response = controller.updateDevice("id1", request);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("NewName", response.getBody().getName());

        verify(service).update("id1", request);
    }

    @Test
    void deleteDevice_shouldDelegateToService() {
        doNothing().when(service).delete("id1");

        ResponseEntity<Void> response = controller.deleteDevice("id1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).delete("id1");
    }

    @Test
    void getDevicesByBrand_shouldReturnFilteredDevices() {
        DeviceEntity entity = DeviceEntity.builder()
                .id("id1")
                .name("A")
                .brand("Apple")
                .state(DeviceState.AVAILABLE)
                .build();

        Device dto = Device.builder()
                .id("id1")
                .name("A")
                .brand("Apple")
                .state(DeviceState.AVAILABLE)
                .build();

        when(service.getDevicesByBrand("Apple")).thenReturn(List.of(entity));

        ResponseEntity<List<Device>> response = controller.getDevicesByBrand("Apple");

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Apple", response.getBody().getFirst().getBrand());

        verify(service).getDevicesByBrand("Apple");
    }

    @Test
    void getDevicesByState_shouldReturnFilteredDevices() {
        DeviceEntity entity = DeviceEntity.builder()
                .id("id1")
                .name("A")
                .brand("Apple")
                .state(DeviceState.IN_USE)
                .build();

        when(service.getDevicesByState(DeviceState.IN_USE)).thenReturn(List.of(entity));

        ResponseEntity<List<Device>> response = controller.getDevicesByState(DeviceState.IN_USE);

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(DeviceState.IN_USE, response.getBody().getFirst().getState());

        verify(service).getDevicesByState(DeviceState.IN_USE);
    }
}