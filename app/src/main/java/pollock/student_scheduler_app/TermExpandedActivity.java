package pollock.student_scheduler_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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

import Database.CourseRepository;
import Database.InstructorRepository;
import Model.Course;
import Model.Instructor;
import Model.Term;
import ViewUtils.CourseViewCreation;
import ViewUtils.TermViewCreation;


public class TermExpandedActivity extends AppCompatActivity {

    static Term selectedTerm;
    Course selectedCourse;
    private FloatingActionButton addCourseButton;
    private CourseRepository courseRepository;
    private InstructorRepository instructorRepository;
    LinearLayout linearLayout;
    String[] statusOptions = {"In Progress", "Completed", "Dropped", "Plan to Take"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_expanded);


        courseRepository = new CourseRepository(getApplication());
        instructorRepository = new InstructorRepository(getApplication());



        linearLayout = findViewById(R.id.coursesContainer);


        final TextView termNameText = findViewById(R.id.termTitle);
        final TextView termStartText = findViewById(R.id.startDate);
        final TextView termEndText = findViewById(R.id.endDate);
        termNameText.setText(String.valueOf(selectedTerm.getTitle()));
        termStartText.setText(String.valueOf(selectedTerm.getStartDate()));
        termEndText.setText(String.valueOf(selectedTerm.getEndDate()));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addClickListenerToFAB();
        loadLocalArrayLists();

        ArrayList<RelativeLayout> courseLayouts = loadLocalArrayLists();
        refreshLinearLayout(courseLayouts);


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

                  String status = statusSpinner.getSelectedItem().toString();

                  String inName = instructorNameText.getText().toString();
                  String inNumber = instructorNumberText.getText().toString();
                  String inEmail = instructorEmailText.getText().toString();

                if (courseName.isEmpty() || inName.isEmpty() || inNumber.isEmpty() || inEmail.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Fill out all mandatory fields.", Toast.LENGTH_LONG).show();
                    return;
                }
                  String note = optionalNote.getText().toString();

                if (courseStartDate.isAfter(courseEndDate)){
                    Toast.makeText(getApplicationContext(), "The End Date should be after the Start Date", Toast.LENGTH_LONG).show();
                    return;
                }

                  Random random = new Random();

                  int courseId = random.nextInt(10000);
                  int instructorId = random.nextInt(20000);

                  Course course = new Course(courseId, courseName, courseStartDate, courseEndDate, status, note, instructorId, selectedTerm.getId());
                  Instructor instructor = new Instructor(instructorId, inName, inNumber, inEmail);

                  instructorRepository.insertInstructor(instructor);
                  courseRepository.insertCourse(course);


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

    private ArrayList<RelativeLayout> loadLocalArrayLists(){
        ArrayList<RelativeLayout> courseLayouts = new ArrayList<>();

        for (Course course: courseRepository.getmAllCourses()){
            if (course.getTermId() == selectedTerm.getId()){
                RelativeLayout relativeLayout = createCourseLayout(course);
                courseLayouts.add(relativeLayout);
            }

        }

        return courseLayouts;
    }

    private void setCourseClickListeners(RelativeLayout rL, final Course course){
        rL.setTag(course);
        rL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Course course = (Course) v.getTag();

                showCourseOptions(course);
            }
        });
    }

    private void showCourseOptions(Course course){
        selectedCourse = course;

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.course_options, null);



        builder2.setView(dialogView);
        builder2.setTitle("Add Course");

        Button viewOption = dialogView.findViewById(R.id.viewCourseBtn);
        Button editOption = dialogView.findViewById(R.id.editCourseBtn);
        Button deleteOption = dialogView.findViewById(R.id.deleteCourseBtn);

        builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog2 = builder2.create();

        viewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseExpandedActivity.getCourseInfo(selectedCourse);
                alertDialog2.dismiss();

                Intent intent = new Intent(TermExpandedActivity.this, CourseExpandedActivity.class);
                startActivity(intent);
            }
        });

        deleteOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseRepository.deleteCourse(selectedCourse);
                ArrayList<RelativeLayout> updatedCourseLayouts = loadLocalArrayLists();
                refreshLinearLayout(updatedCourseLayouts);
                alertDialog2.dismiss();

            }
        });

        editOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseEditActivity.getSelectedCourse(selectedCourse);
                alertDialog2.dismiss();

                Intent intent = new Intent(TermExpandedActivity.this, CourseEditActivity.class);
                startActivity(intent);
            }
        });


        alertDialog2.show();
    }

    private RelativeLayout createCourseLayout(Course course) {
        RelativeLayout relativeLayout = CourseViewCreation.createCourseRelativeLayout(this, course);
        setCourseClickListeners(relativeLayout, course);
        relativeLayout.setTag(course);
        return relativeLayout;
    }

    private void refreshLinearLayout(ArrayList<RelativeLayout> termLayouts) {
        linearLayout.removeAllViews(); // Remove all views from the LinearLayout

        // Re-add the updated views
        for (RelativeLayout termLayout : termLayouts) {
            linearLayout.addView(termLayout);
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
