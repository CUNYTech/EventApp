package Model;

/**
 * Created by junhaochen on 10/31/17.
 */

public class UserInformation {
    private String age;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String image;
    private float totalRating;
    private int ratedCounter;

    public UserInformation() {}

    public float getTotalRating() { return totalRating;}

    public void setTotalRating(float totalRating) {this.totalRating = totalRating;}

    public int getRatedCounter() {return ratedCounter;}

    public void setRatedCounter(int ratedCounter) {this.ratedCounter = ratedCounter;}

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
