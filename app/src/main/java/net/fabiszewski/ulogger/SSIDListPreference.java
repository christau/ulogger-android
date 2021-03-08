package net.fabiszewski.ulogger;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.AttributeSet;

import androidx.core.app.ActivityCompat;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SSIDListPreference extends MultiSelectListPreference {
    private List<String> ssids = new ArrayList<>();

    public SSIDListPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public SSIDListPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);

    }

    public SSIDListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);

    }

    public SSIDListPreference(Context context) {
        super(context);
        init(context, null, 0, 0);

    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        WifiConfiguration[] ssidList = loadConfiguredNetworksSorted();
        for (WifiConfiguration c: ssidList) {
            ssids.add(c.SSID);
        }
    }

    private WifiConfiguration[] loadConfiguredNetworksSorted() {
        WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
            // if WiFi is turned off, getConfiguredNetworks returns null on many devices
            if (configuredNetworks != null) {
                WifiConfiguration[] result = configuredNetworks.toArray(new WifiConfiguration[configuredNetworks.size()]);
                Arrays.sort(result, new Comparator<WifiConfiguration>() {
                    @Override
                    public int compare(WifiConfiguration lhs, WifiConfiguration rhs) {
                        return lhs.SSID.compareToIgnoreCase(rhs.SSID);
                    }
                });
                return result;
            } // if
        } // if
        // WiFi is turned of or device doesn't have WiFi
        return null;
    }


    @Override
    public CharSequence[] getEntries() {
        return ssids.toArray(new String[0]);
    }

    @Override
    public CharSequence[] getEntryValues() {
        return ssids.toArray(new String[0]);
    }
}
