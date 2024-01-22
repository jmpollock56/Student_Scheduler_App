package pollock.student_scheduler_app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import Database.AssessmentRepository;
import Model.Assessment;
import ViewUtils.AssessmentViewCreation;

public class AssessmentExpandedActivity extends AppCompatActivity {

    static Assessment showAssessment;

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

    public static void getSelectedAssessment(Assessment assessment){
        showAssessment = assessment;
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
