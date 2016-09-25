package in.ciautomotivetechnologies.ciautomotive.network.interfaces;

import java.util.List;

import in.ciautomotivetechnologies.ciautomotive.network.models.AddCarModel;

/**
 * Created by pariskshitdutt on 20/05/16.
 */
public interface GetCarsInterface {
    void gotCars(List<AddCarModel> cars);
    void error(int code);
}
