package in.ciautomotivetechnologies.ciautomotive.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import in.ciautomotivetechnologies.ciautomotive.R;
import in.ciautomotivetechnologies.ciautomotive.activities.DiagnosticsActivity;
import in.ciautomotivetechnologies.ciautomotive.activities.LocationActivity;
import in.ciautomotivetechnologies.ciautomotive.activities.MainActivity;
import in.ciautomotivetechnologies.ciautomotive.activities.NotificationActivity;
import in.ciautomotivetechnologies.ciautomotive.activities.TripActivity;
import in.ciautomotivetechnologies.ciautomotive.network.CallExecutor;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.CallUpdate;
import in.ciautomotivetechnologies.ciautomotive.network.models.AddCarModel;

/**
 * Created by pariskshitdutt on 19/05/16.
 */
public class CarArrayAdapter extends ArrayAdapter<AddCarModel> implements CallUpdate {
    Context context;
    List<AddCarModel> models;
    SharedPreferences prefs;
    View snackbar;
    MainActivity activity;
    public CarArrayAdapter(Context context,  List<AddCarModel> models,View snackbar) {
        super(context,models.size(),models);
        this.context = context;
        this.models=models;
        this.snackbar=snackbar;
        this.activity=(MainActivity) context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.car_cell_layout, parent, false);
        }
            prefs = context.getSharedPreferences(context.getResources().getString(R.string.shared_prefs), Context.MODE_PRIVATE);
            final AddCarModel carData = models.get(position);
            final CallExecutor executor = new CallExecutor();
            TextView carName = (TextView) convertView.findViewById(R.id.car_name);
            TextView model = (TextView) convertView.findViewById(R.id.model);
            TextView mileage = (TextView) convertView.findViewById(R.id.mileage);
            ImageButton diagnosticsButton = (ImageButton) convertView.findViewById(R.id.diagnostics_button);
            ImageButton locationButton = (ImageButton) convertView.findViewById(R.id.location_button);
            ImageButton tripButton = (ImageButton) convertView.findViewById(R.id.trip_button);
            ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.delete_button);
            ImageButton notificationButton= (ImageButton) convertView.findViewById(R.id.notification_button);

            carName.setText(carData.name);
            model.setText(carData.model);
            mileage.setText(carData.mileage);
            diagnosticsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent diagnosticsIntent = new Intent(context, DiagnosticsActivity.class);
                    diagnosticsIntent.putExtra(context.getResources().getString(R.string.device_id), carData.device_id);
                    context.startActivity(diagnosticsIntent);
                }
            });
            locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent locationIntent = new Intent(context, LocationActivity.class);
                    locationIntent.putExtra(context.getResources().getString(R.string.device_id), carData.device_id);
                    context.startActivity(locationIntent);
                }
            });
            tripButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent tripIntent = new Intent(context, TripActivity.class);
                    tripIntent.putExtra(context.getResources().getString(R.string.device_id), carData.device_id);
                    context.startActivity(tripIntent);
                }
            });
            notificationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent notificationIntent = new Intent(context, NotificationActivity.class);
                    notificationIntent.putExtra(context.getResources().getString(R.string.device_id), carData.device_id);
                    context.startActivity(notificationIntent);
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(R.string.delete_car_alert)
                            .setPositiveButton(R.string.positive_alert, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String token = prefs.getString(context.getResources().getString(R.string.token), null);
                                    executor.deleteCar(carData.device_id, token, CarArrayAdapter.this);
                                }
                            })
                            .setNegativeButton(R.string.cancel_alert, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    builder.create();
                    builder.show();

                }
            });
        return convertView;
    }

    @Override
    public void success() {
        Snackbar.make(snackbar, context.getResources().getString(R.string.deleted_car), Snackbar.LENGTH_SHORT).show();
        activity.refresh();
    }

    @Override
    public void error(int code) {
            Snackbar.make(snackbar, context.getResources().getString(R.string.call_issue), Snackbar.LENGTH_SHORT).show();
    }
}
