package com.example.devicesApi.devicesApi.service;

import com.example.devicesApi.devicesApi.entity.DeviceEntity;
import com.example.devicesApi.devicesApi.exceptions.DeviceInUseException;
import com.example.devicesApi.devicesApi.exceptions.DeviceNotFoundException;
import com.example.devicesApi.devicesApi.mapper.DeviceMapper;
import com.example.devicesApi.devicesApi.repository.DeviceRepository;
import com.example.devicesApi.model.Device;
import com.example.devicesApi.model.DeviceRequest;
import com.example.devicesApi.model.DeviceState;
import com.example.devicesApi.model.DeviceUpdateRequest;
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

    public DeviceEntity getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found: " + id));
    }

    public DeviceEntity update(String id, DeviceUpdateRequest request) {
        DeviceEntity entity = getById(id);

        checkUpdatable(entity, request);

        if (request.getName() != null) {
            entity.setName(request.getName());
        }

        if (request.getBrand() != null) {
            entity.setBrand(request.getBrand());
        }

        if (request.getState() != null) {
            entity.setState(request.getState());
        }

        return repository.save(entity);
    }

    public void delete(String id) {
        DeviceEntity entity = getById(id);
        if (entity.getState() == DeviceState.IN_USE) {
            throw new DeviceInUseException("Cannot delete a device that is in use");
        }
        repository.deleteById(id);
    }

    public List<Device> getDevicesByBrand(String brand) {
        return repository.findByBrand(brand)
                .stream()
                .map(DeviceMapper::toDto)
                .toList();
    }

    public List<Device> getDevicesByState(DeviceState state) {
        return repository.findByState(state)
                .stream()
                .map(DeviceMapper::toDto)
                .toList();
    }

    private void checkUpdatable(DeviceEntity entity, DeviceUpdateRequest request) {
        // if the state is in_use, only the state can be updated
        if (entity.getState() == DeviceState.IN_USE) {
            if (request.getName() != null || request.getBrand() != null) {
                throw new DeviceInUseException("Cannot update name or brand for a device that is in use");
            }
        }
    }

}
