package Model;

/**
 * Created by napti on 10/8/2017.
 */

public class Customer {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String gender;
    private int age;
    private String image;


    public Customer() {
    }
    public Customer(int id, String email, String firstName, String lastName,
                    String password, String gender, int age) {

        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.id = id;
    }

    public Customer(String image, String email, String firstName, String lastName,
                    String password, String gender, int age) {
        this.image = image;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.gender = gender;
        this.age = age;
    }


    public Customer(String email, String firstName, String lastName,
                    String password, String gender, int age) {

        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.gender = gender;
        this.age = age;
    }



    public Customer(String email, String firstName, String lastName,
                    String password, String gender) {

        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.gender = gender;
    }

    public Customer(String email, String firstName, String lastName,
                    String password) {

        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getfirstName() {
        return firstName;
    }

    public void setfirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getlastName() {
        return lastName;
    }

    public void setlastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}