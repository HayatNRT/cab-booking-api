package com.uber.uberapi.models;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class Role extends Auditable {
    @Column(unique = true, nullable = false)
    private String name;
    private String description;
}


// Role Based Authentication
// Permission based authentication