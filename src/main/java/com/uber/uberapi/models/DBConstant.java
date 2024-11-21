package com.uber.uberapi.models;


import lombok.*;

import jakarta.persistence.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dbconstant")
public class DBConstant extends Auditable {
    @Column(unique = true, nullable = false)
    private String name;
    private String value;
}
