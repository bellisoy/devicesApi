package com.example.devicesApi.devicesApi.service;


import com.example.devicesApi.devicesApi.entity.DeviceEntity;
import com.example.devicesApi.devicesApi.repository.DeviceRepository;
import com.example.devicesApi.model.DeviceRequest;
import com.example.devicesApi.model.DeviceState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        verify(repository).save(any());
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
}