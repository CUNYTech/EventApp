package Model;

import java.util.ArrayList;

/**
 * Created by napti on 11/29/2017.
 */

public class Events {

    private String start, destination, date, time, name, image, id;
    private ArrayList<String> lines;

    public Events() {
    }

    public Events(String start, String destination, String date, String time, String name,
                  String image,  ArrayList<String> lines, String id) {
        this.start = start;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.name = name;
        this.image = image;
        this.lines = lines;
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getLines() {
        return lines;
    }

    public void setLines(ArrayList<String> lines) {
        this.lines = lines;
    }
}


