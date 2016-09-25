package in.ciautomotivetechnologies.ciautomotive.network.interfaces;

/**
 * Created by pariskshitdutt on 17/05/16.
 */
public interface TokenResultInterface {
    void registered(String token,String secret,String expires);
    void error(int code);
}
