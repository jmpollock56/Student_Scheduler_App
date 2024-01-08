package pollock.student_scheduler_app;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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

import DAO.TermDAO;
import Database.Repository;
import Database.StudentData;
import Model.Term;


public class MainActivity extends AppCompatActivity {

    private Repository repository;
    StudentData database;
    TermDAO termDAO;



    private ArrayList<Term> allTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Room.databaseBuilder(this, StudentData.class, "StudentScheduleDatabase.db")
                .build();
        termDAO = database.termDAO();
        repository = new Repository(getApplication());

        allTerms = repository.getmAllTerms();
        for (Term term: allTerms){
            Term.addTerm(term);
        }



        FloatingActionButton addTermButton = findViewById(R.id.floatingActionButton);

        addTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTermDialog();
            }
        });


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

                Term newTerm = new Term(0, termTitle, startDate, endDate);
                repository.insert(newTerm);
                Log.d("Terms", repository.getmAllTerms().toString());

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
}
