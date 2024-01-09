package pollock.student_scheduler_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import Model.Term;

public class TermExpandedActivity extends AppCompatActivity {

    private static Term selectedTerm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_expanded);

        RelativeLayout relativeLayout = findViewById(R.id.rL);

        final TextView termNameText = relativeLayout.findViewById(R.id.termTitle);
        final TextView termStartText = relativeLayout.findViewById(R.id.startDate);
        final TextView termEndText = relativeLayout.findViewById(R.id.endDate);
        termNameText.setText(String.valueOf(selectedTerm.getTitle()));
        termStartText.setText(String.valueOf(selectedTerm.getStartDate()));
        termEndText.setText(String.valueOf(selectedTerm.getEndDate()));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static void getTermInfo(Term term){
        selectedTerm = term;
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
