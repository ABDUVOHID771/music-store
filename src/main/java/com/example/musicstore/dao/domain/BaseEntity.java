package com.example.musicstore.dao.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false, length = 16)
    private UUID uuid;

    public boolean isNew() {
        return this.uuid == null && this.id == null;
    }

    @PrePersist
    public void generateUUID() {
        this.uuid = UUID.fromString(UUID.randomUUID().toString().substring(2, 36));
    }

}
