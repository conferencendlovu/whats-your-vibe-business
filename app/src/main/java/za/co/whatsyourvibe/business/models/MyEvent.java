package za.co.whatsyourvibe.business.models;

import java.io.Serializable;

public class MyEvent  implements Serializable {

    private String id;

    private String name;

    private String category;

    private String description;

    private String location;

    private double latitude;

    private double longitude;

    private String poster;

    private String image1;

    private String image2;

    private String image3;

    private String time;

    private String date;

    private String ageRestricted;

    private String smoking;

    private String children;

    private String alcohol;

    private boolean isPaidEvent;

    private double eventEntryFee;

    public double getStandardTicket() {
        return standardTicket;
    }

    public void setStandardTicket(double standardTicket) {
        this.standardTicket = standardTicket;
    }

    public double getEarlyBirdTicket() {
        return earlyBirdTicket;
    }

    public void setEarlyBirdTicket(double earlyBirdTicket) {
        this.earlyBirdTicket = earlyBirdTicket;
    }

    public double getGroupTicket() {
        return groupTicket;
    }

    public void setGroupTicket(double groupTicket) {
        this.groupTicket = groupTicket;
    }

    public double getVipTicket() {
        return vipTicket;
    }

    public void setVipTicket(double vipTicket) {
        this.vipTicket = vipTicket;
    }

    private double standardTicket;

    private double earlyBirdTicket;

    private double groupTicket;

    private double vipTicket;

    private String eventPrivacy;

    private String creatorUid;

    private String creatorDisplayName;

    private int going;

    private int shares;

    public MyEvent() {
        // empty constructor needed
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAgeRestricted() {
        return ageRestricted;
    }

    public void setAgeRestricted(String ageRestricted) {
        this.ageRestricted = ageRestricted;
    }


    public boolean isPaidEvent() {
        return isPaidEvent;
    }

    public void setPaidEvent(boolean paidEvent) {
        isPaidEvent = paidEvent;
    }

    public double getEventEntryFee() {
        return eventEntryFee;
    }

    public void setEventEntryFee(double eventEntryFee) {
        this.eventEntryFee = eventEntryFee;
    }

    public String getEventPrivacy() {
        return eventPrivacy;
    }

    public void setEventPrivacy(String eventPrivacy) {
        this.eventPrivacy = eventPrivacy;
    }

    public String getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(String creatorUid) {
        this.creatorUid = creatorUid;
    }

    public String getCreatorDisplayName() {
        return creatorDisplayName;
    }

    public void setCreatorDisplayName(String creatorDisplayName) {
        this.creatorDisplayName = creatorDisplayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGoing() {
        return going;
    }

    public void setGoing(int going) {
        this.going = going;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getSmoking() {
        return smoking;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(String alcohol) {
        this.alcohol = alcohol;
    }
}
