package pollock.student_scheduler_app;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import DAO.TermDAO;
import Database.Repository;
import Database.StudentData;
import Model.Term;
import ViewUtils.ViewCreations;


public class MainActivity extends AppCompatActivity {

    private Repository repository;
    StudentData database;
    TermDAO termDAO;
    FloatingActionButton addTermButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setDatabaseItems(); // setting up db items
        loadLocalArrayLists(); // load data from db into local storage

        LinearLayout linearLayout = findViewById(R.id.termContainer);

        for(Term term: Term.getTerms()){
           RelativeLayout relativeLayout = ViewCreations.createTermRelativeLayout(this, term);

            setTermClickListeners(relativeLayout, term);

            relativeLayout.setTag(term);
            linearLayout.addView(relativeLayout);
        }

        addClickListenerToFAB(); // add click listener to FloatingActionButton

    }

    private void showAddTermDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.term_input, null);
        final EditText editTextTerm = dialogView.findViewById(R.id.editTextTerm);
        final TextView startDateText = dialogView.findViewById(R.id.startDateText);
        final DatePicker startDatePicker = dialogView.findViewById(R.id.startDatePicker);
        final TextView endDateText = dialogView.findViewById(R.id.endDateText);
        final DatePicker endDatePicker = dialogView.findViewById(R.id.endDatePicker);

        builder.setView(dialogView);
        builder.setTitle("Add Term");
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String termTitle = editTextTerm.getText().toString();

                int startYear = startDatePicker.getYear();
                int startMonth = startDatePicker.getMonth() + 1;
                int startDayOfMonth = startDatePicker.getDayOfMonth();

                LocalDate startDate = LocalDate.of(startYear, startMonth, startDayOfMonth);

                int endYear = endDatePicker.getYear();
                int endMonth = endDatePicker.getMonth() + 1;
                int endDayOfMonth = endDatePicker.getDayOfMonth();

                LocalDate endDate = LocalDate.of(endYear, endMonth, endDayOfMonth);

                Random random = new Random();

                int termId = random.nextInt(10000);

                Term newTerm = new Term(termId, termTitle, startDate, endDate);
                repository.insert(newTerm);
                loadLocalArrayLists();

                // Create a new RelativeLayout for the newly added term
                RelativeLayout newTermLayout = ViewCreations.createTermRelativeLayout(MainActivity.this, newTerm);

               setTermClickListeners(newTermLayout, newTerm);

                // Add the new RelativeLayout to the LinearLayout
                LinearLayout linearLayout = findViewById(R.id.termContainer);
                linearLayout.addView(newTermLayout);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void addClickListenerToFAB(){
        addTermButton = findViewById(R.id.floatingActionButton);
        addTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTermDialog();
            }
        });
    }

    private void setDatabaseItems(){
        database = Room.databaseBuilder(this, StudentData.class, "StudentScheduleDatabase.db")
                .build();
        termDAO = database.termDAO();
        repository = new Repository(getApplication());
    }

    private void loadLocalArrayLists(){

        for (Term term: repository.getmAllTerms()){
            Term.addTerm(term);
        }

    }

    private void setTermClickListeners(RelativeLayout rL, final Term term){
        rL.setTag(term);
        rL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Term selectedTerm = (Term) v.getTag();
                TermExpandedActivity.getTermInfo(selectedTerm);

                Intent intent = new Intent(MainActivity.this, TermExpandedActivity.class);
                startActivity(intent);

                Log.d("Term", String.valueOf(selectedTerm.getId()));
            }
        });
    }

}
