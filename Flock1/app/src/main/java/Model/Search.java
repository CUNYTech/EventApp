package Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by pprickle on 10/22/17.
 */

//if object, we can collect and store searches to form a history
public class Search implements Parcelable {
    private String start;
    private String destination;
    private ArrayList<String> lines;
    private String date;
    private String time;
    private String name;
    private String image;
    private String id;

    public Search() {}

    public Search(String s, String d, ArrayList<String> ls, String da, String t, String name,
                  String image, String id) {
        start = s;
        destination = d;
        lines = ls;
        date = d;
        time = t;
        this.name = name;
        this.image = image;
        this.id = id;

    }

    //write object values to parcel for storage
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(start);
        dest.writeString(destination);
        dest.writeSerializable(lines);
        dest.writeString(date);
        dest.writeString(time);
    }

    //constructor used for parcel
    public Search(Parcel parcel){
        start = parcel.readString();
        destination = parcel.readString();
        lines = (ArrayList<String>) parcel.readSerializable();
        date = parcel.readString();
        time = parcel.readString();
    }

    //creator - used when un-parceling our parcel (creating the object)
    public static final Parcelable.Creator<Search> CREATOR = new Parcelable.Creator<Search>(){

        @Override
        public Search createFromParcel(Parcel parcel) {
            return new Search(parcel);
        }

        @Override
        public Search[] newArray(int size) {
            return new Search[0];
        }
    };

    //return hashcode of object
    public int describeContents() {
        return hashCode();
    }

    public String getStart() {return start;}
    public void setStart(String s) {start = s;}

    public String getDestination() {return destination;}
    public void setDestination(String d) {destination = d;}

    public ArrayList<String> getLines() {return lines;}
    public void setlines(ArrayList<String> ls) {lines = ls;}

    public String getDate() {return date;}
    public void setDate(String da) {date = da;}

    public String getTime() {return time;}
    public void setTime(String t) {time = t;}

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
}