package com.example.devicesApi.devicesApi.controller;

import com.example.devicesApi.api.DevicesApi;
import com.example.devicesApi.devicesApi.entity.DeviceEntity;
import com.example.devicesApi.devicesApi.mapper.DeviceMapper;
import com.example.devicesApi.devicesApi.service.DeviceService;
import com.example.devicesApi.model.Device;
import com.example.devicesApi.model.DeviceRequest;
import com.example.devicesApi.model.DeviceState;
import com.example.devicesApi.model.DeviceUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DeviceController implements DevicesApi {

    private final DeviceService service;


    @Override
    public ResponseEntity<Device> createDevice(DeviceRequest deviceRequest) {
        DeviceEntity saved = service.create(deviceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(DeviceMapper.toDto(saved));
    }

    @Override
    public ResponseEntity<Device> getDeviceById(String id) {
        DeviceEntity entity = service.getById(id);
        return ResponseEntity.ok(DeviceMapper.toDto(entity));
    }

    @Override
    public ResponseEntity<List<Device>> getAllDevices() {
        List<Device> result = service.getAll().stream()
                .map(DeviceMapper::toDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Device> updateDevice(String id, DeviceUpdateRequest request) {
        DeviceEntity updated = service.update(id, request);
        return ResponseEntity.ok(DeviceMapper.toDto(updated));
    }

    @Override
    public ResponseEntity<Void> deleteDevice(String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<Device>> getDevicesByBrand(String brand) {
        return ResponseEntity.ok(service.getDevicesByBrand(brand)
                .stream()
                .map(DeviceMapper::toDto)
                .toList());
    }

    @Override
    public ResponseEntity<List<Device>> getDevicesByState(DeviceState state) {
        return ResponseEntity.ok(service.getDevicesByState(state)
                .stream()
                .map(DeviceMapper::toDto)
                .toList());
    }
}