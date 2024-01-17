package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import DAO.AssessmentDAO;
import DAO.CourseDAO;
import DAO.InstructorDAO;
import DAO.TermDAO;
import Model.Assessment;
import Model.Instructor;
import Model.Term;
import Model.Course;

@Database(entities = {Term.class, Course.class, Assessment.class, Instructor.class}, version = 8)
@TypeConverters(DateConverter.class)
public abstract class StudentData extends RoomDatabase {
    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();
    public abstract InstructorDAO instructorDAO();
    private static volatile StudentData INSTANCE;


    static StudentData getDatabase(final Context context) {
        if(INSTANCE == null){
            synchronized (StudentData.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), StudentData.class, "StudentScheduleDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
