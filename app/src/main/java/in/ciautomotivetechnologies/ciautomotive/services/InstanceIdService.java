package in.ciautomotivetechnologies.ciautomotive.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import in.ciautomotivetechnologies.ciautomotive.R;
import in.ciautomotivetechnologies.ciautomotive.network.CallExecutor;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.CallUpdate;

/**
 * Created by pariskshitdutt on 01/06/16.
 */
public class InstanceIdService extends FirebaseInstanceIdService implements CallUpdate {
    String TAG="token service";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        SharedPreferences prefs = getSharedPreferences(getResources().getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        String token = prefs.getString(getResources().getString(R.string.token), null);

        if(token==null){
            SharedPreferences.Editor editor=prefs.edit();
            editor.putString(getResources().getString(R.string.fcm_token),refreshedToken);
            editor.commit();
        }else{
            CallExecutor executor=new CallExecutor();
            executor.addDevice(refreshedToken,token,InstanceIdService.this);
        }
    }

    @Override
    public void success() {
        Log.i(TAG,"Successfully registered the device");
    }

    @Override
    public void error(int code) {

    }
}
