package com.uber.uberapi.services;

import com.uber.uberapi.dto.DriverRequest;
import com.uber.uberapi.models.Account;
import com.uber.uberapi.models.Driver;
import com.uber.uberapi.models.Role;
import com.uber.uberapi.repositories.DriverRepository;
import com.uber.uberapi.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {
    private final RoleRepository roleRepository;
    private final DriverRepository driverRepository;

    @Transactional
    public Driver createDriver(DriverRequest request) {
        Driver driver = getDriver(request);
        return driverRepository.save(driver);
    }

    public Driver getDriver(DriverRequest request) {
        List<Role> roleDriver = roleRepository.findByName("ROLE_DRIVER");
        return Driver.builder().gender(request.gender()).name(request.name())
                .phoneNumber(request.phoneNumber()).dob(request.dob())
                .account(new Account(request.username(), request.password(), roleDriver)).build();
    }
}
