package pollock.student_scheduler_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;

import Database.AssessmentRepository;
import Model.Assessment;


public class AssessmentEditActivity extends AppCompatActivity {

    static Assessment selectedAssessment;
    AssessmentRepository assessmentRepository;
    Spinner assessmentTypeSpinner;
    EditText assessmentName;
    DatePicker assessmentEndDate;
    String[] assessmentSpinnerOptions = {"Performance Assessment", "Objective Assessment"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_edit);

        assessmentRepository = new AssessmentRepository(getApplication());

        assessmentName = findViewById(R.id.assessmentTitleEdit);
        assessmentTypeSpinner = findViewById(R.id.assessmentTypeEdit);
        assessmentEndDate = findViewById(R.id.assessmentEndDateEdit);

        loadSpinner();
        loadOtherAssessmentInfo();

        Button saveBtn = findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAssessmentEdit();
            }
        });



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void saveAssessmentEdit(){
        String type = assessmentTypeSpinner.getSelectedItem().toString();
        String name = assessmentName.getText().toString();

        if (name.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please give the assessment a name.", Toast.LENGTH_LONG).show();
            return;
        }

        int endYear = assessmentEndDate.getYear();
        int endMonth = assessmentEndDate.getMonth() + 1;
        int endDay = assessmentEndDate.getDayOfMonth();

        LocalDate endDate = LocalDate.of(endYear, endMonth, endDay);

        Assessment editedAssessment = new Assessment(selectedAssessment.getId(), type, name, endDate, selectedAssessment.getCourseId());

        assessmentRepository.updateAssessment(editedAssessment);

        Intent intent = new Intent(AssessmentEditActivity.this, CourseExpandedActivity.class);
        startActivity(intent);
    }

    private void loadSpinner(){
        Log.d("SelectedAss in loadSpinner", String.valueOf(selectedAssessment));
        assessmentTypeSpinner = findViewById(R.id.assessmentTypeEdit);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, assessmentSpinnerOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assessmentTypeSpinner.setAdapter(adapter);


        for (int i = 0; i < assessmentSpinnerOptions.length; i++){
            if (assessmentSpinnerOptions[i].equals(selectedAssessment.getType())){
                assessmentTypeSpinner.setSelection(i);
            }
        }
    }


    private void loadOtherAssessmentInfo(){
        Log.d("SelectedAss in loadOtherAssessmentInfo()", String.valueOf(selectedAssessment));
       assessmentName.setText(selectedAssessment.getTitle());

        int endYear = selectedAssessment.getEndDate().getYear();
        int endMonth = selectedAssessment.getEndDate().getMonthValue() - 1;
        int endDay = selectedAssessment.getEndDate().getDayOfMonth();

        assessmentEndDate.init(endYear, endMonth, endDay, ((view, year, monthOfYear, dayOfMonth) -> {

        }));
    }

    public static void getSelectedAssessment(Assessment assessment){
        selectedAssessment = assessment;
        Log.d("selected ass", String.valueOf(selectedAssessment));
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
