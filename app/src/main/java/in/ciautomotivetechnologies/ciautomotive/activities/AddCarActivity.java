package in.ciautomotivetechnologies.ciautomotive.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.ciautomotivetechnologies.ciautomotive.R;
import in.ciautomotivetechnologies.ciautomotive.network.CallExecutor;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.AddCarInterface;
import in.ciautomotivetechnologies.ciautomotive.network.models.AddCarModel;

public class AddCarActivity extends AppCompatActivity implements AddCarInterface {
    @BindView(R.id.car_name_edittext)
    EditText carName;
    @BindView(R.id.device_id_edittext)
    EditText deviceId;
    @BindView(R.id.mileage_edittext)
    EditText mileage;
    @BindView(R.id.model_edittext)
    EditText modelName;
    @BindView(R.id.snackbar)
    View coordinatorLayoutView;

    SharedPreferences prefs;
    @OnClick(R.id.add_car)
    public void addCar(View view){
        Log.i("adding car","adding car");
        CallExecutor executor=new CallExecutor();
        executor.addCar(carName.getText().toString(),
                modelName.getText().toString(),
                mileage.getText().toString(),
                deviceId.getText().toString(),
                prefs.getString(getResources().getString(R.string.token),null),
                AddCarActivity.this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        ButterKnife.bind(this);
        prefs = getSharedPreferences(getResources().getString(R.string.shared_prefs), Context.MODE_PRIVATE);
    }

    @Override
    public void carAdded(AddCarModel model) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",model);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void error(int code) {
        if(code==CallExecutor.BAD_REQUEST){
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayoutView, getResources().getString(R.string.fill_all), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }else{
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayoutView, getResources().getString(R.string.call_issue), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }
}
