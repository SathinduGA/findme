package dim.mobilecomputing.findme.models;

/**
 * Created by Sathindu on 2016-07-22.
 */
public class LocationData {
    private double latitude = 6.9344;
    private double longitude = 79.8428;

    public LocationData(){

    }

    public LocationData(double latitude, double longitude){
        this.setLatitude(latitude);
        this.setLongitude(longitude);
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
}
