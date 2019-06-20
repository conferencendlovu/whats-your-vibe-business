package za.co.whatsyourvibe.business.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import za.co.whatsyourvibe.business.models.Business;
import za.co.whatsyourvibe.business.models.Event;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("businesses/createBusinessProfile")
    Call<Business> createBusinessAccount(@Field("businessId") String businessId,
                                        @Field("businessName") String businessName,
                                        @Field("businessEmail") String businessEmail);

    @GET("events/getMyEvents/{organiserId}")
    Call<List<Event>> getMyEvents(@Path("organiserId") String organiserId);

    @FormUrlEncoded
    @POST("events/createEvent")
    Call<Event> createEvent(@Field("eventName") String eventName,
                            @Field("eventType") String eventType,
                            @Field("eventLocation") String eventLocation,
                            @Field("overview") String overview,
                            @Field("category") String category,
                            @Field("entryFee") double entryFee,
                            @Field("latitude") double latitude,
                            @Field("longitude") double longitude,
                            @Field("organiserId") String organiserId);

    @GET("businesses/getProfileDetails/{businessId}")
    Call<List<Business>> membershipInfo(@Path("businessId") String businessId);
}
