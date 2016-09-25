package in.ciautomotivetechnologies.ciautomotive.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.ciautomotivetechnologies.ciautomotive.R;
import in.ciautomotivetechnologies.ciautomotive.adapters.LocationArrayAdapter;
import in.ciautomotivetechnologies.ciautomotive.network.CallExecutor;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.CarLocationInterface;
import in.ciautomotivetechnologies.ciautomotive.network.models.CarLocations;


public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback, CarLocationInterface {

    @BindView(R.id.locations)
    ListView locations;
    SharedPreferences prefs;
    @BindView(R.id.snackbar)
    CoordinatorLayout coordinatorLayoutView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        List<String> locations_array=new ArrayList<>();
        for(int i=0;i<10;i++){
            locations_array.add("test");
        }
        prefs=getSharedPreferences(getResources().getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        ButterKnife.bind(this);
        final CallExecutor executor=new CallExecutor();
        Bundle bundle=getIntent().getExtras();
        final String device_id=bundle.getString(getResources().getString(R.string.device_id));
        final String token=prefs.getString(getResources().getString(R.string.token),null);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                executor.getCarLocation(device_id,token,LocationActivity.this);
            }
        },0, 60000);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void response(List<CarLocations> carLocation) {
        LocationArrayAdapter adapter=new LocationArrayAdapter(LocationActivity.this,carLocation,coordinatorLayoutView);
        locations.setAdapter(adapter);
    }

    @Override
    public void error(int error) {
        if(error==CallExecutor.UNAUTHORIZED){
            Snackbar.make(coordinatorLayoutView, getResources().getString(R.string.not_allowed), Snackbar.LENGTH_SHORT).show();
        }else{
            Snackbar.make(coordinatorLayoutView, getResources().getString(R.string.call_issue), Snackbar.LENGTH_SHORT).show();
        }
    }
}
