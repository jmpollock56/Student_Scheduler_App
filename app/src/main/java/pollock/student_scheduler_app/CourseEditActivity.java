package pollock.student_scheduler_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import java.util.Date;
import java.util.Objects;

import Database.CourseRepository;
import Database.InstructorRepository;
import Model.Course;
import Model.Instructor;


public class CourseEditActivity extends AppCompatActivity {

    static Course editCourse;

    private Instructor courseInstructor;

    CourseRepository courseRepository;

    InstructorRepository instructorRepository;

    String[] statusOptions = {"In Progress", "Completed", "Dropped", "Plan to Take"};

    EditText editCourseName;
    DatePicker editCourseStartDate;
    DatePicker editCourseEndDate;
    EditText editInstructorName;
    EditText editInstructorNumber;
    EditText editInstructorEmail;
    EditText editCourseNote;

    Spinner statusSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseRepository = new CourseRepository(getApplication());
        instructorRepository = new InstructorRepository(getApplication());

        loadSpinner();
        findCourseInstructor();
        loadOtherCourseInfo();

        Button saveCourseBtn = findViewById(R.id.editSaveBtn);

        saveCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCourseEdit();
            }
        });

    }

    private void saveCourseEdit(){
        String courseName = editCourseName.getText().toString();

        int startYear = editCourseStartDate.getYear();
        int startMonth = editCourseStartDate.getMonth() + 1;
        int startDay = editCourseStartDate.getDayOfMonth();

        LocalDate newStartDate = LocalDate.of(startYear, startMonth, startDay);

        int endYear = editCourseEndDate.getYear();
        int endMonth = editCourseEndDate.getMonth() + 1;
        int endDay = editCourseEndDate.getDayOfMonth();

        LocalDate newEndDate = LocalDate.of(endYear, endMonth, endDay);

        String status = statusSpinner.getSelectedItem().toString();

        String instructorName = editInstructorName.getText().toString();
        String instructorNumber = editInstructorNumber.getText().toString();
        String instructorEmail = editInstructorEmail.getText().toString();

        if (courseName.isEmpty() || instructorName.isEmpty() || instructorNumber.isEmpty() || instructorEmail.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please Fill out all mandatory fields.", Toast.LENGTH_LONG).show();
            return;
        }

        String courseNote = editCourseNote.getText().toString();

        if (newStartDate.isAfter(newEndDate)){
            Toast.makeText(getApplicationContext(), "The End Date should be after the Start Date", Toast.LENGTH_LONG).show();
            return;
        }

        Course editedCourse = new Course(editCourse.getId(), courseName, newStartDate, newEndDate, status, courseNote, courseInstructor.getId(), editCourse.getTermId());
        Instructor editedInstructor = new Instructor(courseInstructor.getId(), instructorName, instructorNumber, instructorEmail);

        courseRepository.updateCourse(editedCourse);
        instructorRepository.updateInstructor(editedInstructor);

        Intent intent = new Intent(CourseEditActivity.this, TermExpandedActivity.class);
        startActivity(intent);
    }
    private void findCourseInstructor(){
        for (Instructor instructor: instructorRepository.getmAllInstructors()){
            if (instructor.getId() == editCourse.getInstructorId()){
                courseInstructor = instructor;
            }
        }
    }

    private void loadOtherCourseInfo(){

        editCourseName = findViewById(R.id.editCourseName);
        editCourseStartDate = findViewById(R.id.editCourseStartDate);
        editCourseEndDate = findViewById(R.id.editCourseEndDate);
        editInstructorName = findViewById(R.id.instructorName);
        editInstructorNumber = findViewById(R.id.instructorNumber);
        editInstructorEmail = findViewById(R.id.instructorEmail);
        editCourseNote = findViewById(R.id.optionalNote);



        editCourseName.setText(editCourse.getTitle());

        int startYear = editCourse.getStartDate().getYear();
        int startMonth = editCourse.getStartDate().getMonthValue() - 1;
        int startDay = editCourse.getStartDate().getDayOfMonth();

        editCourseStartDate.init(startYear, startMonth, startDay, ((view, year, monthOfYear, dayOfMonth) -> {

        }));

        int endYear = editCourse.getEndDate().getYear();
        int endMonth = editCourse.getEndDate().getMonthValue() - 1;
        int endDay = editCourse.getEndDate().getDayOfMonth();

        editCourseEndDate.init(endYear, endMonth, endDay, ((view, year, monthOfYear, dayOfMonth) -> {

        }));

        editInstructorName.setText(courseInstructor.getName());
        editInstructorNumber.setText(courseInstructor.getPhoneNumber());
        editInstructorEmail.setText(courseInstructor.getEmailAddress());
        editCourseNote.setText(editCourse.getNote());
    }

    private void loadSpinner(){

        statusSpinner = findViewById(R.id.editCourseStatus);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);


        for (int i = 0; i < statusOptions.length; i++){
            if (statusOptions[i].equals(editCourse.getStatus())){
                statusSpinner.setSelection(i);
            }
        }
    }

    public static void getSelectedCourse(Course course){
        editCourse = course;
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
