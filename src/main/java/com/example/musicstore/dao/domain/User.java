package com.example.musicstore.dao.domain;

import lombok.*;

import javax.persistence.*;

/**
 * Created by Abduvohid 25/3/2020.
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "authorities")
    private String authorities;

    @Column(name = "enabled")
    private boolean enabled;

}

