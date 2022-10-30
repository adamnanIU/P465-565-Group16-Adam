package org.example.service;

import org.example.entity.Itinerary;
import org.example.entity.Park;
import org.example.entity.ParkRides;
import org.example.exception.InvalidItinerary;
import org.example.repo.ItineraryRepository;
import org.example.repo.ParkRepository;
import org.example.repo.ParkRidesRepository;
import org.example.repo.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ItineraryService {


    @Autowired
    private ItineraryRepository itineraryRepository;



    //check if itinerary is null
    public void addPark(Itinerary itinerary, Park park) throws InvalidItinerary {
        Itinerary itinerary1 = itineraryRepository.findByItineraryId(itinerary.getItineraryID());
        if(itinerary1 == null){
            List<Park> parkList = new ArrayList<>();
            parkList.add(park);
            itinerary.setParkList(parkList);
            itineraryRepository.save(itinerary);

        }
        else{
            boolean parkAlreadyAdded = false;
            List<Park> parkList = itinerary1.getParkList();
            String parkName = park.getParkName();
            for (int i = 0; i < parkList.size(); i++) {
                if(parkList.get(i).getParkName().equals(parkName))
                    parkAlreadyAdded = true;
            }
            if(parkAlreadyAdded)
                throw new InvalidItinerary("Park already in itinerary");

            parkList.add(park);
            itinerary.setParkList(parkList);
            itineraryRepository.save(itinerary);

        }

    }


    //add park ride

    public void addParkRide(Itinerary itinerary, ParkRides parkRides) throws InvalidItinerary {
        Itinerary itinerary1 = itineraryRepository.findByItineraryId(itinerary.getItineraryID());
        if(itinerary1 == null){
            List<ParkRides> parkRidesList = new ArrayList<>();
            parkRidesList.add(parkRides);
            itinerary.setParkRidesList(parkRidesList);
            itineraryRepository.save(itinerary);

        }
        else{
            boolean parkRideAlreadyAdded = false;
            List<ParkRides> parkList = itinerary1.getParkRidesList();
            String parkRideName = parkRides.getRides().getName();
            for (int i = 0; i < parkList.size(); i++) {
                if(parkList.get(i).getRides().getName().equals(parkRideName))
                    parkRideAlreadyAdded = true;
            }
            if(parkRideAlreadyAdded)
                throw new InvalidItinerary("Ride already in itinerary");

            parkList.add(parkRides);
            itinerary.setParkRidesList(parkList);
            itineraryRepository.save(itinerary);

        }

    }

    //delete park
    //check if itinerary is null
    public void deletePark(Itinerary itinerary, Park park) throws InvalidItinerary {
        Itinerary itinerary1 = itineraryRepository.findByItineraryId(itinerary.getItineraryID());
        if(itinerary1 == null){
            throw new InvalidItinerary("Your itinerary is empty. You cannot remove this park");
        }
        else{
            boolean parkAlreadyAdded = false;
            List<Park> parkList = itinerary1.getParkList();
            String parkName = park.getParkName();
            for (int i = 0; i < parkList.size(); i++) {
                if(parkList.get(i).getParkName().equals(parkName)) {
                    parkList.remove(i);
                    parkAlreadyAdded = true;
                }
            }
            if(!parkAlreadyAdded)
                throw new InvalidItinerary("Park is not in itinerary");


            itinerary.setParkList(parkList);
            itineraryRepository.save(itinerary);

        }

    }





    //delete parkride


    public void deleteParkRide(Itinerary itinerary, ParkRides parkRides) throws InvalidItinerary {
        Itinerary itinerary1 = itineraryRepository.findByItineraryId(itinerary.getItineraryID());
        if(itinerary1 == null){
            throw new InvalidItinerary("Your itinerary is empty. You cannot remove this park");

        }
        else{
            boolean parkRideAlreadyAdded = false;
            List<ParkRides> parkList = itinerary1.getParkRidesList();
            String parkRideName = parkRides.getRides().getName();
            for (int i = 0; i < parkList.size(); i++) {
                if (parkList.get(i).getRides().getName().equals(parkRideName)) {
                    parkRideAlreadyAdded = true;
                    parkList.remove(i);
                }
            }
            if(!parkRideAlreadyAdded)
                throw new InvalidItinerary("Ride not in itinerary");


            itinerary.setParkRidesList(parkList);
            itineraryRepository.save(itinerary);

        }

    }







}
