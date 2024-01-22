package pollock.student_scheduler_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

import Database.AssessmentRepository;
import Database.CourseRepository;
import Database.InstructorRepository;
import Model.Assessment;
import Model.Course;
import Model.Instructor;
import Model.Term;
import ViewUtils.AssessmentViewCreation;
import ViewUtils.CourseViewCreation;
import ViewUtils.TermViewCreation;

public class CourseExpandedActivity extends AppCompatActivity {

    static Course selectedCourse;
    Assessment selectedAssessment;
    FloatingActionButton addAssessmentBtn;
    String[] assessmentSpinnerOptions = {"Performance Assessment", "Objective Assessment"};
    AssessmentRepository assessmentRepository;
    LinearLayout assessmentLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_expanded);
        RelativeLayout relativeLayout = findViewById(R.id.header);

        assessmentRepository = new AssessmentRepository(getApplication());

        assessmentLinearLayout = findViewById(R.id.assessmentList);

        ArrayList<RelativeLayout> assessmentLayouts = loadLocalArrayLists();
        refreshLinearLayout(assessmentLayouts);

        addClickListenerToFAB();

        final TextView courseName = relativeLayout.findViewById(R.id.courseName);
        final TextView courseStartDate = relativeLayout.findViewById(R.id.courseStartDate);
        final TextView courseEndDate = relativeLayout.findViewById(R.id.courseEndDate);

        courseName.setText( String.valueOf(selectedCourse.getTitle()));
        courseStartDate.setText("Start Date: \n" +selectedCourse.getStartDate());
        courseEndDate.setText("End Date: \n" + selectedCourse.getEndDate());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public static void getCourseInfo(Course course){
        selectedCourse = course;
    }

    private void addClickListenerToFAB(){
        addAssessmentBtn = findViewById(R.id.addAssessment);

        addAssessmentBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) { showAddAssessmentDialog(); }
        }));
    }

    public void showAddAssessmentDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.assessment_input, null);

        Spinner assessmentTypeSpinner = dialogView.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, assessmentSpinnerOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assessmentTypeSpinner.setAdapter(adapter);

        builder.setView(dialogView);
        builder.setTitle("Add Assessment");
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {


                final EditText assessmentName = dialogView.findViewById(R.id.assessmentTitleInput);
                final DatePicker assessmentEndDate = dialogView.findViewById(R.id.assessmentEndDate);
                String assessmentType = assessmentTypeSpinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(assessmentName.getText().toString())) {

                    Toast.makeText(getApplicationContext(), "Please enter the assessment name", Toast.LENGTH_SHORT).show();

                }


                int endYear = assessmentEndDate.getYear();
                int endMonth = assessmentEndDate.getMonth() + 1;
                int endDayOfMonth = assessmentEndDate.getDayOfMonth();

                LocalDate assessmentEnd = LocalDate.of(endYear, endMonth, endDayOfMonth);

                Random random = new Random();

                int assessmentId = random.nextInt(10000);
                Assessment newAssessment = new Assessment(assessmentId, assessmentType, assessmentName.getText().toString(), assessmentEnd, selectedCourse.getId());

                assessmentRepository.insertAssessment(newAssessment);



                RelativeLayout newAssessmentLayout = AssessmentViewCreation.createAssessmentRelativeLayout(CourseExpandedActivity.this, newAssessment);
                setAssessmentClickListeners(newAssessmentLayout, newAssessment);

                assessmentLinearLayout.addView(newAssessmentLayout);


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

    private void showAssessmentOptions(Assessment assessment){

        selectedAssessment = assessment;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.assessment_options, null);

        builder.setView(dialogView);
        builder.setTitle("Assessment Options");

        Button viewAssessment = dialogView.findViewById(R.id.viewAssBtn);
        Button editAssessment = dialogView.findViewById(R.id.editAssBtn);
        Button deleteAssessment = dialogView.findViewById(R.id.deleteAssBtn);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();

        viewAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AssessmentExpandedActivity.getSelectedAssessment(selectedAssessment);

                Intent intent = new Intent(CourseExpandedActivity.this, AssessmentExpandedActivity.class);
                startActivity(intent);

                alertDialog.dismiss();
            }
        });

        editAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AssessmentEditActivity.getSelectedAssessment(assessment);

                Intent intent = new Intent(CourseExpandedActivity.this, AssessmentEditActivity.class);
                startActivity(intent);

                alertDialog.dismiss();
            }
        });

        deleteAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assessmentRepository.deleteAssessment(selectedAssessment);
                alertDialog.dismiss();

                ArrayList<RelativeLayout> assessmentLayouts = loadLocalArrayLists();
                refreshLinearLayout(assessmentLayouts);
            }
        });
        alertDialog.show();
    }


    private ArrayList<RelativeLayout> loadLocalArrayLists() {
        ArrayList<RelativeLayout> assessmentLayouts = new ArrayList<>();

        for (Assessment assessment : assessmentRepository.getmAllAssessments()) {
            RelativeLayout relativeLayout = createAssessmentLayout(assessment);
            assessmentLayouts.add(relativeLayout);
        }

        return assessmentLayouts;


    }

    private void setAssessmentClickListeners(RelativeLayout rL, final Assessment assessment){
        rL.setTag(assessment);
        rL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Assessment deleteAssessment = (Assessment) v.getTag();
                showAssessmentOptions(deleteAssessment);
            }
        });
    }

    private RelativeLayout createAssessmentLayout(Assessment assessment) {
        RelativeLayout relativeLayout = AssessmentViewCreation.createAssessmentRelativeLayout(this, assessment);
        setAssessmentClickListeners(relativeLayout, assessment);
        relativeLayout.setTag(assessment);
        return relativeLayout;
    }

    private void refreshLinearLayout(ArrayList<RelativeLayout> assessmentLayouts) {
        assessmentLinearLayout.removeAllViews(); // Remove all views from the LinearLayout

        // Re-add the updated views
        for (RelativeLayout assessmentLayout : assessmentLayouts) {
            assessmentLinearLayout.addView(assessmentLayout);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();  // Go back when the back button is pressed
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
