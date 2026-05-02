package com.example.devicesApi.devicesApi.controller;

import com.example.devicesApi.api.DevicesApi;
import com.example.devicesApi.devicesApi.service.DeviceService;
import com.example.devicesApi.model.Device;
import com.example.devicesApi.model.DeviceRequest;
import com.example.devicesApi.model.DeviceState;
import com.example.devicesApi.model.DeviceUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DeviceController implements DevicesApi {

    private final DeviceService service;

    public DeviceController(DeviceService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Device> createDevice(DeviceRequest deviceRequest) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();

    }

    @Override
    public ResponseEntity<Device> getDeviceById(String id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();

    }

    @Override
    public ResponseEntity<List<Device>> getAllDevices() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<Device> updateDevice(String id, DeviceUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();

    }

    @Override
    public ResponseEntity<Void> deleteDevice(String id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<List<Device>> getDevicesByBrand(String brand) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<List<Device>> getDevicesByState(DeviceState state) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}