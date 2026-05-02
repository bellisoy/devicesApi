package com.example.devicesApi.devicesApi.entity;

import com.example.devicesApi.model.DeviceState;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String brand;

    @Enumerated(EnumType.STRING)
    private DeviceState state;

    private OffsetDateTime creationTime;
}