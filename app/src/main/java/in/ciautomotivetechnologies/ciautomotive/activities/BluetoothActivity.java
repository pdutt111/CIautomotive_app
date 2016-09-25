package in.ciautomotivetechnologies.ciautomotive.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.ciautomotivetechnologies.ciautomotive.R;
import in.ciautomotivetechnologies.ciautomotive.adapters.DevicesArrayAdapter;

public class BluetoothActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    @BindView(R.id.devices_view)
    public ListView devices;
    @BindView(R.id.snackbar)
    View coordinatorLayoutView;
    @BindView(R.id.progressBar)
    public ProgressBar pBar;
    BluetoothAdapter mBluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        ButterKnife.bind(this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }else{
            getPairedDevice();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_ENABLE_BT){
            getPairedDevice();
        }

    }
    private void getPairedDevice(){
        List<BluetoothDevice> devicesArray=new ArrayList<>();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
// If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                devicesArray.add(device);
            }
        }
        final DevicesArrayAdapter devicesArrayAdapter=new DevicesArrayAdapter(BluetoothActivity.this,devicesArray,coordinatorLayoutView);
        devices.setAdapter(devicesArrayAdapter);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

    }
}
