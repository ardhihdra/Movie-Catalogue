package com.example.myfilmandtvlist.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.example.myfilmandtvlist.MainActivity;
import com.example.myfilmandtvlist.R;
import com.example.myfilmandtvlist.todayFilm.TodayMovieActivity;

import java.util.Calendar;

public class ReminderReceiver extends BroadcastReceiver {
    public static final String TYPE_REPEATING = "RepeatingAlarm";
    public static final String TITLE = "My Film & TV List Reminder";
    public static final String KEY_MESSAGE = "message";

    public static final String TYPE_REMINDER = "daily";
    public static final String TYPE_RELEASE = "release";

    public static final int ID_DAILY_REMINDER = 101;
    public static final int ID_RELEASE_REMINDER = 102;

    public static final String DAILY_MESSAGE = "Have you check your App today?";

    public ReminderReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(TITLE);
        int notifId = intent.getIntExtra(TYPE_REMINDER,0);
        String message = intent.getStringExtra(KEY_MESSAGE);
        //Toast.makeText(context, title + " : " + "Have you check your App today?", Toast.LENGTH_LONG).show();
        showAlarmNotification(context, title, message, notifId);
    }

    private void showAlarmNotification(Context context, String title, String content, int notifId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";
        Intent intent;
        PendingIntent pendingIntent;
        if(notifId == ID_DAILY_REMINDER) {
            intent = new Intent(context, MainActivity.class);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        } else {
            intent = new Intent(context, TodayMovieActivity.class);
            pendingIntent = TaskStackBuilder.create(context)
                    .addParentStack(TodayMovieActivity.class)
                    .addNextIntent(intent)
                    .getPendingIntent(110, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        //beda ini

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.the_movie)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(content)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }
    }

    public void setDailyAlarm(Context context, int idReminder, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        Calendar calendar = Calendar.getInstance();
        int time;
        if(idReminder == ID_DAILY_REMINDER) {
            time = 7;
            intent.putExtra(TYPE_REMINDER,ID_DAILY_REMINDER);
            intent.putExtra(KEY_MESSAGE, message);
        } else {
            time = 8;
            intent.putExtra(TYPE_REMINDER,idReminder);
            intent.putExtra(KEY_MESSAGE, "New Movie : " + message + " is out!"); // diisi list film
        }
        calendar.set(Calendar.HOUR_OF_DAY, time);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idReminder, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        //Toast.makeText(context, "Daily alarm set up", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context, int idRemainder) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idRemainder, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        //Toast.makeText(context, "Daily alarm turned off", Toast.LENGTH_SHORT).show();
    }
}
