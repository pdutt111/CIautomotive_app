package in.ciautomotivetechnologies.ciautomotive.network.interfaces;

import in.ciautomotivetechnologies.ciautomotive.network.models.PhonenumberRegistation;
import in.ciautomotivetechnologies.ciautomotive.network.models.PositiveResult;
import in.ciautomotivetechnologies.ciautomotive.network.models.TokenResult;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;


/**
 * Created by pariskshitdutt on 17/05/16.
 */
public interface LoginInterface {
    String devUrl="http://52.76.209.202:3340";
    @FormUrlEncoded
    @POST("/api/v1/users/create")
    Call<PhonenumberRegistation> phonenumberRegistration( @Field("phonenumber") String phonenumber);

    @FormUrlEncoded
    @POST("/api/v1/users/verifyPhonenumber")
    Call<TokenResult> saveUser(@Field("phonenumber") String phonenumber,
                                          @Field("pin") String pin);
    @FormUrlEncoded
    @POST("/api/v1/users/protected/updateUserProfile")
    Call<TokenResult> updateUser(@Field("name") String name,
                               @Field("email") String email,@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("/api/v1/users/protected/device")
    Call<PositiveResult> addDevice(@Field("service") String service,
                                 @Field("reg_id") String reg_id,@Header("Authorization") String authorization);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(devUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
