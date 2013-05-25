package com.manuelpeinado.googledirectionsclient.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.manuelpeinado.googledirectionsclient.GoogleDirectionsClient;
import com.manuelpeinado.googledirectionsclient.GoogleDirectionsResponse;
import com.manuelpeinado.googledirectionsclient.GoogleDirectionsResponseListener;
import com.manuelpeinado.googledirectionsclient.Query;

public class GoogleDirectionsFragment extends Fragment implements GoogleDirectionsResponseListener {
    private GoogleDirectionsClient mClient;
    private GoogleDirectionsResponseListener mListener;

    public static GoogleDirectionsFragment newInstance(double lat0, double lng0, double lat1, double lng1) {
        GoogleDirectionsFragment result = new GoogleDirectionsFragment();
        Bundle args = new Bundle();
        args.putParcelable("query", new Query(lat0, lng0, lat1, lng1));
        result.setArguments(args);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Create and execute the background task, only the first time onAttach is called.
        boolean firstTime = mClient == null;
        if (firstTime) {
            mClient = new GoogleDirectionsClient();
        }

        mListener = (GoogleDirectionsResponseListener) activity;

        if (firstTime) {
            Query query = (Query)getArguments().getParcelable("query");
            mClient.sendRequest(query, this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Set the callback to null so we don't accidentally leak the activity instance.
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mClient != null) {
            mClient.cancel();
        }
    }
    
    @Override
    public void onResponseReady(GoogleDirectionsResponse response) {
        if (mListener != null) {
            mListener.onResponseReady(response);
        }
    }
}
