package com.example.devicesApi.devicesApi.service;

import com.example.devicesApi.devicesApi.entity.DeviceEntity;
import com.example.devicesApi.devicesApi.repository.DeviceRepository;
import com.example.devicesApi.model.DeviceRequest;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class DeviceService {

    private final DeviceRepository repository;

    public DeviceService(DeviceRepository repository) {
        this.repository = repository;
    }

    public DeviceEntity create(DeviceRequest request) {
        DeviceEntity entity = DeviceEntity.builder()
                .name(request.getName())
                .brand(request.getBrand())
                .state(request.getState())
                .creationTime(OffsetDateTime.now())
                .build();
        return repository.save(entity);
    }

    public List<DeviceEntity> getAll() {
        return repository.findAll();
    }
}
