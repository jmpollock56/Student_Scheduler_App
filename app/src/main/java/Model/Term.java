package Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Database.TermRepository;

@Entity(tableName = "terms")
public class Term {
    @PrimaryKey()
    private int id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

    private static ArrayList<Term> allTerms = new ArrayList<>();

    public Term(int id, String title, LocalDate startDate, LocalDate endDate){
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public static void addTerm(Term newTerm){
        allTerms.add(newTerm);
    }


    public int getId(){
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

    public static ArrayList<Term> getTerms(){
        return allTerms;
    }

}
