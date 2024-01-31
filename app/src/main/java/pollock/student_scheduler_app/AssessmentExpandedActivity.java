package pollock.student_scheduler_app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Random;

import Database.AssessmentRepository;
import ExtraOptions.Alert;
import ExtraOptions.AssessmentAlert;
import Model.Assessment;
import Model.Course;
import ViewUtils.AssessmentViewCreation;

public class AssessmentExpandedActivity extends AppCompatActivity {

    static Assessment showAssessment;
    static Course parentCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_expanded);

        final TextView assessmentName = findViewById(R.id.assessmentNameShow);
        final TextView assessmentType = findViewById(R.id.assessmentTypeShow);
        final TextView assessmentEndDate = findViewById(R.id.assessmentEndDateShow);

        assessmentName.setText(showAssessment.getTitle());
        assessmentType.setText(showAssessment.getType());
        assessmentEndDate.setText(showAssessment.getEndDate().toString());



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_course_expanded, menu);
        return true;
    }

    public static void getSelectedAssessment(Assessment assessment, Course course){
        showAssessment = assessment;
        parentCourse = course;
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
        selectedObject.setText(showAssessment.getTitle());

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
                LocalDate chosenStartDate = parentCourse.getStartDate();

                Date chosenDate = java.sql.Date.valueOf(String.valueOf(chosenStartDate));
                long trigger = chosenDate.getTime();



                Intent intent = new Intent(AssessmentExpandedActivity.this, AssessmentAlert.class);
                intent.setAction("ExtraOptions.AssessmentAlert.ACTION_ALERT");
                intent.putExtra("key", showAssessment.getTitle() + " starts today!");

                String contentText = " starts today!";
                String contentTitle = "An Assessment has started!";


                PendingIntent sender = PendingIntent.getBroadcast(AssessmentExpandedActivity.this, numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);

                Toast.makeText(getApplicationContext(), "Alert set for " + showAssessment.getTitle() + " at " + parentCourse.getStartDate(), Toast.LENGTH_LONG).show();
                //alertDialog.dismiss();
            }
        });

        endDateAlertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int numAlert = random.nextInt(100000000);
                LocalDate chosenEndDate = showAssessment.getEndDate();
                Date chosenDate = java.sql.Date.valueOf(String.valueOf(chosenEndDate));
                long trigger = chosenDate.getTime();
                System.out.println(trigger);


                Intent intent = new Intent(AssessmentExpandedActivity.this, AssessmentAlert.class);
                intent.putExtra("key", showAssessment.getTitle() + " ends today!");
                intent.putExtra("title", "An Assessment has Ended!");
                intent.putExtra("text", showAssessment.getTitle() + " ends today!");
                intent.setAction("ExtraOptions.AssessmentAlert.ACTION_ALERT");





                PendingIntent sender = PendingIntent.getBroadcast(AssessmentExpandedActivity.this, numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);

                Toast.makeText(getApplicationContext(), "Alert set for " + showAssessment.getTitle() + " at " + showAssessment.getEndDate(), Toast.LENGTH_LONG).show();


                //alertDialog.dismiss();
            }
        });



        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();  // Go back when the back button is pressed
            return true;
        } else if (item.getItemId() == R.id.setAssessmentAlert) {
            System.out.println("Set Assessment Alert");
            showAlertOptions();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
