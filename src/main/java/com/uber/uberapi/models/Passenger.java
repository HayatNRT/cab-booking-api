package com.uber.uberapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "passenger")
public class Passenger extends Auditable {
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private Account account;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @JsonIgnore
    @OneToMany(mappedBy = "passenger")
    private List<Booking> bookings = new ArrayList<>();

    @OneToOne
    private Booking activeBooking = null;

    @Temporal(value = TemporalType.DATE)
    private LocalDate dob;

    private String phoneNumber;

    @JsonIgnore
    @OneToOne
    private ExactLocation home;
    @JsonIgnore
    @OneToOne
    private ExactLocation work;
    @JsonIgnore
    @OneToOne
    private ExactLocation lastKnownLocation;

    @JsonIgnore
    @OneToOne
    private Review avgRating;
    // this is updated by a cron job that runs nightly
}