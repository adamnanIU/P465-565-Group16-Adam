package org.example.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "dop")
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "userId")
    private User user;

    @ElementCollection
    @Column(name = "parkList")
    private List<Park> parkList;


    @ElementCollection
    @Column(name = "listOfParkRides")
    private List<ParkRides> parkRidesList;


    public Integer getItineraryID() {
        return id;
    }

    public void setItineraryID(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Park> getParkList() {
        return parkList;
    }

    public void setParkList(List<Park> parkList) {
        this.parkList = parkList;
    }

    public List<ParkRides> getParkRidesList() {
        return parkRidesList;
    }

    public void setParkRidesList(List<ParkRides> parkRidesList) {
        this.parkRidesList = parkRidesList;
    }

    @Override
    public String toString() {
        return "Itinerary{" +
                "itineraryID=" + id +
                ", user=" + user +
                ", parkList=" + parkList +
                ", parkRidesList=" + parkRidesList +
                '}';
    }

}
