package com.manuelpeinado.googledirectionsclient.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.manuelpeinado.googledirectionsclient.GoogleDirectionsResponse;
import com.manuelpeinado.googledirectionsclient.GoogleDirectionsResponseListener;
import com.manuelpeinado.googledirectionsclient.fragment.GoogleDirectionsFragment;

public class DirectionsFragmentActivity extends SherlockFragmentActivity implements GoogleDirectionsResponseListener {

    private static final String DIRECTIONS_FRAGMENT = "geocoding";
    private Button mButton;
    private FragmentManager mFragmentManager;
    private static final double MADRID_LAT = 40.4;
    private static final double MADRID_LNG = -3.683333;
    private static final double BARCELONA_LAT = 41.383333; 
    private static final double BARCELONA_LNG = 2.183333;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_directions_fragment);
        mButton = (Button) findViewById(R.id.button);
        initButtonText();
    }

    private void initButtonText() {
        Fragment geocodingFragment = getGoogleDirectionsFragment();
        if (geocodingFragment == null) {
            mButton.setText(R.string.get_directions);
            setProgressBarIndeterminateVisibility(false);
        } else {
            mButton.setText(android.R.string.cancel);
            setProgressBarIndeterminateVisibility(true);
        }
    }

    private GoogleDirectionsFragment getGoogleDirectionsFragment() {
        return (GoogleDirectionsFragment) mFragmentManager.findFragmentByTag(DIRECTIONS_FRAGMENT);
    }

    public void getDirections(View v) {
        Fragment directionsFragment = getGoogleDirectionsFragment();
        if (directionsFragment != null) {
            cleanUpAfterResponse();
        } else {
            directionsFragment = GoogleDirectionsFragment.newInstance(MADRID_LAT, MADRID_LNG, BARCELONA_LAT,
                    BARCELONA_LNG);
            attach(directionsFragment, DIRECTIONS_FRAGMENT);
            setProgressBarIndeterminateVisibility(true);
            mButton.setText(android.R.string.cancel);
        }
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
        Fragment geocodingFragment = getGoogleDirectionsFragment();
        detach(geocodingFragment);
        mButton.setText(R.string.get_directions);
        setProgressBarIndeterminateVisibility(false);
    }

    private void attach(Fragment fragment, String tag) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.add(fragment, tag);
        ft.commit();
    }

    private void detach(Fragment fragment) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }
}
