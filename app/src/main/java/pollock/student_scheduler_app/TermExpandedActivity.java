package pollock.student_scheduler_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.Random;

import Database.CourseRepository;
import Database.InstructorRepository;
import Model.Course;
import Model.Instructor;
import Model.Term;
import ViewUtils.CourseViewCreation;


public class TermExpandedActivity extends AppCompatActivity {

    private static Term selectedTerm;
    private FloatingActionButton addCourseButton;

    private CourseRepository courseRepository;
    private InstructorRepository instructorRepository;
    GestureDetector gestureDetector;
    LinearLayout linearLayout;
    String[] statusOptions = {"In Progress", "Completed", "Dropped", "Plan to Take"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_expanded);


        courseRepository = new CourseRepository(getApplication());
        instructorRepository = new InstructorRepository(getApplication());

        RelativeLayout relativeLayout = findViewById(R.id.rL);
        linearLayout = findViewById(R.id.coursesContainer);


        final TextView termNameText = relativeLayout.findViewById(R.id.termTitle);
        final TextView termStartText = relativeLayout.findViewById(R.id.startDate);
        final TextView termEndText = relativeLayout.findViewById(R.id.endDate);
        termNameText.setText(String.valueOf(selectedTerm.getTitle()));
        termStartText.setText(String.valueOf(selectedTerm.getStartDate()));
        termEndText.setText(String.valueOf(selectedTerm.getEndDate()));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addClickListenerToFAB();
        loadLocalArrayLists();

        for(Course course: courseRepository.getmAllCourses()){
            if (course.getTermId() == selectedTerm.getId()){
                RelativeLayout rL = CourseViewCreation.createCourseRelativeLayout(this, course);

                setCourseClickListeners(rL, course);
                rL.setTag(course);
                linearLayout.addView(rL);
            }

        }

    }

    public static void getTermInfo(Term term){
        selectedTerm = term;
    }

    private void addClickListenerToFAB(){
        addCourseButton = findViewById(R.id.addCourseFAB);

        addCourseButton.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View v) { showAddCourseDialog(); }
        }));
    }

    private void showAddCourseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.course_input, null);

        Spinner statusSpinner = dialogView.findViewById(R.id.statusSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        final EditText courseNameInput = dialogView.findViewById(R.id.editTextCourse);
        final DatePicker startCoursePicker = dialogView.findViewById(R.id.startDateCoursePicker);
        final DatePicker endCoursePicker = dialogView.findViewById(R.id.endDateCoursePicker);
        final EditText instructorNameText = dialogView.findViewById(R.id.instructorName);
        final EditText instructorNumberText = dialogView.findViewById(R.id.instructorNumber);
        final EditText instructorEmailText = dialogView.findViewById(R.id.instructorEmail);
        final EditText optionalNote = dialogView.findViewById(R.id.optionalNote);

        builder.setView(dialogView);
        builder.setTitle("Add Course");

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                  String courseName = courseNameInput.getText().toString();

                  int startYear = startCoursePicker.getYear();
                  int startMonth = startCoursePicker.getMonth() + 1;
                  int startDayOfMonth = startCoursePicker.getDayOfMonth();

                  LocalDate courseStartDate = LocalDate.of(startYear, startMonth, startDayOfMonth);

                  int endYear = endCoursePicker.getYear();
                  int endMonth = endCoursePicker.getMonth() + 1;
                  int endDayOfMonth = endCoursePicker.getDayOfMonth();

                  LocalDate courseEndDate = LocalDate.of(endYear, endMonth, endDayOfMonth);

                  String status = statusSpinner.toString();

                  String inName = instructorNameText.toString();
                  String inNumber = instructorNumberText.toString();
                  String inEmail = instructorEmailText.toString();
                  String note = optionalNote.toString();

                  Random random = new Random();

                  int courseId = random.nextInt(10000);
                  int instructorId = random.nextInt(20000);

                  Course course = new Course(courseId, courseName, courseStartDate, courseEndDate, status, note, instructorId, selectedTerm.getId());
                  Instructor instructor = new Instructor(instructorId, inName, inNumber, inEmail);


                  courseRepository.insertCourse(course);
                  instructorRepository.insertInstructor(instructor);

                  loadLocalArrayLists();

                  RelativeLayout newCourseLayout = CourseViewCreation.createCourseRelativeLayout(TermExpandedActivity.this, course);

                  setCourseClickListeners(newCourseLayout, course);
                  linearLayout.addView(newCourseLayout);



            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void loadLocalArrayLists(){

        for(Course course: courseRepository.getmAllCourses()){
            Course.addCourse(course);
        }

        for (Instructor instructor: instructorRepository.getmAllInstructors()){
            Instructor.addInstructor(instructor);
        }


    }

    private void setCourseClickListeners(RelativeLayout rL, final Course course){
        rL.setTag(course);
        rL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Course selectedCourse = (Course) v.getTag();
                CourseExpandedActivity.getCourseInfo(selectedCourse);

                Intent intent = new Intent(TermExpandedActivity.this, CourseExpandedActivity.class);
                startActivity(intent);


            }
        });
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
