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
    private String time;
    private String date;
    private String ageRestricted;
    private boolean smokingAllowed;
    private boolean childrenAllowed;
    private boolean alcoholAllowed;
    private boolean isPaidEvent;
    private double eventEntryFee;
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

    public boolean isSmokingAllowed() {
        return smokingAllowed;
    }

    public void setSmokingAllowed(boolean smokingAllowed) {
        this.smokingAllowed = smokingAllowed;
    }

    public boolean isChildrenAllowed() {
        return childrenAllowed;
    }

    public void setChildrenAllowed(boolean childrenAllowed) {
        this.childrenAllowed = childrenAllowed;
    }

    public boolean isAlcoholAllowed() {
        return alcoholAllowed;
    }

    public void setAlcoholAllowed(boolean alcoholAllowed) {
        this.alcoholAllowed = alcoholAllowed;
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
}
