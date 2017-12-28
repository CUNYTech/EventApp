package Model;

/**
 * Created by napti on 12/27/2017.
 */

public class Markers {

    private String name;
    private double longitude;
    private double lattitude;
    private String lines;

    public Markers() {
    }

    public Markers(String name, double longitude, double lattitude, String lines) {
        this.name = name;
        this.longitude = longitude;
        this.lattitude = lattitude;
        this.lines = lines;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public String getLines() {
        return lines;
    }

    public void setLines(String lines) {
        this.lines = lines;
    }
}
