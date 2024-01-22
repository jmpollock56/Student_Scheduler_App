package pollock.student_scheduler_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;

import Database.TermRepository;
import Model.Term;


public class TermEditActivity extends AppCompatActivity {

   static Term editTerm;
   LinearLayout termEditLayout;
   Button editSaveBtn;
   private TermRepository termRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        termRepository = new TermRepository(getApplication());

        termEditLayout = findViewById(R.id.container);
        editSaveBtn = findViewById(R.id.editSaveBtn);

        final EditText termNameEditText = findViewById(R.id.termNameEdit);
        final DatePicker termStartEdit = findViewById(R.id.startDateTermEdit);
        final DatePicker termEndEdit = findViewById(R.id.endDateTermEdit);

        termNameEditText.setText(editTerm.getTitle());

        int startYear = editTerm.getStartDate().getYear();
        int startMonth = editTerm.getStartDate().getMonthValue() - 1;
        int startDay = editTerm.getStartDate().getDayOfMonth();

        termStartEdit.init(startYear, startMonth, startDay, ((view, year, monthOfYear, dayOfMonth) -> {

        }));

        int endYear = editTerm.getEndDate().getYear();
        int endMonth = editTerm.getEndDate().getMonthValue() - 1;
        int endDay = editTerm.getEndDate().getDayOfMonth();

        termEndEdit.init(endYear, endMonth, endDay, ((view, year, monthOfYear, dayOfMonth) -> {

        }));

        editSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String termName = termNameEditText.getText().toString();

                int newStartYear = termStartEdit.getYear();
                int newStartMonth = termStartEdit.getMonth() + 1;
                int newStartDay = termStartEdit.getDayOfMonth();

                LocalDate newStartDate = LocalDate.of(newStartYear, newStartMonth, newStartDay);

                int newEndYear = termEndEdit.getYear();
                int newEndMonth = termEndEdit.getMonth() + 1;
                int newEndDay = termEndEdit.getDayOfMonth();

                LocalDate newEndDate = LocalDate.of(newEndYear, newEndMonth, newEndDay);

                Term editedTerm = new Term(editTerm.getId(), termName, newStartDate, newEndDate);

                termRepository.updateTerm(editedTerm);

                Intent intent = new Intent(TermEditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    public static void getSelectedTerm(Term term){
        editTerm = term;
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
