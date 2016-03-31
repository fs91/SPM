package edu.purdue.spm;

import android.content.Context;
import android.content.Intent;
 
// This class contains the GCM configuration and our server registration url.
public final class CommonUtilities {
 
    // give your server registration url here
    static final String SERVER_URL = "http://web.ics.purdue.edu/~lin185/gcm_server_php/register.php"; 
 
    // Google project id
    static final String SENDER_ID = "713365819764"; 
 
    /**
     * Tag used on log messages.
     */
    static final String TAG = "AndroidHive GCM";
 
    static final String DISPLAY_MESSAGE_ACTION =
            "com.androidhive.pushnotifications.DISPLAY_MESSAGE";
 
    static final String EXTRA_MESSAGE = "message";
    
 
    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}