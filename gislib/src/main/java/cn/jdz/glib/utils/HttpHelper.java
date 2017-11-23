package cn.jdz.glib.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by alex on 2017/9/1.
 */

public class HttpHelper {

    public static Observable<String> getString(final String url){
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        emitter.onNext(response.body().string());
                    }
                });
            }
        });
    }

    public static Observable<File> getFile(final String url,final File file){
        if(file.exists()){
            file.delete();
        }
        return Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<File> emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onError(e);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = response.body().byteStream();
                        FileOutputStream os = new FileOutputStream(file);
                        int len=0;
                        byte[] buffer=new byte[2048];
                        while ((len=is.read(buffer))!=-1)
                        {
                            os.write(buffer,0,len);
                        }
                        os.flush();
                        is.close();
                        os.close();
                        emitter.onNext(file);
                    }
                });
            }
        });
    }

    public static Observable<String> postFile(final String url, final File file, final String filename, final HashMap<String,String> parts){
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                RequestBody filebody = RequestBody.create(MediaType.parse("application/octet-stream"),file);
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file",filename,filebody);
                for(Map.Entry<String,String> entry :parts.entrySet()){
                    builder.addFormDataPart(entry.getKey(),entry.getValue());
                }
                RequestBody requestBody = builder.build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onError(e);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        emitter.onNext(response.body().string());
                    }
                });
            }
        });
    }

    public static Observable<String> postValues(final String url,final HashMap<String,String> parts){
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder builder = new FormBody.Builder();
                for(Map.Entry<String,String> entry :parts.entrySet()){
                    builder.add(entry.getKey(),entry.getValue());
                }
                RequestBody requestBody = builder.build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        emitter.onNext(response.body().string());
                    }
                });
            }
        });
    }

    public static Observable<String> putFile(final String url, final File file, final String filename, final HashMap<String,String> parts){
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                RequestBody filebody = RequestBody.create(MediaType.parse("application/octet-stream"),file);
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file",filename,filebody);
                for(Map.Entry<String,String> entry :parts.entrySet()){
                    builder.addFormDataPart(entry.getKey(),entry.getValue());
                }
                RequestBody requestBody = builder.build();
                Request request = new Request.Builder()
                        .url(url)
                        .put(requestBody)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onError(e);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        emitter.onNext(response.body().string());
                    }
                });
            }
        });
    }

    public static Observable<String> putValues(final String url,final HashMap<String,String> parts){
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder builder = new FormBody.Builder();
                for(Map.Entry<String,String> entry :parts.entrySet()){
                    builder.add(entry.getKey(),entry.getValue());
                }
                RequestBody requestBody = builder.build();
                Request request = new Request.Builder()
                        .url(url)
                        .put(requestBody)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        emitter.onNext(response.body().string());
                    }
                });
            }
        });
    }
}
