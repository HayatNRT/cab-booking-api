package com.uber.uberapi.repositories;

import com.uber.uberapi.models.Driver;
import com.uber.uberapi.models.ExactLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findFirstByAccount_Username(String username);

    @Query(value = """
        SELECT l
        FROM ExactLocation l
        WHERE (6371 * acos(cos(radians(:latitude)) 
              * cos(radians(l.latitude)) 
              * cos(radians(l.longitude) - radians(:longitude)) 
              + sin(radians(:latitude)) 
              * sin(radians(l.latitude)))) < :distance
    """)
    List<ExactLocation> findLocationsWithinDistance(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("distance") Double distance
    );

}