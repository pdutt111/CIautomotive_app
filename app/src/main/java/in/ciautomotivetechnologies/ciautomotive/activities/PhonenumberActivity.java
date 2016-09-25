package in.ciautomotivetechnologies.ciautomotive.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
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
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.PhonenumberRegistered;


public class PhonenumberActivity extends AppCompatActivity implements PhonenumberRegistered {
    @BindView(R.id.phonenumber)
    EditText phonenumber;
    @BindView(R.id.snackbar)
    View coordinatorLayoutView;

    @OnClick(R.id.submit)
    public void submit(View view) {
        // TODO submit data to server...
        Log.i("pressed submit","pressed");
        String ph=phonenumber.getText().toString();
        CallExecutor executor=new CallExecutor();
        executor.postPhonenumber(ph,PhonenumberActivity.this);
        SharedPreferences prefs = getSharedPreferences(getResources().getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getResources().getString(R.string.pref_phonenumber),ph);
        editor.commit();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonenumber);
        ButterKnife.bind(this);
        Log.i("created activity","created");
    }

    @Override
    public void getpin(String pin) {
        Intent pinVerify = new Intent(PhonenumberActivity.this, PinVerifyActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("pin",pin);
        pinVerify.putExtras(bundle);
        startActivity(pinVerify);
        finish();
    }

    @Override
    public void error(int code) {
        if(code==CallExecutor.BAD_REQUEST){
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayoutView, getResources().getString(R.string.wrong_phonenumber), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }else{
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayoutView, getResources().getString(R.string.call_issue), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }
}
