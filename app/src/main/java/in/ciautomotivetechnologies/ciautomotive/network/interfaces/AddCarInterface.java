package in.ciautomotivetechnologies.ciautomotive.network.interfaces;

import in.ciautomotivetechnologies.ciautomotive.network.models.AddCarModel;

/**
 * Created by pariskshitdutt on 19/05/16.
 */
public interface AddCarInterface {
    void carAdded(AddCarModel model);
    void error(int code);
}
