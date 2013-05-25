package com.manuelpeinado.googledirectionsclient;

import android.os.Parcel;
import android.os.Parcelable;

public class Query implements Parcelable {
    public double lat0;
    public double lng0;
    public double lat1;
    public double lng1;

    public Query() {
    }

    public Query(double lat0, double lng0, double lat1, double lng1) {
        this.lat0 = lat0;
        this.lng0 = lng0;
        this.lat1 = lat1;
        this.lng1 = lng1;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(lat0);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lat1);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lng0);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lng1);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Query other = (Query) obj;
        if (Double.doubleToLongBits(lat0) != Double.doubleToLongBits(other.lat0))
            return false;
        if (Double.doubleToLongBits(lat1) != Double.doubleToLongBits(other.lat1))
            return false;
        if (Double.doubleToLongBits(lng0) != Double.doubleToLongBits(other.lng0))
            return false;
        if (Double.doubleToLongBits(lng1) != Double.doubleToLongBits(other.lng1))
            return false;
        return true;
    }

    // <Parcelable>
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(lat0);
        out.writeDouble(lng0);
        out.writeDouble(lat1);
        out.writeDouble(lng1);
    }

    public static final Parcelable.Creator<Query> CREATOR = new Parcelable.Creator<Query>() {
        public Query createFromParcel(Parcel in) {
            return new Query(in);
        }

        public Query[] newArray(int size) {
            return new Query[size];
        }
    };

    private Query(Parcel in) {
        lat0 = in.readDouble();
        lng0 = in.readDouble();
        lat1 = in.readDouble();
        lng1 = in.readDouble();
    }
    
    // </Parcelable>

}
