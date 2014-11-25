package com.launch.apollo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import com.squareup.okhttp.*;
import com.zerokol.views.JoystickView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    public static final MediaType JSON
            = MediaType.parse("application/json");
    public static final String BBB_URL = "http://192.168.42.1:5000/test/";
    private OkHttpClient client;
    private TextView tAngle;
    private TextView tMagnitude;
    private TextView tResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_center);
        client = new OkHttpClient();

        tAngle = (TextView) findViewById(R.id.cc_angle_value);
        tMagnitude = (TextView) findViewById(R.id.cc_mag_value);
        tResponse = (TextView) findViewById(R.id.cc_response);

        JoystickView mJoystick = (JoystickView) findViewById(R.id.primary_joystick);
        mJoystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                tAngle.setText(String.valueOf(angle));
                tMagnitude.setText(String.valueOf(power));
                sendAngleHTTP dataCall = new sendAngleHTTP();
                dataCall.execute(angle, power);
                //tResponse.setText(response);
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);

    }

    private class sendAngleHTTP extends AsyncTask<Integer, Void, String>{

        @Override
        protected String doInBackground(Integer... params) {
            try {
                int angle = params[0];
                int magnitude = params[1];

                JSONObject data = new JSONObject().put("angle", angle).put("mag", magnitude);
                RequestBody body = RequestBody.create(JSON, data.toString() );
                Request request = new Request.Builder().url(BBB_URL).post(body).build();
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Nothing";
        }
    }
}
