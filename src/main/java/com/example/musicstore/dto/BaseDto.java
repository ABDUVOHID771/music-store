package com.example.musicstore.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class BaseDto implements Serializable {

    private Long id;
    private UUID uuid;
}
