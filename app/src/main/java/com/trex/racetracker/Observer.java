package com.trex.racetracker;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import static com.trex.racetracker.MainActivity.AUTHORITY;

/**
 * Created by Igor on 26.4.2017.
 */

public class Observer extends ContentObserver {

    private Account mAccount;
    private Context context;

    long lastTimeofCall = 0L;
    long lastTimeofUpdate = 0L;
    long threshold_time = 10000;

    public Observer(Handler handler, Context context, Account account) {
        super(handler);
        this.mAccount = account;
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {

        onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri changeUri) {

        lastTimeofCall = System.currentTimeMillis();
        if(lastTimeofCall - lastTimeofUpdate > threshold_time){
            Bundle params = new Bundle();
            params.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            params.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

            if (ContentResolver.isSyncPending(mAccount, AUTHORITY)  ||
                    ContentResolver.isSyncActive(mAccount, AUTHORITY)) {
                Log.i("ContentResolver", "SyncPending, canceling");
                ContentResolver.cancelSync(mAccount, AUTHORITY);
            }

            ContentResolver.requestSync(mAccount, AUTHORITY, params);
            Log.d("info","request sync");
            //Toast.makeText(context, "request sync", Toast.LENGTH_SHORT).show();

            lastTimeofUpdate = System.currentTimeMillis();
        }
    }

}
