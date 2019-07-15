package com.mjacksi.rojprints.Utilises;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("ajax_orders2.php")
    Call<ResponseBody> post(
            @Field("NotificationId") String notificationId,
            @Field("friendPhone") String friendPhone,
            @Field("AuthKey") String authKey,
            @Field("City") String city,
            @Field("isDelivery") String isDelivery,
            @Field("Address") String address,
            @Field("UserName") String userName,
            @Field("Payment") String payment,
            @Field("TotalePrice") String totalePrice,
            @Field("pageJson") String pageJson,
            @Field("Note") String note,
            @Field("PhoneNumber") String phoneNumber,
            @Field("cardsArray") String cardsArray,
            @Field("isGift") String isGift
//"NotificationId="
//        "&friendPhone=" ,

//        "&AuthKey=" , "au
//        "&City=" , city);
//"&isDelivery=" ,
//        "&Address=" , add
//"&UserName=" , na
//"&Payment=" , "ال
//    t("&cards=" + "")
//"&TotalePrice=" ,
//        "&pageJson=" , pa
//"&Note=" , notes)
//            "&PhoneNumber=" ,
//            "&cardsArray=" ,
//            "&isGift=" , Stri
    );
}
