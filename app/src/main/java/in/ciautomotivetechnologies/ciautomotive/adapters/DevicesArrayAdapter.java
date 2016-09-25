package in.ciautomotivetechnologies.ciautomotive.adapters;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import in.ciautomotivetechnologies.ciautomotive.R;
import in.ciautomotivetechnologies.ciautomotive.activities.BluetoothActivity;
import in.ciautomotivetechnologies.ciautomotive.activities.DiagnosticsActivity;
import in.ciautomotivetechnologies.ciautomotive.activities.LocationActivity;
import in.ciautomotivetechnologies.ciautomotive.activities.MainActivity;
import in.ciautomotivetechnologies.ciautomotive.activities.TripActivity;
import in.ciautomotivetechnologies.ciautomotive.network.CallExecutor;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.CallUpdate;
import in.ciautomotivetechnologies.ciautomotive.network.models.AddCarModel;
import in.ciautomotivetechnologies.ciautomotive.threads.BluetoothThread;

/**
 * Created by pariskshitdutt on 19/05/16.
 */
public class DevicesArrayAdapter extends ArrayAdapter<BluetoothDevice> implements CallUpdate {
    Context context;
    List<BluetoothDevice> models;
    SharedPreferences prefs;
    View snackbar;
    BluetoothActivity activity;
    public DevicesArrayAdapter(Context context, List<BluetoothDevice> models, View snackbar) {
        super(context,models.size(),models);
        this.context = context;
        this.models=models;
        this.activity=(BluetoothActivity)context;
        this.snackbar=snackbar;
    }
    @Override
    public int getCount(){
        return models.size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.devices_cell_layout, parent, false);
            viewHolder.bluetoothDevice=models.get(position);
            viewHolder.device=(TextView)convertView.findViewById(R.id.device_name);
            viewHolder.position=position;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConnectThread connection=new ConnectThread(viewHolder.bluetoothDevice);
                    ExecutorService es = Executors.newCachedThreadPool();
                    es.execute(connection);
                }
            });
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) convertView.getTag();
        }

        viewHolder.device.setText(viewHolder.bluetoothDevice.getName() + "\n" + viewHolder.bluetoothDevice.getAddress());

        return convertView;
    }

    @Override
    public void success() {
        Snackbar.make(snackbar, context.getResources().getString(R.string.deleted_car), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void error(int code) {
        Snackbar.make(snackbar, context.getResources().getString(R.string.call_issue), Snackbar.LENGTH_SHORT).show();
    }
    static class ViewHolder {
        TextView device;
        int position;

        public BluetoothDevice bluetoothDevice;
    }

    private class ConnectThread implements Runnable{
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                UUID uuid = UUID.fromString("758f162b-ca81-45b1-bed8-a7811315a5e4");
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
//            mBluetoothAdapter.cancelDiscovery();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.devices.setVisibility(View.GONE);
                    activity.pBar.setVisibility(View.VISIBLE);
                }
            });
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                Log.i("bluetooth error","unable to connect");
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.pBar.setVisibility(View.GONE);
                        activity.devices.setVisibility(View.VISIBLE);
                        Snackbar.make(snackbar, context.getResources().getString(R.string.bluetooth_unable_connect), Snackbar.LENGTH_SHORT).show();
                    }
                });
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
//            manageConnectedSocket(mmSocket);
            BluetoothThread bluetoothThread=new BluetoothThread(mmSocket);
            bluetoothThread.run();
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}


