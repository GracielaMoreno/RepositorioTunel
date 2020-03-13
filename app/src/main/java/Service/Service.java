package Service;

import Modelo.Imagen;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Service {
    public static final String URL="http://192.168.43.161:3000/";

    @Multipart
    @POST("/imagen/upload-image")
    Call<Imagen> agregarImagen(@Part MultipartBody.Part image);


}
