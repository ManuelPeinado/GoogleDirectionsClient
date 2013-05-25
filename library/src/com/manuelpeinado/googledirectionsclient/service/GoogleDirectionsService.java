package com.manuelpeinado.googledirectionsclient.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.manuelpeinado.googledirectionsclient.GoogleDirectionsClient;
import com.manuelpeinado.googledirectionsclient.GoogleDirectionsResponse;
import com.manuelpeinado.googledirectionsclient.Query;

public class GoogleDirectionsService extends IntentService {

    public static final String RESPONSE_ACTION = "com.manuelpeinado.googledirectionsclient.RESPONSE_READY";
    public static final String PARAM_OUT_SUCCESS = "success";
    public static final String PARAM_IN_QUERY = "query";
    public static final String PARAM_OUT_RESPONSE = "response";

    public static class IntentBuilder {
        private Query query = new Query();

        public IntentBuilder setOrigin(double lat, double lng) {
            query.lat0 = lat;
            query.lng0 = lng;
            return this;
        }

        public IntentBuilder setDestination(double lat, double lng) {
            query.lat1 = lat;
            query.lng1 = lng;
            return this;
        }

        public Intent build(Context context) {
            Bundle extras = new Bundle();
            extras.putParcelable(PARAM_IN_QUERY, query);
            return new Intent(context, GoogleDirectionsService.class).putExtras(extras);
        }

        public Query getQuery() {
            return query;
        }
    }

    public GoogleDirectionsService() {
        super("GoogleDirectionsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GoogleDirectionsClient task = new GoogleDirectionsClient();
        Query query = intent.getParcelableExtra(PARAM_IN_QUERY);
        String json = task.getRawResponseSync(query);
        Intent resultIntent = new Intent(RESPONSE_ACTION);
        if (json != null) {
            GoogleDirectionsResponse response = GoogleDirectionsResponse.fromRawJson(json);
            resultIntent.putExtra(PARAM_OUT_RESPONSE, response);
            resultIntent.putExtra(PARAM_OUT_SUCCESS, true);
        } else {
            resultIntent.putExtra(PARAM_OUT_SUCCESS, false);
        }
        resultIntent.putExtra(PARAM_IN_QUERY, query);
        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
    }
}
