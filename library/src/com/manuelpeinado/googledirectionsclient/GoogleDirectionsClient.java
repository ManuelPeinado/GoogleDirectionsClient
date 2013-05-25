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
    private boolean mMockSlowResponse = true;

    private class Task extends AsyncTask<Void, Void, Object> {
        GoogleDirectionsResponseListener responseListener;
        Query query;

        private Task(Query query) {
            this.query = query;
        }

        @Override
        protected Object doInBackground(Void... params) {
            return getDirectionsSync(query);
        }

        @Override
        protected void onPostExecute(Object response) {
            if (responseListener != null) {
                responseListener.onResponseReady((GoogleDirectionsResponse) response);
            }
        }

        public void setResponseListener(GoogleDirectionsResponseListener callback) {
            this.responseListener = callback;
        }
    }

    public GoogleDirectionsResponse getDirectionsSync(Query query) {
        try {
            String json = getRawResponseSync(query);
            if (json != null) {
                Gson gson = new Gson();
                return gson.fromJson(json, GoogleDirectionsResponse.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRawResponseSync(Query query) {
        try {
            if (mMockSlowResponse) {
                Thread.sleep(10000);
            }
            String start = String.format(LOCATION_ARG, query.lat0, query.lng0);
            String end = String.format(LOCATION_ARG, query.lat1, query.lng1);
            String args = String.format(ARGS, encode(start), encode(end));
            String url = BASE_URL + args;
            return new Resty().text(url).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String encode(String arg) throws UnsupportedEncodingException {
        return URLEncoder.encode(arg, ENCODING);
    }

    public void sendRequest(Query query, GoogleDirectionsResponseListener responseListener) {
        if (mTask != null) {
            mTask.setResponseListener(null);
        }
        mTask = new Task(query);
        mTask.setResponseListener(responseListener);
        mTask.execute();
    }

    public void setResponseListener(GoogleDirectionsResponseListener listener) {
        if (mTask != null) {
            mTask.setResponseListener(listener);
        }
    }

    public void cancel() {
        if (mTask != null) {
            mTask.cancel(true);
            mTask = null;
        }
    }
}