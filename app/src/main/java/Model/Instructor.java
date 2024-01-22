package Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "instructors")
public class Instructor {

    @PrimaryKey()
    private int id;
    private String name;
    private String phoneNumber;
    private String emailAddress;


    private static ArrayList<Instructor> allInstructors = new ArrayList<>();

    public Instructor(int id, String name, String phoneNumber, String emailAddress){
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public void setEmailAddress(String emailAddress){
        this.emailAddress = emailAddress;
    }

    public static void addInstructor(Instructor instructor){
        allInstructors.add(instructor);
    }

    public String getName(){
        return name;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public int getId() {
        return id;
    }

    public static ArrayList<Instructor> getAllInstructors(){
        return allInstructors;
    }
}
