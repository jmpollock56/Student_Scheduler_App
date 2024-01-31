package ExtraOptions;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import Model.Assessment;

import pollock.student_scheduler_app.R;

public class AssessmentAlert extends BroadcastReceiver {
    private String channelId = "CHANNEL_ID2";
    private static int notificationnId;


    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {

        String details = intent.getStringExtra("key");
        Toast.makeText(context, details, Toast.LENGTH_LONG).show();
        createNotificationChannel(context, channelId);

        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");



        NotificationCompat.Builder n = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.baseline_school_24)
                .setContentText(text)
                .setContentTitle(title);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


        notificationManager.notify(notificationnId++, n.build());


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
}
