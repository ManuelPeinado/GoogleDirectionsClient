package com.manuelpeinado.googledirectionsclient;

import java.util.ArrayList;
import java.util.List;

public class Route {
    public List<Leg> legs;

    public List<Step> getAllSteps() {
        List<Step> result = new ArrayList<Step>();
        for (Leg leg : legs) {
            result.addAll(leg.getStepList());
        }
        return result;
    }
}
