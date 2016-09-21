package com.itseasyright.app.retrofittest;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.itseasyright.app.retrofittest.api.models.Snapshots;
import com.itseasyright.app.retrofittest.application.MainApplication;
import com.itseasyright.app.retrofittest.event.GetSnapshots;
import com.squareup.otto.Subscribe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import retrofit2.http.GET;
import rx.Observable;
import rx.Observer;
import rx.Subscription;


public class MainActivity extends AppCompatActivity {
    private MainApplication app;
    private SharedPreferences sharedPref;
    private ImageView snap;
    private CheckBox up;
    private Subscription _subscription2 = null;
    //private String token;
    //Pattern r = Pattern.compile(".(?<='\\B).*|.*(?='.*').");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = new MainApplication();
        sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        snap = (ImageView) findViewById(R.id.imageview_snapshot);
        up = (CheckBox) findViewById(R.id.checkbox_toggle_update);


        //snap.setEnabled(false);

        /*snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.bus.post(new GetSnapshots("1", sharedPref.getString(getString(R.string.saved_token), "null")));
            }
        });*/
        new CheckPref(this, snap).execute();
        up.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    subscribeUpdate();
                } else {
                    RxUtils.unsubscribeIfNotNull(_subscription2);
                }
            }
        });
    }

    public void subscribeUpdate(){
        _subscription2 = Observable//
                .interval(0, 1, TimeUnit.SECONDS)//
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        //app.bus.post(new GetSnapshots("1", sharedPref.getString(getString(R.string.saved_token), "null")));
                        //_log(String.format("C3 [%s] XXXX COMPLETE", _getCurrentTimestamp()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        //Timber.e(e, "something went wrong in TimingDemoFragment example");
                    }

                    @Override
                    public void onNext(Long number) {
                        //_log(String.format("C3 [%s]     NEXT", _getCurrentTimestamp()));
                        app.bus.post(new GetSnapshots("1", sharedPref.getString(getString(R.string.saved_token), "null")));
                    }
                });
    }

    @Subscribe
    public void getSnapsService(Snapshots response) {
        if (response != null) {
            Log.d("response", response.getStreams().get(0).getSnapshots());

            byte[] decodedString = Base64.decode(response.getStreams().get(0).getSnapshots(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            snap.setImageBitmap(decodedByte);
          /*Log.d("image",decodedByte);
          Picasso.with(getApplicationContext())
                  .load(decodedByte)
                  .fit()
                  .centerInside()
                  .into(snap);*/
        }
        //  Log.d("response",response.getStreams().get(0).getFramecount());
    }


    public class CheckPref extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        private SharedPreferences sharedPref;
        private ImageView snap;

        CheckPref(Context context, ImageView snap) {
            this.context = context;
            this.snap = snap;
            sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String token = "null";
            while (token.contentEquals("null")) {
                token = sharedPref.getString(context.getString(R.string.saved_token), "null");
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            this.snap.setEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        app.bus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        app.bus.unregister(this);
        RxUtils.unsubscribeIfNotNull(_subscription2);
    }
}
