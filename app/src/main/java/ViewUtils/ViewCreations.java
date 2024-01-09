package ViewUtils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import Model.Term;
import pollock.student_scheduler_app.MainActivity;

public class ViewCreations {

    public static RelativeLayout createTermRelativeLayout(Context context, Term term) {
        RelativeLayout relativeLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                80 * (int) context.getResources().getDisplayMetrics().density
        );

        // Set the layout parameters for the RelativeLayout
        relativeLayout.setLayoutParams(layoutParams);
        relativeLayout.setBackgroundColor(Color.parseColor("#00BCD4"));



        // Create and add TextViews to the RelativeLayout
        TextView startDateTextView = createTextView(context, term.getStartDate().toString());
        TextView endDateTextView = createTextView(context, term.getEndDate().toString());
        TextView termNameTextView = createTextView(context, term.getTitle());

        // Set layout parameters for each TextView within the RelativeLayout
        setTextViewLayoutParams(startDateTextView, RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        setTextViewLayoutParams(endDateTextView, RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        setTextViewLayoutParams(termNameTextView, RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);

        // Add TextViews to the RelativeLayout
        relativeLayout.addView(startDateTextView);
        relativeLayout.addView(endDateTextView);
        relativeLayout.addView(termNameTextView);


        return relativeLayout;
    }

    public static TextView createTextView(Context context, String text) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        textView.setText(text);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(14);
        return textView;
    }

    public static void setTextViewLayoutParams(TextView textView, int rules1, int rules2) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        params.addRule(rules1);

        // Check if rules2 is valid (not -1) before adding the rule
        if (rules2 != -1) {
            params.addRule(rules2);
        }

        textView.setLayoutParams(params);
    }
}
