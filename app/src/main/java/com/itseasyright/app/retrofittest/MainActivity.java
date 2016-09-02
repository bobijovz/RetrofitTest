package com.itseasyright.app.retrofittest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.itseasyright.app.retrofittest.api.models.Feeds;
import com.itseasyright.app.retrofittest.application.MainApplication;
import com.itseasyright.app.retrofittest.event.GetGithubFeeds;
import com.squareup.otto.Subscribe;


public class MainActivity extends AppCompatActivity {
    private MainApplication app;
    //private String token;
    //Pattern r = Pattern.compile(".(?<='\\B).*|.*(?='.*').");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = new MainApplication();
        app.bus.post(new GetGithubFeeds());
        //new GetToken().execute("https://dotcmrt3.gov.ph/cctv");

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
    }

    @Subscribe
    public void getFeed(Feeds response){
        Log.d("Feed",response.getTimeline_url());
    }


    /*public class GetToken extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls) {
            String token = "";
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(urls[0]).openConnection();
                conn.setReadTimeout(10000 *//* milliseconds *//*);
                conn.setConnectTimeout(15000 *//* milliseconds *//*);
                conn.setRequestMethod("GET");
                conn.connect();

                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                //StringBuilder str = new StringBuilder();
                String line = null;
                while((line = reader.readLine()) != null)
                {
                    if (line.contains("window._token"))
                        token = line;
                }
                in.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return token.replaceAll(".(?<='\\B).*|.*(?='.*').","");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("token",s);
            app.bus.post(new GetSnapshotsRequest(s,"1"));
        }
    }*/
}
