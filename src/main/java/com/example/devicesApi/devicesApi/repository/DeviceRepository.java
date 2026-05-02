package com.example.devicesApi.devicesApi.repository;


import com.example.devicesApi.devicesApi.entity.DeviceEntity;
import com.example.devicesApi.model.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<DeviceEntity, String> {

    List<DeviceEntity> findByBrand(String brand);

    List<DeviceEntity> findByState(DeviceState state);

}