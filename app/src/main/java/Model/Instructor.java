package Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "instructors")
public class Instructor {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String phoneNumber;
    private String emailAddress;

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
}
