package Model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "assessments",
        foreignKeys = @ForeignKey(entity = Course.class,
                parentColumns = "id",
                childColumns = "courseId",
                onDelete = ForeignKey.RESTRICT))

public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type;
    private String title;
    private LocalDate endDate;
    private int courseId; //fk

    public Assessment(int id, String type, String title, LocalDate endDate, int courseId){
        this.id = id;
        this.type = type;
        this.title = title;
        this.endDate = endDate;
        this.courseId = courseId;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setEndDate(LocalDate endDate){
        this.endDate = endDate;
    }

    public void setCourseId(int courseId){
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getCourseId() {
        return courseId;
    }
}
