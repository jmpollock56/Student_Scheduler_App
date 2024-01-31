package pollock.student_scheduler_app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.health.SystemHealthManager;
import android.view.LayoutInflater;
import android.view.Menu;
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

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

import Database.AssessmentRepository;
import Database.InstructorRepository;
import ExtraOptions.Alert;
import Model.Assessment;
import Model.Course;
import Model.Instructor;
import ViewUtils.AssessmentViewCreation;

public class CourseExpandedActivity extends AppCompatActivity {

    static Course selectedCourse;
    Assessment selectedAssessment;
    Instructor associatedInstructor;
    InstructorRepository instructorRepository;
    FloatingActionButton addAssessmentBtn;
    String[] assessmentSpinnerOptions = {"Performance Assessment", "Objective Assessment"};
    AssessmentRepository assessmentRepository;
    LinearLayout assessmentLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_expanded);


        assessmentRepository = new AssessmentRepository(getApplication());
        instructorRepository = new InstructorRepository(getApplication());

        assessmentLinearLayout = findViewById(R.id.assessmentList);

        ArrayList<RelativeLayout> assessmentLayouts = loadLocalArrayLists();
        refreshLinearLayout(assessmentLayouts);

        addClickListenerToFAB();
        findInstructor();

        final TextView courseName = findViewById(R.id.courseName);
        final TextView courseStartDate = findViewById(R.id.courseStartDate);
        final TextView courseEndDate = findViewById(R.id.courseEndDate);
        final TextView courseStatus = findViewById(R.id.courseStatus);
        final TextView instructorNameDetail = findViewById(R.id.instructorNameDetail);
        final TextView instructorNumberDetail = findViewById(R.id.instructorNumberDetail);
        final TextView instructorEmailDetail = findViewById(R.id.instructorEmailDetail);
        final TextView courseNotesDetail = findViewById(R.id.courseNoteDetail);


        courseName.setText( String.valueOf(selectedCourse.getTitle()));
        courseStartDate.setText(selectedCourse.getStartDate().toString());
        courseEndDate.setText(selectedCourse.getEndDate().toString());
        courseStatus.setText(selectedCourse.getStatus());
        instructorNameDetail.setText(associatedInstructor.getName());
        instructorNumberDetail.setText(associatedInstructor.getPhoneNumber());
        instructorEmailDetail.setText(associatedInstructor.getEmailAddress());
        courseNotesDetail.setText(selectedCourse.getNote());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public static void getCourseInfo(Course course){
        selectedCourse = course;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_term_expanded, menu);
        return true;
    }

    private void findInstructor(){
        //get Instructor
        for (Instructor instructor: instructorRepository.getmAllInstructors()){
            if (selectedCourse.getInstructorId() == instructor.getId()){
                associatedInstructor = instructor;
            }
        }
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

                if (assessmentName.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please give the assessment a name.", Toast.LENGTH_LONG).show();
                    return;
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
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

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
                AssessmentExpandedActivity.getSelectedAssessment(selectedAssessment, selectedCourse);

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
            if (assessment.getCourseId() == selectedCourse.getId()){
                RelativeLayout relativeLayout = createAssessmentLayout(assessment);
                assessmentLayouts.add(relativeLayout);
            }
        }
        return assessmentLayouts;
    }

    private void showAlertOptions(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.set_alerts, null);

        builder.setView(dialogView);
        builder.setTitle("Alert Options");

        Button startDateAlertBtn = dialogView.findViewById(R.id.startDateAlert);
        Button endDateAlertBtn = dialogView.findViewById(R.id.endDateAlert);


        TextView selectedObject = dialogView.findViewById(R.id.selectedObject);
        selectedObject.setText(selectedCourse.getTitle());

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();

        startDateAlertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int numAlert = random.nextInt(100000000);
                LocalDate chosenStartDate = selectedCourse.getStartDate();
                System.out.println(selectedCourse.getTitle());
                Date chosenDate = java.sql.Date.valueOf(String.valueOf(chosenStartDate));
                System.out.println(chosenDate);
                long trigger = chosenDate.getTime();
                System.out.println(trigger);


                Intent intent = new Intent(CourseExpandedActivity.this, Alert.class);
                intent.setAction("ExtraOptions.Alert.ACTION_ALERT");
                intent.putExtra("key", selectedCourse.getTitle() + " starts today!");
                intent.putExtra("title", "A Course has Started!");
                intent.putExtra("text", selectedCourse.getTitle() + " starts today!");


                PendingIntent sender = PendingIntent.getBroadcast(CourseExpandedActivity.this, numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);

                Toast.makeText(getApplicationContext(), "Alert set for " + selectedCourse.getTitle() + " at " + selectedCourse.getStartDate(), Toast.LENGTH_LONG).show();
                //alertDialog.dismiss();
            }
        });

        endDateAlertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int numAlert = random.nextInt(100000000);
                LocalDate chosenEndDate = selectedCourse.getEndDate();
                Date chosenDate = java.sql.Date.valueOf(String.valueOf(chosenEndDate));
                long trigger = chosenDate.getTime();
                System.out.println(trigger);


                Intent intent = new Intent(CourseExpandedActivity.this, Alert.class);
                intent.setAction("ExtraOptions.Alert.ACTION_ALERT");
                intent.putExtra("key", selectedCourse.getTitle() + " ends today!");
                intent.putExtra("title", "A Course has Ended!");
                intent.putExtra("text", selectedCourse.getTitle() + " ends today!");

                PendingIntent sender = PendingIntent.getBroadcast(CourseExpandedActivity.this, numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);

                Toast.makeText(getApplicationContext(), "Alert set for " + selectedCourse.getTitle() + " at " + selectedCourse.getEndDate(), Toast.LENGTH_LONG).show();


                //alertDialog.dismiss();
            }
        });



        alertDialog.show();
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
        assessmentLinearLayout.removeAllViews();

        for (RelativeLayout assessmentLayout : assessmentLayouts) {
            assessmentLinearLayout.addView(assessmentLayout);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();  // Go back when the back button is pressed
            return true;
        } else if (item.getItemId() == R.id.setCourseAlert) {
            System.out.println("Set Course Alert");
            showAlertOptions();
            return true;

        } else if (item.getItemId() == R.id.shareNotes) {
            Intent sentIntent = new Intent();
            sentIntent.setAction(Intent.ACTION_SEND);
            sentIntent.putExtra(Intent.EXTRA_TEXT, selectedCourse.getNote());
            sentIntent.putExtra(Intent.EXTRA_TITLE, "Note from " + selectedCourse.getTitle());
            sentIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sentIntent, null);
            startActivity(shareIntent);
            System.out.println("Share Notes");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
