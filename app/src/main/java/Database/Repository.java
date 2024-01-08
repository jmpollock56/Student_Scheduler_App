package Database;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import DAO.TermDAO;
import Model.Term;

public class Repository {
    private TermDAO mTermDAO;
    private ArrayList<Term> mAllTerms;
    private Term term;
    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application){
        StudentData db = StudentData.getDatabase(application);
        mTermDAO = db.termDAO();
    }

    public ArrayList<Term> getmAllTerms(){
        databaseExecutor.execute(() -> {
            mAllTerms = (ArrayList<Term>) mTermDAO.getAllTerms();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllTerms;
    }

    public void insert(Term term){
        databaseExecutor.execute(() ->{
            mTermDAO.insert(term);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Term term){
        databaseExecutor.execute(() ->{
            mTermDAO.update(term);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Term term){
        databaseExecutor.execute(() ->{
            mTermDAO.delete(term);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
