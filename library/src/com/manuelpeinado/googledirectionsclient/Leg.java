package com.manuelpeinado.googledirectionsclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Leg {
    public Distance distance;
    public Duration duration;
    public List<Step> steps;
    
    public Collection<Step> getStepList() {
        List<Step> result = new ArrayList<Step>();
        for (Step step : steps) {
            result.add(step);
        }
        return result;
    }
}
