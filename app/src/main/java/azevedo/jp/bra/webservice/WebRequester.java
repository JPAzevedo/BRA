package azevedo.jp.bra.webservice;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import azevedo.jp.bra.C;
import azevedo.jp.bra.R;
import azevedo.jp.bra.entities.HTTPResquetHelper;
import azevedo.jp.bra.interfaces.OnHTTPRequestResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by joaop on 07/07/2017.
 */

public class WebRequester implements Callback{

    private OnHTTPRequestResponse listener;
    private Context context;
    private C.API_TYPE type;
    private final int statusOk = 200;
    private final int statusCreated = 201;

    public WebRequester(OnHTTPRequestResponse listener, Context context){
        this.listener = listener;
        this.context = context;
    }

    public String getData(C.API_TYPE type, HTTPResquetHelper httpResquetHelper){
        this.type = type;
        String url = URLManager.getURL(type,httpResquetHelper);
        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient okHttpClient = getHTTPBuilder().build();
        okHttpClient.newCall(request).enqueue(this);
        return url;
    }

    public void putData(C.API_TYPE type,HTTPResquetHelper httpResquetHelper,String json){
        this.type = type;
        String url = URLManager.getURL(type,httpResquetHelper);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        OkHttpClient okHttpClient = getHTTPBuilder().build();
        okHttpClient.newCall(request).enqueue(this);
    }

    public void postData(C.API_TYPE type,HTTPResquetHelper httpResquetHelper){
        this.type = type;
        String url = URLManager.getURL(type,httpResquetHelper);
        RequestBody body = RequestBody.create(null, new byte[]{});
        Request request = new Request.Builder()
                .url(url)
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(body)
                .build();

        OkHttpClient okHttpClient = getHTTPBuilder().build();
        okHttpClient.newCall(request).enqueue(this);
    }

    public OkHttpClient.Builder getHTTPBuilder(){
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient().newBuilder();
        okHttpClientBuilder.connectTimeout(C.HTTP_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(C.HTTP_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(C.HTTP_TIMEOUT, TimeUnit.SECONDS);
        return okHttpClientBuilder;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        listener.onError(e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        int statusCode = response.code();
        if(statusCode==statusOk || statusCode == statusCreated){
            listener.onSuccess(response.body().string(),type);
        }
        else{
            listener.onError(context.getString(R.string.httprequest_unexpectedresponse));
        }
    }
}
