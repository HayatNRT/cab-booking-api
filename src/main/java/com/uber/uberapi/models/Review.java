package com.uber.uberapi.models;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class Review extends Auditable {
    private Integer ratingOutOfFive;
    private String note;
}
