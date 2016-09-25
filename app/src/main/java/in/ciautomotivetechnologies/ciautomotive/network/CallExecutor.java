package in.ciautomotivetechnologies.ciautomotive.network;

import android.util.Log;

import java.util.List;

import in.ciautomotivetechnologies.ciautomotive.network.interfaces.AddCarInterface;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.CallUpdate;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.CarHealthInterface;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.CarInterface;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.CarLocationInterface;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.GetCarsInterface;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.LoginInterface;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.PhonenumberRegistered;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.TokenResultInterface;
import in.ciautomotivetechnologies.ciautomotive.network.interfaces.TripStartedInterface;
import in.ciautomotivetechnologies.ciautomotive.network.models.AddCarModel;
import in.ciautomotivetechnologies.ciautomotive.network.models.CarHealth;
import in.ciautomotivetechnologies.ciautomotive.network.models.CarLocations;
import in.ciautomotivetechnologies.ciautomotive.network.models.PhonenumberRegistation;
import in.ciautomotivetechnologies.ciautomotive.network.models.PositiveResult;
import in.ciautomotivetechnologies.ciautomotive.network.models.TokenResult;
import in.ciautomotivetechnologies.ciautomotive.network.models.TripStarted;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pariskshitdutt on 17/05/16.
 */
public class CallExecutor {
    final public static int BAD_REQUEST=1;
    final public static int UNAUTHORIZED=2;
    final public static int RANDOM_ERROR=3;
    private static LoginInterface lgInterface;
    private static CarInterface carInterface;
    public CallExecutor(){
        if(lgInterface==null) {
            Log.i("setting up","setting up");
            lgInterface=LoginInterface.retrofit.create(LoginInterface.class);
            carInterface=CarInterface.retrofit.create(CarInterface.class);
        }
    }

    public void postPhonenumber(String ph, final PhonenumberRegistered activityCaller){
        Call<PhonenumberRegistation> regCall = lgInterface.phonenumberRegistration(ph);
        regCall.enqueue(new Callback<PhonenumberRegistation>() {
            @Override
            public void onResponse(Call<PhonenumberRegistation> call, Response<PhonenumberRegistation> response) {
                if(response.code()==200){
                    PhonenumberRegistation phReg=response.body();
                    activityCaller.getpin(phReg.pin);
                }
                else if(response.code()==400){
                    activityCaller.error(BAD_REQUEST);
                }else{
                    activityCaller.error(RANDOM_ERROR);
                }
            }

            @Override
            public void onFailure(Call<PhonenumberRegistation> call, Throwable t) {
                Log.i("error on phonenumber",t.getMessage());
            }
        });
    }
    public void verifyPin(String phonenumber, String pin, final TokenResultInterface tokenResultInterface){
        Call<TokenResult> saveUserCall = lgInterface.saveUser(phonenumber,pin);
        saveUserCall.enqueue(new Callback<TokenResult>() {
            @Override
            public void onResponse(Call<TokenResult> call, Response<TokenResult> response) {
                Log.i("error code",response.code()+"");
                if(response.code()==200){
                    TokenResult result=response.body();
                    tokenResultInterface.registered(result.token,result.secret,result.expires);
                }
                else if(response.code()==401){
                    tokenResultInterface.error(UNAUTHORIZED);
                }else{
                    tokenResultInterface.error(RANDOM_ERROR);
                }
            }

            @Override
            public void onFailure(Call<TokenResult> call, Throwable t) {

            }
        });
    }
    public void updateUser(String token, String name, String email, final TokenResultInterface tokenResultInterface){
        Call<TokenResult> updateUser=lgInterface.updateUser(name,email,token);
        updateUser.enqueue(new Callback<TokenResult>() {
            @Override
            public void onResponse(Call<TokenResult> call, Response<TokenResult> response) {
                Log.i("error code",response.code()+"");
                if(response.code()==200){
                    TokenResult result=response.body();
                    tokenResultInterface.registered(result.token,result.secret,result.expires);
                }
                else if(response.code()==401){
                    tokenResultInterface.error(UNAUTHORIZED);
                }else{
                    tokenResultInterface.error(RANDOM_ERROR);
                }
            }

            @Override
            public void onFailure(Call<TokenResult> call, Throwable t) {

            }
        });
    }
    public void addCar(String name,String model,String mileage,String device_id,String token,final AddCarInterface addCarInterface){
        Log.i("calling add car","calling");
        final Call<AddCarModel> addCar=carInterface.addCar(name,model,device_id,mileage,token);
        addCar.enqueue(new Callback<AddCarModel>() {
            @Override
            public void onResponse(Call<AddCarModel> call, Response<AddCarModel> response) {
                if(response.code()==200){
                    AddCarModel model=response.body();
                    addCarInterface.carAdded(model);
                }else{
                    addCarInterface.error(BAD_REQUEST);
                }

            }

            @Override
            public void onFailure(Call<AddCarModel> call, Throwable t) {
            }
        });
    }
    public void getCars(String token,final GetCarsInterface getCarsInterface){
        final Call<List<AddCarModel>> addCar=carInterface.getCars(token);
        addCar.enqueue(new Callback<List<AddCarModel>>() {
            @Override
            public void onResponse(Call<List<AddCarModel>> call, Response<List<AddCarModel>> response) {
                if(response.code()==200){
                    List<AddCarModel> cars=response.body();
                    getCarsInterface.gotCars(cars);
                }else{
                    getCarsInterface.error(BAD_REQUEST);
                }

            }

            @Override
            public void onFailure(Call<List<AddCarModel>> call, Throwable t) {

            }
        });
    }
    public void deleteCar(String device_id,String token,final CallUpdate callUpdate){
        final Call<PositiveResult> addCar=carInterface.deleteCar(device_id,token);
        addCar.enqueue(new Callback<PositiveResult>() {
            @Override
            public void onResponse(Call<PositiveResult> call, Response<PositiveResult> response) {
                if(response.code()==200) {
                    callUpdate.success();
                }else if(response.code()==401){
                    callUpdate.error(UNAUTHORIZED);
                }else{
                    callUpdate.error(BAD_REQUEST);
                }
            }

            @Override
            public void onFailure(Call<PositiveResult> call, Throwable t) {

            }
        });
    }
    public void getTripStarted(String device_id,String token,final TripStartedInterface started){
        final Call<TripStarted> addCar=carInterface.getTripStarted(device_id,token);
        addCar.enqueue(new Callback<TripStarted>() {
            @Override
            public void onResponse(Call<TripStarted> call, Response<TripStarted> response) {
                if(response.code()==200){
                    TripStarted tripStarted=(TripStarted)response.body();
                    if(tripStarted.result){
                        started.running();
                    }else{
                        started.notRunning();
                    }
                }
            }

            @Override
            public void onFailure(Call<TripStarted> call, Throwable t) {

            }
        });
    }
    public void addDevice(String reg_id,String token,final CallUpdate deviceAddListener){
        final Call<PositiveResult> addDevice=lgInterface.addDevice("Android",reg_id,token);
        addDevice.enqueue(new Callback<PositiveResult>() {
            @Override
            public void onResponse(Call<PositiveResult> call, Response<PositiveResult> response) {
                if(response.code()==200){
                    deviceAddListener.success();
                }else{
                    deviceAddListener.error(response.code());
                }
            }

            @Override
            public void onFailure(Call<PositiveResult> call, Throwable t) {

            }
        });
    }
    public void getCarHealth(String device_id,String token,final CarHealthInterface healthInterface){
        final Call<CarHealth> getCarHealth=carInterface.getCarHealth(token,device_id);
        getCarHealth.enqueue(new Callback<CarHealth>() {
            @Override
            public void onResponse(Call<CarHealth> call, Response<CarHealth> response) {
                if(response.code()==200){
                    CarHealth carHealth=(CarHealth)response.body();
                    healthInterface.response(carHealth);
                }else if(response.code()==400){
                        healthInterface.error(BAD_REQUEST);
                    }else{
                        healthInterface.error(RANDOM_ERROR);
                    }
            }

            @Override
            public void onFailure(Call<CarHealth> call, Throwable t) {

            }
        });
    }
    public void getCarLocation(String device_id,String token,final CarLocationInterface locationInterface){
        final Call<List<CarLocations>> getCarLocation=carInterface.getCarLocations(token,device_id);
        getCarLocation.enqueue(new Callback<List<CarLocations>>() {
            @Override
            public void onResponse(Call<List<CarLocations>> call, Response<List<CarLocations>> response) {
                if(response.code()==200){
                    List<CarLocations> carHealth=(List<CarLocations>)response.body();
                    locationInterface.response(carHealth);
                }else if(response.code()==400){
                    locationInterface.error(BAD_REQUEST);
                }else{
                    locationInterface.error(RANDOM_ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<CarLocations>> call, Throwable t) {

            }
        });

    }
    public void postTripStart(String device_id,String token,final CallUpdate tripInterface){
        final Call<PositiveResult> tripStarted=carInterface.postTripStart(token,device_id);
        tripStarted.enqueue(new Callback<PositiveResult>() {
            @Override
            public void onResponse(Call<PositiveResult> call, Response<PositiveResult> response) {
                if(response.code()==200){
                    tripInterface.success();
                }else if(response.code()==400){
                    tripInterface.error(BAD_REQUEST);
                }else{
                    tripInterface.error(RANDOM_ERROR);
                }
            }

            @Override
            public void onFailure(Call<PositiveResult> call, Throwable t) {

            }
        });

    }
    public void postTripEnd(String device_id,String token,final CallUpdate tripInterface){
        final Call<PositiveResult> tripStarted=carInterface.postTripEnd(token,device_id);
        tripStarted.enqueue(new Callback<PositiveResult>() {
            @Override
            public void onResponse(Call<PositiveResult> call, Response<PositiveResult> response) {
                if(response.code()==200){
                    tripInterface.success();
                }else if(response.code()==400){
                    tripInterface.error(BAD_REQUEST);
                }else{
                    tripInterface.error(RANDOM_ERROR);
                }
            }

            @Override
            public void onFailure(Call<PositiveResult> call, Throwable t) {

            }
        });

    }
}
