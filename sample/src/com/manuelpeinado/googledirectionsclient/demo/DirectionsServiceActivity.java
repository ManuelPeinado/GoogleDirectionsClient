package com.manuelpeinado.googledirectionsclient.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.manuelpeinado.googledirectionsclient.GoogleDirectionsResponse;
import com.manuelpeinado.googledirectionsclient.GoogleDirectionsResponseListener;
import com.manuelpeinado.googledirectionsclient.Query;
import com.manuelpeinado.googledirectionsclient.service.GoogleDirectionsService;

public class DirectionsServiceActivity extends SherlockFragmentActivity implements GoogleDirectionsResponseListener {

    private Button mButton;
    private Query mLastQuery;
    private LocalBroadcastManager mLocalBroadcastManager;
    private GeocodingReceiver mGeocodingReceiver = new GeocodingReceiver();

    private class GeocodingReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Query query = intent.getParcelableExtra(GoogleDirectionsService.PARAM_IN_QUERY);
            if (!query.equals(mLastQuery)) {
                return;
            }
            boolean success = intent.getBooleanExtra(GoogleDirectionsService.PARAM_OUT_SUCCESS, false);
            if (success) {
                GoogleDirectionsResponse response = intent
                        .getParcelableExtra(GoogleDirectionsService.PARAM_OUT_RESPONSE);
                onResponseReady(response);
            } else {
                onResponseReady(null);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLastQuery = savedInstanceState.getParcelable("lastQuery");
        }
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        setContentView(R.layout.activity_directions);
        mButton = (Button) findViewById(R.id.button);
        initButtonText();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("lastQuery", mLastQuery);
    }

    private void initButtonText() {
        if (mLastQuery != null) {
            mButton.setText(android.R.string.cancel);
            setProgressBarIndeterminateVisibility(true);
        } else {
            mButton.setText(R.string.get_directions);
            setProgressBarIndeterminateVisibility(false);
        }
    }

    public void getDirections(View v) {
        if (mLastQuery != null) {
            cleanUpAfterResponse();
        } else {
            startDirectionsService();
            setProgressBarIndeterminateVisibility(true);
            mButton.setText(android.R.string.cancel);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(GoogleDirectionsService.RESPONSE_ACTION);
        mLocalBroadcastManager.registerReceiver(mGeocodingReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocalBroadcastManager.unregisterReceiver(mGeocodingReceiver);
    }

    private void startDirectionsService() {
        GoogleDirectionsService.IntentBuilder intentBuilder = new GoogleDirectionsService.IntentBuilder();
        intentBuilder.setOrigin(Common.MADRID_LAT, Common.MADRID_LNG);
        intentBuilder.setDestination(Common.BARCELONA_LAT, Common.BARCELONA_LNG);
        mLastQuery = intentBuilder.getQuery();
        Intent intent = intentBuilder.build(this);
        startService(intent);
    }

    @Override
    public void onResponseReady(GoogleDirectionsResponse response) {
        cleanUpAfterResponse();
        if (response != null) {
            String fmt = "The trip from Madrid to Barcelona takes: %s";
            String text = String.format(fmt, response.getDuration());
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.get_directions_error, Toast.LENGTH_LONG).show();
        }
    }

    private void cleanUpAfterResponse() {
        mLastQuery = null;
        mButton.setText(R.string.get_directions);
        setProgressBarIndeterminateVisibility(false);
    }

}
