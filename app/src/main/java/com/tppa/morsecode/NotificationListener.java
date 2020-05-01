package com.tppa.morsecode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationListener extends NotificationListenerService {

    private NLServiceReceiver nlServiceReceiver;
    @Override
    public void onCreate() {
        super.onCreate();
        nlServiceReceiver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.tppa.morsecode.GET_ACTIVE_NOTIFICATIONS_LIST_SERVICE");
        registerReceiver(nlServiceReceiver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlServiceReceiver);
    }


    class NLServiceReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
                for(StatusBarNotification sbn : NotificationListener.this.getActiveNotifications())
                {
                    Intent i2 = new  Intent("com.tppa.morsecode.GET_ACTIVE_NOTIFICATIONS_LIST");
                    i2.putExtra("id",sbn.getId());
                    sendBroadcast(i2);
                }
         }

    }



}