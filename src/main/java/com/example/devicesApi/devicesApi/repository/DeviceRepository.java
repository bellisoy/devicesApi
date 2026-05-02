package com.example.devicesApi.devicesApi.repository;


import com.example.devicesApi.devicesApi.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<DeviceEntity, String> {


}