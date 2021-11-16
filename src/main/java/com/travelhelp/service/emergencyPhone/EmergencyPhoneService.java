package com.travelhelp.service.emergencyPhone;

import com.travelhelp.domain.EmergencyPhone;
import com.travelhelp.domain.Language;
import com.travelhelp.domain.dto.EmergencyPhoneDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

import java.util.List;

import static com.travelhelp.utils.Constants.URL_BASE;

public class EmergencyPhoneService {

    private IEmergencyPhoneService emergencyPhoneService;

    public EmergencyPhoneService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        emergencyPhoneService = retrofit.create(IEmergencyPhoneService.class);
    }

    public Observable<List<EmergencyPhone>> getAllEmergencyPhones() {
        return emergencyPhoneService.getAllEmergencyPhones();
    }
    public Call<EmergencyPhone> addNewEmergencyPhone(EmergencyPhoneDTO newEmergencyPhoneDTO) {
        return emergencyPhoneService.addNewEmergencyPhone(newEmergencyPhoneDTO);
    }
    public Call<ResponseBody> deleteEmergencyPhone(long id){
        return emergencyPhoneService.deleteEmergencyPhone(id);
    }
    public Call<EmergencyPhone> modifyEmergencyPhone(long id, EmergencyPhoneDTO newEmergencyPhoneDTO){
        return emergencyPhoneService.modifyEmergencyPhone(id, newEmergencyPhoneDTO);
    }

}
