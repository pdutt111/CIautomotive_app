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
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.TokenResultInterface;

public class CreateUserActivity extends AppCompatActivity implements TokenResultInterface {

    @BindView(R.id.name)
    EditText nameEdittext;
    @BindView(R.id.email)
    EditText emailEdittext;
    @BindView(R.id.snackbar)
    View coordinatorLayoutView;

    SharedPreferences prefs;
    final String BLANK="";
    @OnClick(R.id.add_car)
    public void next(View view) {
        // TODO submit data to server...
        if(!nameEdittext.getText().toString().equals(BLANK)&&!emailEdittext.getText().toString().equals(BLANK)) {
            SharedPreferences prefs = getSharedPreferences(getResources().getString(R.string.shared_prefs), Context.MODE_PRIVATE);
            String token = prefs.getString(getResources().getString(R.string.token), null);
            Log.i("token", token);
            if (token != null) {
                CallExecutor executor = new CallExecutor();
                executor.updateUser(token, nameEdittext.getText().toString(), emailEdittext.getText().toString(), CreateUserActivity.this);
            }
        }else{
            if(nameEdittext.getText().toString().equals(BLANK)&&emailEdittext.getText().toString().equals(BLANK)){
                Snackbar.make(coordinatorLayoutView, getResources().getString(R.string.no_name_email), Snackbar.LENGTH_SHORT).show();
            }else if(nameEdittext.getText().toString().equals(BLANK)){
                Snackbar.make(coordinatorLayoutView, getResources().getString(R.string.no_name), Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(coordinatorLayoutView, getResources().getString(R.string.no_email), Snackbar.LENGTH_SHORT).show();
            }
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        ButterKnife.bind(this);
        prefs = getSharedPreferences(getResources().getString(R.string.shared_prefs), Context.MODE_PRIVATE);
    }

    @Override
    public void registered(String token, String secret, String expires) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getResources().getString(R.string.token),token);
        editor.putString(getResources().getString(R.string.secret),secret);
        editor.putString(getResources().getString(R.string.expires),expires);
        editor.commit();
        Intent intent = new Intent(CreateUserActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void error(int code) {
        if(code==CallExecutor.UNAUTHORIZED){
            Snackbar.make(coordinatorLayoutView, getResources().getString(R.string.wrong_pin), Snackbar.LENGTH_SHORT).show();
        }else{

            Snackbar.make(coordinatorLayoutView, getResources().getString(R.string.call_issue), Snackbar.LENGTH_SHORT).show();
        }
    }
}
