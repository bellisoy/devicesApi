package com.example.devicesApi.devicesApi.service;


import com.example.devicesApi.devicesApi.entity.DeviceEntity;
import com.example.devicesApi.devicesApi.exceptions.DeviceInUseException;
import com.example.devicesApi.devicesApi.exceptions.DeviceNotFoundException;
import com.example.devicesApi.devicesApi.repository.DeviceRepository;
import com.example.devicesApi.model.Device;
import com.example.devicesApi.model.DeviceRequest;
import com.example.devicesApi.model.DeviceState;
import com.example.devicesApi.model.DeviceUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    private DeviceRepository repository;

    @InjectMocks
    private DeviceService service;

    @Test
    void create_shouldSetCreationTime() {
        DeviceRequest request = DeviceRequest.builder()
                .name("Device1")
                .brand("Brand1")
                .state(DeviceState.AVAILABLE)
                .build();

        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        DeviceEntity result = service.create(request);

        assertNotNull(result.getCreationTime());
        assertEquals("Device1", result.getName());
        assertEquals(DeviceState.AVAILABLE, result.getState());

        verify(repository).save(argThat(entity ->
                entity.getName().equals("Device1") &&
                        entity.getBrand().equals("Brand1") &&
                        entity.getState() == DeviceState.AVAILABLE &&
                        entity.getCreationTime() != null
        ));
    }

    @Test
    void getById_shouldReturnDevice_whenExists() {
        DeviceEntity entity = DeviceEntity.builder()
                .id("id1")
                .name("Test")
                .brand("Brand")
                .state(DeviceState.AVAILABLE)
                .creationTime(OffsetDateTime.now())
                .build();

        when(repository.findById("id1")).thenReturn(Optional.of(entity));

        DeviceEntity result = service.getById("id1");

        assertEquals("Test", result.getName());
        verify(repository).findById("id1");
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(repository.findById("id1")).thenReturn(Optional.empty());

        DeviceNotFoundException ex = assertThrows(DeviceNotFoundException.class,
                () -> service.getById("id1"));

        assertTrue(ex.getMessage().contains("Device not found"));
        verify(repository).findById("id1");
    }

    @Test
    void getAll_shouldReturnAllDevices() {
        DeviceEntity d1 = DeviceEntity.builder().id("1").name("A").brand("B").state(DeviceState.AVAILABLE).creationTime(OffsetDateTime.now()).build();
        DeviceEntity d2 = DeviceEntity.builder().id("2").name("C").brand("D").state(DeviceState.IN_USE).creationTime(OffsetDateTime.now()).build();

        when(repository.findAll()).thenReturn(List.of(d1, d2));

        List<DeviceEntity> all = service.getAll();

        assertEquals(2, all.size());
        verify(repository).findAll();
    }

    @Test
    void update_shouldAllowNameAndBrandChangeIfAvailable() {
        DeviceEntity entity = DeviceEntity.builder()
                .id("id1")
                .name("Old")
                .brand("OldBrand")
                .state(DeviceState.AVAILABLE)
                .creationTime(OffsetDateTime.now())
                .build();

        when(repository.findById("id1")).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        DeviceUpdateRequest request = DeviceUpdateRequest.builder()
                .name("New")
                .brand("NewBrand")
                .state(DeviceState.IN_USE)
                .build();

        DeviceEntity updated = service.update("id1", request);

        assertEquals("New", updated.getName());
        assertEquals("NewBrand", updated.getBrand());
        assertEquals(DeviceState.IN_USE, updated.getState());

        verify(repository).save(any());
    }

    @Test
    void update_shouldAllowPartialUpdate() {
        DeviceEntity entity = DeviceEntity.builder()
                .id("id1")
                .name("Old")
                .brand("OldBrand")
                .state(DeviceState.AVAILABLE)
                .creationTime(OffsetDateTime.now())
                .build();

        when(repository.findById("id1")).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        DeviceUpdateRequest request = DeviceUpdateRequest.builder()
                .name("New")
                .build();

        DeviceEntity updated = service.update("id1", request);

        assertEquals("New", updated.getName());
        assertEquals("OldBrand", updated.getBrand());

        verify(repository).save(any());
    }

    @Test
    void update_shouldNotAllowNameOrBrandChangeIfInUse() {
        DeviceEntity entity = DeviceEntity.builder()
                .id("id1")
                .name("Old")
                .brand("OldBrand")
                .state(DeviceState.IN_USE)
                .creationTime(OffsetDateTime.now())
                .build();

        when(repository.findById("id1")).thenReturn(Optional.of(entity));

        DeviceUpdateRequest request = DeviceUpdateRequest.builder()
                .name("New")
                .brand("NewBrand")
                .build();

        DeviceInUseException ex = assertThrows(DeviceInUseException.class,
                () -> service.update("id1", request));

        assertEquals("Cannot update name or brand for a device that is in use", ex.getMessage());

        verify(repository, never()).save(any());
    }

    @Test
    void delete_shouldThrowIfInUse() {
        DeviceEntity entity = DeviceEntity.builder()
                .id("id1")
                .name("Device")
                .brand("Brand")
                .state(DeviceState.IN_USE)
                .creationTime(OffsetDateTime.now())
                .build();

        when(repository.findById("id1")).thenReturn(Optional.of(entity));

        DeviceInUseException ex = assertThrows(DeviceInUseException.class,
                () -> service.delete("id1"));

        assertEquals("Cannot delete a device that is in use", ex.getMessage());

        verify(repository, never()).deleteById(any());
    }

    @Test
    void delete_shouldSucceedIfAvailable() {
        DeviceEntity entity = DeviceEntity.builder()
                .id("id1")
                .name("Device")
                .brand("Brand")
                .state(DeviceState.AVAILABLE)
                .creationTime(OffsetDateTime.now())
                .build();

        when(repository.findById("id1")).thenReturn(Optional.of(entity));

        service.delete("id1");

        verify(repository).deleteById("id1");
    }

    @Test
    void delete_shouldThrow_whenDeviceNotFound() {
        when(repository.findById("id1")).thenReturn(Optional.empty());

        assertThrows(DeviceNotFoundException.class,
                () -> service.delete("id1"));

        verify(repository, never()).deleteById(any());
    }

    @Test
    void getDevicesByBrand_shouldReturnFilteredDevices() {
        DeviceEntity d1 = DeviceEntity.builder()
                .id("id1")
                .name("A")
                .brand("Apple")
                .state(DeviceState.AVAILABLE)
                .build();

        DeviceEntity d2 = DeviceEntity.builder()
                .id("id2")
                .name("B")
                .brand("Samsung")
                .state(DeviceState.AVAILABLE)
                .build();

        when(repository.findByBrand("Apple")).thenReturn(List.of(d1));

        List<Device> result = service.getDevicesByBrand("Apple");

        assertEquals(1, result.size());
        assertEquals("Apple", result.getFirst().getBrand());

        verify(repository).findByBrand("Apple");
    }

    @Test
    void getDevicesByState_shouldReturnFilteredDevices() {
        DeviceEntity d1 = DeviceEntity.builder()
                .id("id1")
                .name("A")
                .brand("Apple")
                .state(DeviceState.IN_USE)
                .build();

        DeviceEntity d2 = DeviceEntity.builder()
                .id("id2")
                .name("B")
                .brand("Samsung")
                .state(DeviceState.AVAILABLE)
                .build();

        when(repository.findByState(DeviceState.IN_USE)).thenReturn(List.of(d1));

        List<Device> result = service.getDevicesByState(DeviceState.IN_USE);

        assertEquals(1, result.size());
        assertEquals(DeviceState.IN_USE, result.getFirst().getState());

        verify(repository).findByState(DeviceState.IN_USE);
    }
}