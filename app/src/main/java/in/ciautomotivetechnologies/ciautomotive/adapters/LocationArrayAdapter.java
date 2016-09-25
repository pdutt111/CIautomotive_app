package in.ciautomotivetechnologies.ciautomotive.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.List;

import in.ciautomotivetechnologies.ciautomotive.R;
import in.ciautomotivetechnologies.ciautomotive.activities.DiagnosticsActivity;
import in.ciautomotivetechnologies.ciautomotive.activities.LocationActivity;
import in.ciautomotivetechnologies.ciautomotive.activities.MainActivity;
import in.ciautomotivetechnologies.ciautomotive.activities.TripActivity;
import in.ciautomotivetechnologies.ciautomotive.network.CallExecutor;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.CallUpdate;
import in.ciautomotivetechnologies.ciautomotive.network.models.AddCarModel;
import in.ciautomotivetechnologies.ciautomotive.network.models.CarLocations;

/**
 * Created by pariskshitdutt on 19/05/16.
 */
public class LocationArrayAdapter extends ArrayAdapter<CarLocations> implements CallUpdate {
    Context context;
    List<CarLocations> models;
    SharedPreferences prefs;
    View snackbar;
    LocationActivity activity;
    public LocationArrayAdapter(Context context, List<CarLocations> models, View snackbar) {
        super(context,models.size(),models);
        this.context = context;
        this.models=models;
        this.snackbar=snackbar;
        this.activity=(LocationActivity)context;
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
                convertView = inflater.inflate(R.layout.cell_location_rest, parent, false);
            viewHolder.textView=(TextView)convertView.findViewById(R.id.location);
            viewHolder.rectTop=(ImageView)convertView.findViewById(R.id.rect_top);
            viewHolder.rectBottom=(ImageView)convertView.findViewById(R.id.rect_bottom);
            viewHolder.position=position;
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        final int listPosition=position;
//        Log.i(listPosition+"",viewHolder.rectTop.getVisibility()+" , "+viewHolder.rectBottom.getVisibility()+" , "+getCount());
        viewHolder.rectTop.setVisibility(View.VISIBLE);
        viewHolder.rectBottom.setVisibility(View.VISIBLE);
        if(listPosition==0){
                viewHolder.rectTop.setVisibility(View.INVISIBLE);
                Log.i("hiding top",position+"");
            }else if(listPosition==(getCount()-1)){
                viewHolder.rectBottom.setVisibility(View.INVISIBLE);
                Log.i("hiding bottom",position+"");
            }
        CarLocations location=models.get(listPosition);
        viewHolder.textView.setText(location.location[0]+" "+location.location[1]);

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
        TextView textView;
        ImageView rectTop;
        ImageView rectBottom;
        int position;

    }
}
