package org.example.controller;


import org.example.entity.Itinerary;
import org.example.entity.Park;
import org.example.entity.ParkRides;
import org.example.repo.ItineraryRepository;
import org.example.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/itinerary")
public class ItineraryController {


    // ask about autowired

   private ItineraryService itineraryService;

    @PostMapping("/{Park}")
    public ResponseEntity addPark(Itinerary itinerary, @PathVariable("Park") Park park) throws Exception{
        itineraryService.addPark(itinerary, park);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }


    @PostMapping("/{ParkRide}")
    public ResponseEntity addParkRide(Itinerary itinerary, @PathVariable("ParkRide")ParkRides ParkRide) throws Exception{
        itineraryService.addParkRide(itinerary, ParkRide);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }


    @PostMapping("/delete/{Park}")
    public ResponseEntity deletePark(Itinerary itinerary, @PathVariable("Park") Park park) throws Exception{
        itineraryService.deletePark(itinerary, park);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/delete/{ParkRide}")
    public ResponseEntity deleteParkRide(Itinerary itinerary, @PathVariable("ParkRide")ParkRides ParkRide) throws Exception{
        itineraryService.deleteParkRide(itinerary, ParkRide);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }



}
