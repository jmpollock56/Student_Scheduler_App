package Model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.ArrayList;
@Entity(tableName = "courses",
        foreignKeys = {
                @ForeignKey(entity = Term.class,
                        parentColumns = "id",
                        childColumns = "termId",
                        onDelete = ForeignKey.RESTRICT),
                @ForeignKey(entity = Instructor.class,
                        parentColumns = "id",
                        childColumns = "instructorId",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(name = "index_termId", value = {"termId"}),
                @Index(name = "index_instructorId", value = {"instructorId"})
        })

public class Course {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private int instructorId; //fk
    private int termId; //fk


    public Course(int id, String title, LocalDate startDate, LocalDate endDate, String status, int instructorId){
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.instructorId = instructorId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setStartDate(LocalDate startDate){
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate){
        this.endDate = endDate;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public void setInstructorId(int instructorId){
        this.instructorId = instructorId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public int getId() {
        return id;
    }

    public String getTitle(){
        return title;
    }

    public LocalDate getStartDate(){
        return startDate;
    }

    public LocalDate getEndDate(){
        return endDate;
    }

    public String getStatus(){
        return status;
    }

    public int getInstructorId(){
        return instructorId;
    }

    public int getTermId() {
        return termId;
    }

}

