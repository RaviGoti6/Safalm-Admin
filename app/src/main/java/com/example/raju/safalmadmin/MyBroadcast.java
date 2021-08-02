package com.example.raju.safalmadmin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import android.telephony.PhoneNumberUtils;
import android.widget.Toast;

import java.util.Random;

public class MyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Random r = new Random();
        int m = r.nextInt(9999 - 1000) + 1000;

        Intent sendIntent = null;
//        Intent ii = getIntent();
//        Bundle b = ii.getExtras();
//        if (b != null) {
//            slid = (String) b.get("slid");
//            vlid = (String) b.get("vlid");
//            lname = (String) b.get("name");
//            laddress = (String) b.get("address");
//            lmobile = (String) b.get("mobile");
//            lemail = (String) b.get("email");
//            lcname = (String) b.get("cmpName");
//            cmpTask = (String) b.get("cmpTask");
//            clid = (String) b.get("clid");
//            cvlid = (String) b.get("cvlid");
//
//            //txtSLDname.setText(lead_id);
//        }

        Toast.makeText(context, "Alarm Received...", Toast.LENGTH_SHORT).show();
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Bundle b = intent.getExtras();
        String name = (String) b.get("Lead_name");
        String type = (String) b.get("task_type");
        String number = (String) b.get("number");
        String email = (String) b.get("email");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker("SAFALM");
        builder.setContentTitle(name);
        builder.setContentText(type);

        if (type.equals("Call")) {
            builder.setSmallIcon(R.drawable.ic_stat_call1);
            sendIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));

        }
        if (type.equals("SMS")) {
            builder.setSmallIcon(R.drawable.ic_stat_sms1);
            Uri sms_uri = Uri.parse("smsto:" + number);
            sendIntent = new Intent(Intent.ACTION_SENDTO, sms_uri);
        }

        if (type.equals("Mail")) {
            builder.setSmallIcon(R.drawable.ic_stat_mail1);
            String[] TO = {email};
//                    String[] CC = {
//                            "ramdurai25@gmail.com"
//                    };
            sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setData(Uri.parse("mailto:"));
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        }

        if (type.equals("WhatsApp")) {
            builder.setSmallIcon(R.drawable.ic_stat_whatsapp1);
            String smsNumber = "91:" + number;
            sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");
        }
        PendingIntent p = PendingIntent.getActivity(context, 0, sendIntent, 0);
        builder.setAutoCancel(true);
        builder.setContentIntent(p);
        Notification n = builder.build();
        //create the notification
        n.vibrate = new long[]{150, 300, 150, 400};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(m, builder.build());
        //create a vibration
        try {

            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, som);
            toque.play();
        } catch (Exception e) {
        }

    }
}
