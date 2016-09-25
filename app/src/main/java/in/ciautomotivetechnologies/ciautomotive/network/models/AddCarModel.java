package in.ciautomotivetechnologies.ciautomotive.network.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pariskshitdutt on 19/05/16.
 */
public class AddCarModel implements Parcelable {
    public String name,model,device_id,mileage,created_time,modified_time;

    @Override
    public int describeContents() {
        return 0;
    }
    public AddCarModel(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.name = data[0];
        this.model = data[1];
        this.device_id = data[2];
        this.mileage = data[3];
        this.created_time = data[4];
        this.modified_time = data[5];

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.name,
                this.model,
                this.device_id,
                this.mileage,
                this.created_time,
                this.modified_time
        });
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AddCarModel createFromParcel(Parcel in) {
            return new AddCarModel(in);
        }

        public AddCarModel[] newArray(int size) {
            return new AddCarModel[size];
        }
    };
}
