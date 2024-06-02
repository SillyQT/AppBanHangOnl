package com.example.manager.retrofit;

import com.example.manager.model.NotiResponse;
import com.example.manager.model.NotiSendData;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiPushNotification {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAraoc6fE:APA91bHAYn7DLuhZqu9sKr7TQCz-mveG11DfuXMY2MxsoiJT0wdeepnJTnu6_pnlstbOS9MH0nJx9u0VyjzOLVGrPE0B-8QDnP3HtflH0mLjax6ebKFNta-pafz-bS2_2qZsu3Y8_vrW",

            }
    )
    @POST("fcm/send")
    Observable<NotiResponse> senNotification(@Body NotiSendData data);
}
