package in.ciautomotivetechnologies.ciautomotive.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.ciautomotivetechnologies.ciautomotive.R;
import in.ciautomotivetechnologies.ciautomotive.network.CallExecutor;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.CallUpdate;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.TripStartedInterface;


public class TripActivity extends AppCompatActivity implements TripStartedInterface, CallUpdate {
    SharedPreferences prefs;
    @BindView(R.id.trip_start_stop)
    Button tripStartStop;
    CallExecutor executor;
    String token;
    String device_id;

    @OnClick(R.id.trip_start_stop)
    void setTripStartStop(){
        if(tripStartStop.getText().equals(getResources().getString(R.string.begin_trip))){
            executor.postTripStart(device_id,token, TripActivity.this);
        }else{

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        ButterKnife.bind(this);
        Bundle bundle=getIntent().getExtras();
        device_id=bundle.getString(getResources().getString(R.string.device_id));
        prefs=getSharedPreferences(getResources().getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        token=prefs.getString(getResources().getString(R.string.token),null);
        executor=new CallExecutor();
        executor.getTripStarted(token,device_id,TripActivity.this);
    }

    @Override
    public void running() {
        tripStartStop.setText(getResources().getString(R.string.stop_trip));
    }

    @Override
    public void notRunning() {
        tripStartStop.setText(getResources().getString(R.string.begin_trip));
    }

    @Override
    public void success() {

    }

    @Override
    public void error(int code) {

    }
}
