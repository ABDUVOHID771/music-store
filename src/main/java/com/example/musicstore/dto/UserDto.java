package com.example.musicstore.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDto extends BaseDto {

    private String username;
    private String password;
    private String authorities;
    private boolean enabled;
}
