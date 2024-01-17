package pollock.student_scheduler_app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import Database.CourseRepository;
import Database.InstructorRepository;
import Model.Course;
import ViewUtils.CourseViewCreation;

public class CourseExpandedActivity extends AppCompatActivity {

    static Course selectedCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_expanded);
        RelativeLayout relativeLayout = findViewById(R.id.header);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();  // Go back when the back button is pressed
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
