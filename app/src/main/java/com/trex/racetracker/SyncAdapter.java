package com.trex.racetracker;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * Created by Igor on 18.4.2017.
 */

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /*
     * Specify the code you want to run in the sync adapter. The entire
     * sync adapter runs in a background thread, so you don't have to set
     * up your own background processing.
     */
    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
    /*
     * Put the data transfer code here.
     */
    /*

            Connecting to a server
                Although you can assume that the network is available when your data transfer starts,
                the sync adapter framework doesn't automatically connect to a server.
            Downloading and uploading data
                A sync adapter doesn't automate any data transfer tasks.
                If you want to download data from a server and store it in a content provider,
                you have to provide the code that requests the data, downloads it, and inserts it in the provider.
                Similarly, if you want to send data to a server, you have to read it from a file, database, or provider, and send the necessary upload request.
                You also have to handle network errors that occur while your data transfer is running.
            Handling data conflicts or determining how current the data is
                A sync adapter doesn't automatically handle conflicts between data on the server and data on the device.
                Also, it doesn't automatically detect if the data on the server is newer than the data on the device, or vice versa.
                Instead, you have to provide your own algorithms for handling this situation.
            Clean up.
                Always close connections to a server and clean up temp files and caches at the end of your data transfer.
     */



    }
}
