package Database;

import android.app.Application;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import DAO.AssessmentDAO;
import DAO.CourseDAO;
import Model.Assessment;
import Model.Course;

public class AssessmentRepository {
    private AssessmentDAO mAssessmentDAO;
    private ArrayList<Assessment> mAllAssessments;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public AssessmentRepository(Application application){
        StudentData db = StudentData.getDatabase(application);
        mAssessmentDAO = db.assessmentDAO();
    }

    public ArrayList<Assessment> getmAllAssessments(){
        databaseExecutor.execute(() -> {
            mAllAssessments = (ArrayList<Assessment>) mAssessmentDAO.getAllAssessments();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllAssessments;
    }

    public void insertAssessment(Assessment assessment){
        databaseExecutor.execute(() ->{
            mAssessmentDAO.insert(assessment);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateAssessment(Assessment assessment){
        databaseExecutor.execute(() ->{
            mAssessmentDAO.update(assessment);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAssessment(Assessment assessment){
        databaseExecutor.execute(() ->{
            mAssessmentDAO.delete(assessment);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
