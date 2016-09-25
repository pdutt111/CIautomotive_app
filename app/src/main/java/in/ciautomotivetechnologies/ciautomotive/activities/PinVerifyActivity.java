package in.ciautomotivetechnologies.ciautomotive.activities;

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
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.PhonenumberRegistered;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.TokenResultInterface;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PinVerifyActivity extends AppCompatActivity implements TokenResultInterface, PhonenumberRegistered {

    SharedPreferences prefs;

    @BindView(R.id.pin)
    EditText pinEdittext;

    @BindView(R.id.snackbar)
    View coordinatorLayoutView;

    @OnClick(R.id.add_car)
    public void next(View view) {
        // TODO submit data to server...
        String pin_val= pinEdittext.getText().toString();
        Log.i("pin_val",pin_val);
        if(pin_val.length()==5){
            CallExecutor executor=new CallExecutor();
            executor.verifyPin(prefs.getString(getResources().getString(R.string.pref_phonenumber),null),pin_val,PinVerifyActivity.this);
        }else{
            Snackbar.make(coordinatorLayoutView, getResources().getString(R.string.invalid_pin), Snackbar.LENGTH_SHORT).show();
        }

    }
    @OnClick(R.id.resend)
    public void resend(View view) {
        // TODO submit data to server...
            CallExecutor executor=new CallExecutor();
            executor.postPhonenumber(prefs.getString("phonenumber",null),PinVerifyActivity.this);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_verify);
        ButterKnife.bind(this);
        Bundle bundle=getIntent().getExtras();
        String pin_recv=bundle.getString("pin");
        pinEdittext.setText(pin_recv);
        prefs = getSharedPreferences(getResources().getString(R.string.shared_prefs), Context.MODE_PRIVATE);

    }

    @Override
    public void registered(String token, String secret, String expires) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getResources().getString(R.string.token),token);
        editor.putString(getResources().getString(R.string.secret),secret);
        editor.putString(getResources().getString(R.string.expires),expires);
        editor.commit();
        Intent createUser = new Intent(PinVerifyActivity.this, CreateUserActivity.class);
        startActivity(createUser);
        finish();
    }

    @Override
    public void getpin(String pin_recv) {
        pinEdittext.setText(pin_recv);

    }

    @Override
    public void error(int code) {
        Log.i("error",code+"");
        if(code==CallExecutor.UNAUTHORIZED){

            Snackbar.make(coordinatorLayoutView, getResources().getString(R.string.wrong_pin), Snackbar.LENGTH_SHORT).show();
        }else{

            Snackbar.make(coordinatorLayoutView, getResources().getString(R.string.call_issue), Snackbar.LENGTH_SHORT).show();
        }
    }
}
