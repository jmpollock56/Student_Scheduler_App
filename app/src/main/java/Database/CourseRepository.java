package Database;

import android.app.Application;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import DAO.CourseDAO;
import DAO.TermDAO;
import Model.Course;


public class CourseRepository {

    private CourseDAO mCourseDAO;
    private ArrayList<Course> mAllCourses;
    private Course course;
    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public CourseRepository(Application application){
        StudentData db = StudentData.getDatabase(application);
        mCourseDAO = db.courseDAO();
    }

    public ArrayList<Course> getmAllCourses(){
        databaseExecutor.execute(() -> {
            mAllCourses = (ArrayList<Course>) mCourseDAO.getAllCourses();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllCourses;
    }

    public void insertCourse(Course course){
        databaseExecutor.execute(() ->{
            mCourseDAO.insert(course);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateCourse(Course course){
        databaseExecutor.execute(() ->{
            mCourseDAO.update(course);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCourse(Course course){
        databaseExecutor.execute(() ->{
            mCourseDAO.delete(course);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
