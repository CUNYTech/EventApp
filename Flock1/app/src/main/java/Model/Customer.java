package Model;

/**
 * Created by napti on 10/8/2017.
 */

public class Customer {
    private int id;
    private String email_address;
    private String first_name;
    private String last_name;
    private String password;
    private String gender;
    private int age;


    public Customer() {
    }

    public Customer(int id, String email_address, String first_name, String last_name,
                    String password, String gender, int age) {
        this.id = id;
        this.email_address = email_address;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.gender = gender;
        this.age = age;
    }

    public Customer(String email_address, String first_name, String last_name,
                    String password, String gender, int age) {

        this.email_address = email_address;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.gender = gender;
        this.age = age;
    }

    public Customer(String email_address, String first_name, String last_name,
                    String password, String gender) {

        this.email_address = email_address;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.gender = gender;
    }

    public Customer(String email_address, String first_name, String last_name,
                    String password) {

        this.email_address = email_address;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
    }

    public Customer(String email_address, String first_name, String last_name) {

        this.email_address = email_address;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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
}