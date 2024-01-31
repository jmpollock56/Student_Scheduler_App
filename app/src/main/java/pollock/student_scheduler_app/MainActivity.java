package pollock.student_scheduler_app;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import DAO.TermDAO;
import Database.CourseRepository;
import Database.TermRepository;
import Database.StudentData;
import Model.Course;
import Model.Term;
import ViewUtils.TermViewCreation;


public class MainActivity extends AppCompatActivity {

    private TermRepository termRepository;
    private CourseRepository courseRepository;
    StudentData database;
    TermDAO termDAO;
    FloatingActionButton addTermButton;
    Term selectedTerm;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = findViewById(R.id.termContainer);
        courseRepository = new CourseRepository(getApplication());

        setDatabaseItems();

        ArrayList<RelativeLayout> termLayouts = loadLocalArrayLists();
        refreshLinearLayout(termLayouts);

        addClickListenerToFAB();

    }



    private void showAddTermDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.term_input, null);
        final EditText editTextTerm = dialogView.findViewById(R.id.editTextTerm);
        final DatePicker startDatePicker = dialogView.findViewById(R.id.startDatePicker);
        final DatePicker endDatePicker = dialogView.findViewById(R.id.endDatePicker);

        builder.setView(dialogView);
        builder.setTitle("Add Term");
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                String termTitle = editTextTerm.getText().toString();

                if (termTitle.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter a Term Name", Toast.LENGTH_LONG).show();
                    return;
                }

                int startYear = startDatePicker.getYear();
                int startMonth = startDatePicker.getMonth() + 1;
                int startDayOfMonth = startDatePicker.getDayOfMonth();

                LocalDate startDate = LocalDate.of(startYear, startMonth, startDayOfMonth);

                int endYear = endDatePicker.getYear();
                int endMonth = endDatePicker.getMonth() + 1;
                int endDayOfMonth = endDatePicker.getDayOfMonth();

                LocalDate endDate = LocalDate.of(endYear, endMonth, endDayOfMonth);

                if (startDate.isAfter(endDate)){
                    Toast.makeText(getApplicationContext(), "The End Date should be after the Start Date", Toast.LENGTH_LONG).show();
                    return;
                }

                Random random = new Random();

                int termId = random.nextInt(10000);

                Term newTerm = new Term(termId, termTitle, startDate, endDate);
                termRepository.insertTerm(newTerm);


                RelativeLayout newTermLayout = TermViewCreation.createTermRelativeLayout(MainActivity.this, newTerm);

                setTermClickListeners(newTermLayout, newTerm);


                linearLayout.addView(newTermLayout);

                dialog.dismiss();
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
        termRepository = new TermRepository(getApplication());
    }

    private ArrayList<RelativeLayout> loadLocalArrayLists() {
        ArrayList<RelativeLayout> termLayouts = new ArrayList<>();

        for (Term term : termRepository.getmAllTerms()) {
            RelativeLayout relativeLayout = createTermLayout(term);
            termLayouts.add(relativeLayout);
        }

        return termLayouts;


    }

    private void showTermOptions(Term term){

        selectedTerm = term;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.term_options, null);

        builder.setView(dialogView);
        builder.setTitle("Term Options");

        Button viewTermBtn = dialogView.findViewById(R.id.viewTermBtn);
        Button editTermBtn = dialogView.findViewById(R.id.editTermBtn);
        Button deleteTermBtn = dialogView.findViewById(R.id.deleteTermBtn);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();

        viewTermBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermExpandedActivity.getTermInfo(selectedTerm);

                Intent intent = new Intent(MainActivity.this, TermExpandedActivity.class);
                startActivity(intent);

                alertDialog.dismiss();
            }
        });

        deleteTermBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (Course course: courseRepository.getmAllCourses()){
                    if (selectedTerm.getId() == course.getTermId()){
                        Toast.makeText(getApplicationContext(), "Cannot delete Term that has associated Courses", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                termRepository.deleteTerm(selectedTerm);
                alertDialog.dismiss();

                // Reload the LinearLayout with the updated data
                ArrayList<RelativeLayout> updatedTermLayouts = loadLocalArrayLists();
                refreshLinearLayout(updatedTermLayouts);

            }
        });

        editTermBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermEditActivity.getSelectedTerm(selectedTerm);

                Intent intent = new Intent(MainActivity.this, TermEditActivity.class);
                startActivity(intent);

                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void setTermClickListeners(RelativeLayout rL, final Term term){
        rL.setTag(term);
        rL.setId(term.getId());
        rL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Term term = (Term) v.getTag();
                showTermOptions(term);

            }
        });
    }

    private RelativeLayout createTermLayout(Term term) {
        RelativeLayout relativeLayout = TermViewCreation.createTermRelativeLayout(this, term);
        setTermClickListeners(relativeLayout, term);
        relativeLayout.setTag(term);
        return relativeLayout;
    }

    private void refreshLinearLayout(ArrayList<RelativeLayout> termLayouts) {
        linearLayout.removeAllViews(); // Remove all views from the LinearLayout

        // Re-add the updated views
        for (RelativeLayout termLayout : termLayouts) {
            linearLayout.addView(termLayout);
        }
    }





}
