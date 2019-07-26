package com.example.ioanoanea15.creative.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.ioanoanea15.creative.R;
import com.example.ioanoanea15.creative.home.HomeActivity;
import com.example.ioanoanea15.creative.pakages.TokenManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token){
        super.onNewToken(token);

        TokenManager tokenManager = new TokenManager(getApplication());

        Log.d("MyFirebaseToken", "Token= " + token);
        tokenManager.addToken(token);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
         super.onMessageReceived(remoteMessage);
         //Toast.makeText(getBaseContext(),remoteMessage.getNotification().getBody(),Toast.LENGTH_SHORT).show();

        if(remoteMessage.getData().size() > 0){
            Intent intent = new Intent("UpdateMessages");
            intent.putExtra("from",remoteMessage.getData().get("fromuser"));
            intent.putExtra("message",remoteMessage.getNotification().getBody());
            intent.putExtra("isphoto",remoteMessage.getData().get("isphoto"));

            sendBroadcast(intent);
           // messageManager.addMessage(remoteMessage.getData().get("fromuser"),remoteMessage.getNotification().getBody(),remoteMessage.getData().get("messageid"));
        }

    }


    public void showNotifications(RemoteMessage remoteMessage){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this,HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("title"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        notificationManager.notify(0, notificationBuilder.build());
    }

}
