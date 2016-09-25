package in.ciautomotivetechnologies.ciautomotive.network.interfaces;

import java.util.List;

import in.ciautomotivetechnologies.ciautomotive.network.models.CarLocations;
import in.ciautomotivetechnologies.ciautomotive.network.models.AddCarModel;
import in.ciautomotivetechnologies.ciautomotive.network.models.CarHealth;
import in.ciautomotivetechnologies.ciautomotive.network.models.PositiveResult;
import in.ciautomotivetechnologies.ciautomotive.network.models.TripStarted;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by pariskshitdutt on 19/05/16.
 */
public interface CarInterface {
    String devUrl="http://52.76.209.202:3340";

    @FormUrlEncoded
    @POST("/api/v1/cars/protected/car")
    Call<AddCarModel> addCar(@Field("name") String name,
                             @Field("model") String model,
                             @Field("device_id") String device_id,
                             @Field("mileage") String mileage,
                             @Header("Authorization") String authorization);

    @FormUrlEncoded
    @HTTP(method="DELETE",path="/api/v1/cars/protected/car/delete",hasBody=true)
    Call<PositiveResult> deleteCar(@Field("device_id") String device_id,
                                   @Header("Authorization") String authorization);

    @GET("/api/v1/cars/protected/car/all")
    Call<List<AddCarModel>> getCars(@Header("Authorization") String authorization);

    @GET("/api/v1/cars/protected/car/trip/is_started")
    Call<TripStarted> getTripStarted(@Header("Authorization") String authorization, @Query("device_id") String device_id);
    @GET("/api/v1/cars/protected/car/health")
    Call<CarHealth> getCarHealth(@Header("Authorization") String authorization, @Query("device_id") String device_id);

    @GET("/api/v1/cars/protected/car/locations")
    Call<List<CarLocations>> getCarLocations(@Header("Authorization") String authorization, @Query("device_id") String device_id);

    @FormUrlEncoded
    @HTTP(method="POST",path="/api/v1/cars/protected/car/trip/start",hasBody=true)
    Call<PositiveResult> postTripStart(@Header("Authorization") String authorization,@Field("device_id") String device_id);

    @FormUrlEncoded
    @HTTP(method="POST",path="/api/v1/cars/protected/car/trip/end",hasBody=true)
    Call<PositiveResult> postTripEnd(@Header("Authorization") String authorization,@Field("device_id") String device_id);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(devUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
