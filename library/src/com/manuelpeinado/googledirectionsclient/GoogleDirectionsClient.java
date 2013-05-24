package com.manuelpeinado.googledirectionsclient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import us.monoid.web.Resty;
import android.os.AsyncTask;

import com.google.gson.Gson;

public class GoogleDirectionsClient {
    private static String BASE_URL = "http://maps.googleapis.com/maps/api/directions/json?";
    private static String ENCODING = "UTF-8";
    private static String ARGS = "origin=%s&destination=%s&sensor=true";
    private static String LOCATION_ARG = "%s,%s";
    private Task mTask;

    private class Task extends AsyncTask<Void, Void, GoogleDirectionsResponse> {
        GoogleDirectionsResponseListener responseListener;
        private final double lat0;
        private final double lon0;
        private final double lat1;
        private final double lon1;

        private Task(double lat0, double lon0, double lat1, double lon1) {
            this.lat0 = lat0;
            this.lon0 = lon0;
            this.lat1 = lat1;
            this.lon1 = lon1;
        }

        @Override
        protected GoogleDirectionsResponse doInBackground(Void... params) {
            return getDirectionsSync(lat0, lon0, lat1, lon1);
        }

        @Override
        protected void onPostExecute(GoogleDirectionsResponse response) {
            if (responseListener != null) {
                responseListener.onResponseReady(response);
            }
        }

        public void setResponseListener(GoogleDirectionsResponseListener callback) {
            this.responseListener = callback;
        }
    }

    public GoogleDirectionsResponse getDirectionsSync(double lat0, double lon0, double lat1, double lon1) {
        try {
            String start = String.format(LOCATION_ARG, lat0, lon0);
            String end = String.format(LOCATION_ARG, lat1, lon1);
            String args = String.format(ARGS, encode(start), encode(end));
            String url = BASE_URL + args;
            String json = new Resty().text(url).toString();
            Gson gson = new Gson();
            return gson.fromJson(json, GoogleDirectionsResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String encode(String arg) throws UnsupportedEncodingException {
        return URLEncoder.encode(arg, ENCODING);
    }

    public void sendRequest(double lat0, double lon0, double lat1, double lon1,
            GoogleDirectionsResponseListener responseListener) {
        if (mTask != null) {
            mTask.setResponseListener(null);
        }
        mTask = new Task(lat0, lon0, lat1, lon1);
        mTask.setResponseListener(responseListener);
        mTask.execute();
    }

    public void setResponseListener(GoogleDirectionsResponseListener listener) {
        if (mTask != null) {
            mTask.setResponseListener(listener);
        }
    }
}