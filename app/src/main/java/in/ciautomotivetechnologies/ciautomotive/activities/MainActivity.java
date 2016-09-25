package in.ciautomotivetechnologies.ciautomotive.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.analytics.Tracker;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.ciautomotivetechnologies.ciautomotive.R;
import in.ciautomotivetechnologies.ciautomotive.adapters.CarArrayAdapter;
import in.ciautomotivetechnologies.ciautomotive.application.AnalyticsApplication;
import in.ciautomotivetechnologies.ciautomotive.network.CallExecutor;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.AddCarInterface;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.CallUpdate;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.GetCarsInterface;
import in.ciautomotivetechnologies.ciautomotive.network.models.AddCarModel;
import in.ciautomotivetechnologies.ciautomotive.services.InstanceIdService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GetCarsInterface,CallUpdate {
    private static final int ADD_CAR_ACTIVITY = 1;
    @BindView(R.id.snackbar)
    View coordinatorLayoutView;

    public MainActivity context;
    @OnClick(R.id.add_car)
    public void addCar(View view){
        Intent intent = new Intent(MainActivity.this, AddCarActivity.class);
        startActivityForResult(intent,ADD_CAR_ACTIVITY);
    }
    @BindView(R.id.car_list)
    ListView carList;

    CallExecutor executor;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        prefs=getSharedPreferences(getResources().getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        String token=prefs.getString(getResources().getString(R.string.token),null);
        executor=new CallExecutor();
        executor.getCars(token,MainActivity.this);
        String fcmToken=prefs.getString(getResources().getString(R.string.fcm_token),null);
        if(fcmToken!=null){
            Log.i("fcm token","attempting to save fcm token");
            executor.addDevice(fcmToken,token,MainActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
//                Bundle bundle=data.getExtras();
//                AddCarModel model=(AddCarModel)bundle.getParcelable("result");
                executor=new CallExecutor();
                String token=prefs.getString(getResources().getString(R.string.token),null);
                executor.getCars(token,MainActivity.this);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent bluetoothIntent=new Intent(MainActivity.this,BluetoothActivity.class);
            startActivity(bluetoothIntent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void refresh(){
        String token=prefs.getString(getResources().getString(R.string.token),null);
        executor=new CallExecutor();
        executor.getCars(token,MainActivity.this);
    }
    @Override
    public void gotCars(List<AddCarModel> cars) {
        Log.i("cars fetched",cars.size()+"");
        CarArrayAdapter carArrayAdapter=new CarArrayAdapter(MainActivity.this,cars,coordinatorLayoutView);
        carList.setAdapter(carArrayAdapter);
    }

    @Override
    public void success() {
        Log.i("fcm token","successfully saved fcm token");
    }

    @Override
    public void error(int code) {
        if(code==CallExecutor.UNAUTHORIZED){
            Snackbar.make(coordinatorLayoutView, getResources().getString(R.string.not_allowed), Snackbar.LENGTH_SHORT).show();
        }else{
            Snackbar.make(coordinatorLayoutView, getResources().getString(R.string.call_issue), Snackbar.LENGTH_SHORT).show();
        }
    }
}
