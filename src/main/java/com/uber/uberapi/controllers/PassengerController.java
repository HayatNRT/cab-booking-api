package com.uber.uberapi.controllers;

import com.uber.uberapi.dto.PassengerRequest;
import com.uber.uberapi.exceptions.InvalidBookingException;
import com.uber.uberapi.exceptions.InvalidPassengerException;
import com.uber.uberapi.models.*;
import com.uber.uberapi.repositories.BookingRepository;
import com.uber.uberapi.repositories.PassengerRepository;
import com.uber.uberapi.repositories.ReviewRepository;
import com.uber.uberapi.services.BookingService;
import com.uber.uberapi.services.PassengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/passenger")
@RequiredArgsConstructor
@Tag(name = "Passenger Controller", description = "API for Passenger operation")
public class PassengerController {
    // handle all operations for passenger

    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;
    private final ReviewRepository reviewRepository;
    private final PassengerService passengerService;


     // all endpoints that the passenger can use
    public Passenger getPassengerFromId(Long passengerId) {
        Optional<Passenger> passenger = passengerRepository.findById(passengerId);
        if (passenger.isEmpty()) {
            throw new InvalidPassengerException("No passenger with id " + passengerId);
        }
        return passenger.get();
    }

    public Booking getPassengerBookingFromId(Long bookingId, Passenger passenger) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new InvalidBookingException("No booking with id " + optionalBooking);
        }
        Booking booking = optionalBooking.get();
        if (!booking.getPassenger().equals(passenger)) {
            throw new InvalidBookingException("Passenger " + passenger.getBookings() + " has no such booking " + bookingId);
        }
        return booking;
    }

    @Operation(summary = "Get Passenger By Id",
            description = "Retrieves Passenger by their unique Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passenger Found"),
            @ApiResponse(responseCode = "404", description = "Passenger not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Passenger.class)))})

    
    @GetMapping("/{passengerId}")
    public Passenger getPassengerDetails(@PathVariable(name = "passengerId") Long passengerId) {
        // passenger 10 has authenticated
        // endpoint - /passengers/bookings
        // endpoint - /passengers/20/bookings

        // make sure that the passenger is authenticated
        // and has the same passengerId as requested
        return getPassengerFromId(passengerId);
    }

    @Operation(summary = "Get Passenger Booking by passenger Id",
            description = "Retrieves passenger's booking by passenger id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "passenger's booking found"),
            @ApiResponse(responseCode = "404", description = "passenger's booking not found")
    })
    @GetMapping("{passengerId}/bookings")
    public List<Booking> getAllBookings(@PathVariable(name = "passengerId") Long passengerId) {
        Passenger passenger = getPassengerFromId(passengerId);
        return passenger.getBookings();
    }

    @GetMapping("{passengerId}/bookings/{bookingId}")
    public Booking getBooking(@PathVariable(name = "passengerId") Long passengerId,
                              @PathVariable(name = "bookingId") Long bookingId) {
        Passenger passenger = getPassengerFromId(passengerId);
        return getPassengerBookingFromId(bookingId, passenger);
    }
    // passengers/20/bookings
    // Prime, Taj Mahal, Red Fort
    // click the schedule button
    // this controller just saves the booking details
    // it responds back - success
    // sitting on the window
    @Operation(summary = "Booking ride by giving passengerId")
    @PostMapping("{passengerId}/bookings")
    public void requestBooking(@PathVariable(name = "passengerId") Long passengerId,
                               @RequestBody Booking data) {
        Passenger passenger = getPassengerFromId(passengerId);
        List<ExactLocation> route = new ArrayList<>();

        data.getRoute().forEach(location -> {
            route.add(ExactLocation.builder()
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build());
        });

        Booking booking = Booking.builder()
                .rideStartOTP(OTP.make(passenger.getPhoneNumber()))
                .route(route)
                .passenger(passenger)
                .bookingType(data.getBookingType())
                .startTime(data.getStartTime())
                .build();
        bookingService.createBooking(booking);
    }

    @PostMapping("/add")
    public ResponseEntity<Passenger> addPassenger(@Valid @RequestBody PassengerRequest request) {

        return new ResponseEntity<>(passengerService.createPassenger(request), HttpStatus.CREATED);

    }

    @PatchMapping("{passengerId}/bookings/{bookingId}")
    public void updateRoute(@PathVariable(name = "passengerId") Long passengerId,
                            @PathVariable(name = "bookingId") Long bookingId,
                            @RequestBody Booking data) {
        Passenger passenger = getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId, passenger);
        List<ExactLocation> route = new ArrayList<>(booking.getCompletedRoute());
        data.getRoute().forEach(location -> {
            route.add(ExactLocation.builder()
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build());
        });
        bookingService.updateRoute(booking, route);
    }

    // passenger requests booking
    // saved to db
    // message sent to driver matching service
    // consume the message
    // find the drivers
    // if none are available
    // passenger will be notified
    // passenger might retry to find drivers

    // restaurant - waiter analogy
    // Controller = waiters

    @PostMapping("{passengerId}/bookings/{bookingId}")
    public void retryBooking(@PathVariable(name = "passengerId") Long passengerId,
                             @PathVariable(name = "bookingId") Long bookingId) {
        Passenger passenger = getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId, passenger);
        bookingService.retryBooking(booking);
    }

    @DeleteMapping("{passengerId}/bookings/{bookingId}")
    public void cancelBooking(@PathVariable(name = "passengerId") Long passengerId,
                              @PathVariable(name = "bookingId") Long bookingId) {
        Passenger passenger = getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId, passenger);
        bookingService.cancelByPassenger(passenger, booking);
    }

    @PatchMapping("{passengerId}/bookings/{bookingId}/rate")
    public void rateRide(@PathVariable(name = "passengerId") Long passengerId,
                         @PathVariable(name = "bookingId") Long bookingId,
                         @RequestBody Review data) {
        // gets json data in the body
        Passenger passenger = getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId, passenger);
        Review review = Review.builder()
                .note(data.getNote())
                .ratingOutOfFive(data.getRatingOutOfFive())
                .build();
        booking.setReviewByPassenger(review);
        reviewRepository.save(review);
        bookingRepository.save(booking);
    }
}
