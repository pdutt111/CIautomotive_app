package in.ciautomotivetechnologies.ciautomotive.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.ciautomotivetechnologies.ciautomotive.R;
import in.ciautomotivetechnologies.ciautomotive.network.CallExecutor;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.CarHealthInterface;
import in.ciautomotivetechnologies.ciautomotive.network.models.CarHealth;

public class DiagnosticsActivity extends AppCompatActivity implements CarHealthInterface {

    @BindView(R.id.voltage)
    TextView voltageView;
    @BindView(R.id.coolant_temp)
    TextView coolantTempView;
    @BindView(R.id.speed)
    TextView speedView;
    @BindView(R.id.engine_rpm)
    TextView engineRpmView;
    @BindView(R.id.air_temp)
    TextView intakeAirTempView;
    @BindView(R.id.throttle)
    TextView throttleView;
    @BindView(R.id.run_time)
    TextView run_timeView;
    @BindView(R.id.fuel_level)
    TextView fuelLevelView;
    @BindView(R.id.ambient_air_temp)
    TextView ambientAirTempView;
    @BindView(R.id.engine_oil_temp)
    TextView engineOilTempView;
    @BindView(R.id.snackbar)
    CoordinatorLayout coordinatorLayoutView;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostics);
        ButterKnife.bind(this);
        prefs=getSharedPreferences(getResources().getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        Bundle bundle=getIntent().getExtras();
        final String device_id=bundle.getString(getResources().getString(R.string.device_id));
        final String token=prefs.getString(getResources().getString(R.string.token),null);
        final CallExecutor executor=new CallExecutor();
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                executor.getCarHealth(device_id,token,DiagnosticsActivity.this);
            }
        },0, 60000);

    }

    @Override
    public void response(CarHealth health) {

        voltageView.setText(health.voltage);
        coolantTempView.setText(Integer.toString(health.coolant_temp));
        speedView.setText(Integer.toString(health.vehicle_speed_sensor));
        engineRpmView.setText(Integer.toString(health.engine_rpm));
        intakeAirTempView.setText(Integer.toString(health.intake_air_temp));
        throttleView.setText(Integer.toString(health.throttle_position));
        run_timeView.setText(Integer.toString(health.run_time));
        fuelLevelView.setText(Integer.toString(health.fuel_level));
        ambientAirTempView.setText(Integer.toString(health.ambient_air_temp));
        engineOilTempView.setText(Integer.toString(health.engine_oil_temp));

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
