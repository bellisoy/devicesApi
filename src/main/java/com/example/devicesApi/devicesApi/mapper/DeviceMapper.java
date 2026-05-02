package com.example.devicesApi.devicesApi.mapper;


import com.example.devicesApi.devicesApi.entity.DeviceEntity;
import com.example.devicesApi.model.Device;
import com.example.devicesApi.model.DeviceState;

public class DeviceMapper {

    public static Device toDto(DeviceEntity entity) {
        return Device.builder()
                .id(entity.getId())
                .name(entity.getName())
                .brand(entity.getBrand())
                .state(DeviceState.valueOf(entity.getState().name()))
                .creationTime(entity.getCreationTime())
                .build();
    }
}