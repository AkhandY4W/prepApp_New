/*
 * Copyright 2012 Kevin Sawicki <kevinsawicki@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.youth4work.prepapp.util;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;
import static android.location.Criteria.ACCURACY_FINE;
import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static android.location.LocationManager.PASSIVE_PROVIDER;

public class LocationUtils {

    @Nullable
    private static Location getLatest(@Nullable final Location location1,
                                      @Nullable final Location location2) {
        if (location1 == null)
            return location2;

        if (location2 == null)
            return location1;

        if (location2.getTime() > location1.getTime())
            return location2;
        else
            return location1;
    }

    @Nullable
    public static Location getLatestLocation(@NonNull final Context context) {
        LocationManager manager = (LocationManager) context
                .getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(ACCURACY_FINE);
        String provider = manager.getBestProvider(criteria, true);
        Location bestLocation;
        if (provider != null)
            bestLocation = manager.getLastKnownLocation(provider);
        else
            bestLocation = null;
        Location latestLocation = getLatest(bestLocation,
                manager.getLastKnownLocation(GPS_PROVIDER));
        latestLocation = getLatest(latestLocation,
                manager.getLastKnownLocation(NETWORK_PROVIDER));
        latestLocation = getLatest(latestLocation,
                manager.getLastKnownLocation(PASSIVE_PROVIDER));
        return latestLocation;
    }

    @Nullable
    public static Address getAddress(final Context context,
                                     @Nullable final Location location) {
        if (location == null)
            return null;

        final Geocoder geocoder = new Geocoder(context);
        final List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
        } catch (IOException e) {
            return null;
        }
        if (addresses != null && !addresses.isEmpty())
            return addresses.get(0);
        else
            return null;
    }
}
