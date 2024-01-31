package ExtraOptions;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import Model.Course;
import pollock.student_scheduler_app.R;

public class Alert extends BroadcastReceiver {
    String channelId = "CHANNEL_ID";
    static int notificationId;
    static Course alertedCourse;
    static String contentTitle;
    static String contentText;

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {

        String details = intent.getStringExtra("key");
        Toast.makeText(context, details, Toast.LENGTH_LONG).show();
        createNotificationChannel(context, channelId);

        String title = intent.getStringExtra(contentTitle);
        String text = intent.getStringExtra(contentText);

        NotificationCompat.Builder nCourse = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.baseline_school_24)
                .setContentText(text)
                .setContentTitle(title);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


        notificationManager.notify(notificationId++, nCourse.build());


    }

    private void createNotificationChannel(Context context, String CHANNEL_ID){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "mychannelname";
            String description = "mychanneldescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);


            notificationManager.createNotificationChannel(channel);


        }
    }

    public static void getCourse(Course course, String newContentText, String newContentTitle){
        alertedCourse = course;
        contentText = newContentText;
        contentTitle = newContentTitle;


    }


}
