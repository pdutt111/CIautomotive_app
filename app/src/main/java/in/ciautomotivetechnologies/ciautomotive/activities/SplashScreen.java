package in.ciautomotivetechnologies.ciautomotive.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import in.ciautomotivetechnologies.ciautomotive.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreen extends AppCompatActivity {
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                prefs=getSharedPreferences(getResources().getString(R.string.shared_prefs), Context.MODE_PRIVATE);
                String token=prefs.getString(getResources().getString(R.string.token),null);
//                Log.i("splash screen token",token);
                if(token==null){
                    Intent phonenumberActivity = new Intent(SplashScreen.this, PhonenumberActivity.class);
                    startActivity(phonenumberActivity);
                }else{
                    Intent mainActivity = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(mainActivity);
                }
            }
        }, 2000);
        finish();

    }
}
