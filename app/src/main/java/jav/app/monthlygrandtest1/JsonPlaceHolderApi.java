package jav.app.monthlygrandtest1;

import java.util.List;

import jav.app.monthlygrandtest1.Model.Products;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    @GET("products")
    Call<List<Products>> getPosts(@Query("limit") String value);

//
//    @GET("country?fields=currency%2Ccurrency_num_code%2Ccurrency_code%2Ccontinent_code%2Ccurrency%2Ciso_a3%2Cdial_code")
//    @Headers({"x-rapidapi-host: referential.p.rapidapi.com",
//            "x-rapidapi-key: 8ff5250409mshe3d8def1795a332p14e5b7jsnef5cf64e2ef8"})
//    Call<List<CountryList>> getPosts();

}
