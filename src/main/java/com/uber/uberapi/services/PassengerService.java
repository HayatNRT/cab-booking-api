package com.uber.uberapi.services;

import com.uber.uberapi.dto.PassengerRequest;
import com.uber.uberapi.models.Account;
import com.uber.uberapi.models.Passenger;
import com.uber.uberapi.models.Role;
import com.uber.uberapi.repositories.PassengerRepository;
import com.uber.uberapi.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerService {
    private static final String PASSENGER = "ROLE_PASSENGER";
    private final PassengerRepository passengerRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public Passenger createPassenger(PassengerRequest passengerRequest) {
        Passenger passenger = getPassenger(passengerRequest);
        return passengerRepository.save(passenger);
    }


    public Passenger getPassenger(PassengerRequest request) {
        List<Role> role = roleRepository.findByName(PASSENGER);
        return Passenger.builder().phoneNumber(request.phoneNumber()).dob(request.dob())
                .account(new Account(request.username(), request.password(), role))
                .gender(request.gender()).name(request.name()).build();
    }

}
