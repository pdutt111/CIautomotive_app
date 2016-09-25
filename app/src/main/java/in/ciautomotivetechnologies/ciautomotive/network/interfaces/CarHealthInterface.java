package in.ciautomotivetechnologies.ciautomotive.network.interfaces;

import in.ciautomotivetechnologies.ciautomotive.network.models.CarHealth;

/**
 * Created by pariskshitdutt on 23/07/16.
 */
public interface CarHealthInterface {
    void response(CarHealth health);
    void error(int code);
}
