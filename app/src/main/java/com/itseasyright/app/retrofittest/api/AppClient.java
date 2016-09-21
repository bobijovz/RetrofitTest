package com.itseasyright.app.retrofittest.api;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.itseasyright.app.retrofittest.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashSet;
import java.util.prefs.Preferences;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jovijovs on 01/09/2016.
 */
public class AppClient {
    public static final String API_BASE_URL = "https://dotcmrt3.gov.ph/api/";
    private static AppClient appClient;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static HttpLoggingInterceptor interceptor;
    private Retrofit retrofit;

    public static AppClient getClient(Context context) {

        if (appClient == null)
            appClient = new AppClient(context);
        return appClient;
    }

    public Retrofit retrofit() {
        return this.retrofit;
    }

    private AppClient(Context context) {
        new GetToken(context).execute("https://dotcmrt3.gov.ph/cctv/");
    }

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    public class AddCookiesInterceptor implements Interceptor {
        public static final String PREF_COOKIES = "PREF_COOKIES";
        private Context context;
        private String cookie;
        public AddCookiesInterceptor(Context context, String cookie) {
            this.context = context;
            this.cookie = cookie;
        }
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            /*HashSet<String> preferences = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).getStringSet(PREF_COOKIES, new HashSet<String>());
            for (String cookie : preferences) {
                builder.addHeader("Cookie", cookie);
            }*/
            builder.addHeader("Cookie", cookie);
            return chain.proceed(builder.build());
        }
    }

    public class ReceivedCookiesInterceptor implements Interceptor {
        private Context context;
        public ReceivedCookiesInterceptor(Context context) {
            this.context = context;
        }
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).getStringSet("PREF_COOKIES", new HashSet<String>());

                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                }

                SharedPreferences.Editor memes = PreferenceManager.getDefaultSharedPreferences(context).edit();
                memes.putStringSet("PREF_COOKIES", cookies).apply();
                memes.commit();
            }
            return originalResponse;
        }
    }


    public class GetToken extends AsyncTask<String,Void,String[]> {
        private Context context;
        private SharedPreferences sharedPref;
        private SharedPreferences.Editor editor;
        GetToken(Context context){
            this.context = context;
            sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key),Context.MODE_PRIVATE);
            editor = sharedPref.edit();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(String... urls) {
            String token = "";
            String sCookie = "";
            String[] s = new String[3];
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(urls[0]).openConnection();
                conn.setReadTimeout(10000);
                //conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Cookie", sCookie);
                conn.connect();

                InputStream in = conn.getInputStream();
                sCookie = conn.getHeaderField("Set-Cookie");
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line = null;
                while((line = reader.readLine()) != null)
                {
                    if (line.contains("window._token"))
                        token = line;
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            s[0] = sCookie;
            s[1] = token.replaceAll(".(?<='\\B).*|.*(?='.*').","");
            return s;
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            if (s[0]!=null){
                editor.putString(context.getString(R.string.saved_cookies), s[0]) ;
            }
            if (s[1]!=null){
                editor.putString(context.getString(R.string.saved_token), s[1]) ;
            }
            editor.commit();

            interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(interceptor);
            httpClient.addInterceptor(new AddCookiesInterceptor(context, s[0])); // VERY VERY IMPORTANT
            httpClient.addInterceptor(new ReceivedCookiesInterceptor(context));

            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            Log.d("cookie",s[0]);
            Log.d("token",s[1]);
        }
    }

}
