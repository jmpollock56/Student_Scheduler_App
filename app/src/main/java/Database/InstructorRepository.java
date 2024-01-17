package Database;

import android.app.Application;

import androidx.room.Insert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import DAO.CourseDAO;
import DAO.InstructorDAO;
import Model.Course;
import Model.Instructor;

public class InstructorRepository {

    private InstructorDAO mInstructorDAO;
    private ArrayList<Instructor> mAllInstructors;
    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public InstructorRepository(Application application){
        StudentData db = StudentData.getDatabase(application);
        mInstructorDAO = db.instructorDAO();
    }

    public ArrayList<Instructor> getmAllInstructors(){
        databaseExecutor.execute(() -> {
            mAllInstructors = (ArrayList<Instructor>) mInstructorDAO.getAllInstructors();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllInstructors;
    }

    public void insertInstructor(Instructor instructor){
        databaseExecutor.execute(() ->{
            mInstructorDAO.insert(instructor);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateInstructor(Instructor instructor){
        databaseExecutor.execute(() ->{
            mInstructorDAO.update(instructor);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteInstructor(Instructor instructor){
        databaseExecutor.execute(() ->{
            mInstructorDAO.delete(instructor);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
