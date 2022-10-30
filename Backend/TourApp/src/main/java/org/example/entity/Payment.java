package org.example.entity;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(schema = "dop")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "totalCost")
    private Double totalCost;

    @ManyToOne(targetEntity = CreditCard.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "creditCard")
    private CreditCard creditCardId;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "userId")
    private User user;

    @OneToOne(targetEntity = Itinerary.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "itineraryId")
    private Itinerary itinerary;

    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public CreditCard getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(CreditCard creditCardId) {
        this.creditCardId = creditCardId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", totalCost=" + totalCost +
                ", creditCardId=" + creditCardId +
                ", user=" + user +
                ", itinerary=" + itinerary +
                '}';
    }

    public boolean validCreditCardNumber(String cardNumber){
        int sum = 0;
        boolean  alternateBoolean= false;
        for (int i = cardNumber.length() - 1; i >= 0; i--)
        {
            int num = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternateBoolean)
            {
                num *= 2;
                if (num > 9)
                {
                    num = (num % 10) + 1;
                }
            }
            sum += num;
            alternateBoolean = !alternateBoolean;
        }
        return (sum % 10 == 0);
    }

    public boolean validCVC(String cvc){
        if(cvc.length() != 3)
            return false;
        for (int i = 0; i <cvc.length() ; i++) {
            if(!Character.isDigit(cvc.charAt(i)))
                return false;
        }

        return true;
    }


    public boolean validDate(String creditDate){

        String[] attributes = creditDate.split("/");
        String month = attributes[0];
        String year = attributes[1];
        int yearValue = Integer.parseInt(year);
        int monthValue = Integer.parseInt(month);




        Calendar cal = Calendar.getInstance();
        int currentMonth= cal.get(Calendar.MONTH) + 1;
        int currentYear = cal.get(Calendar.YEAR);
        int lastTwoDigits = currentYear % 100;

        if(lastTwoDigits > yearValue)
            return false;

        return lastTwoDigits != yearValue || currentMonth <= monthValue;
    }

    public boolean validCreditCard(CreditCard userCard){
        return validCreditCardNumber(userCard.getCreditCardNumber()) && validCVC(userCard.getCardCVC()) && validDate(userCard.getExpirationDate());
    }

    public double getItineraryPrice(){
        double totalSum = 0.0;

        List<Park> parkList = getItinerary().getParkList();
        List<ParkRides> parkRidesList = getItinerary().getParkRidesList();

        for(int i = 0; i< parkList.size(); i++){
            totalSum += parkList.get(i).getEntryPrice();
        }

        for(int i = 0; i< parkRidesList.size(); i++){
            totalSum += parkRidesList.get(i).getRides().getPrice();
        }
        return totalSum;
    }
}
