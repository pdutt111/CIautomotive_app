package in.ciautomotivetechnologies.ciautomotive.network.interfaces;

import java.util.List;

import in.ciautomotivetechnologies.ciautomotive.network.models.CarLocations;

/**
 * Created by pariskshitdutt on 24/07/16.
 */
public interface CarLocationInterface {

    void response(List<CarLocations> carHealth);

    void error(int error);
}
