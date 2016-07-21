package com.mapbox.mapboxsdk.testapp.activity.maplayout;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapFragment;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.testapp.R;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationDrawerFragment navigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        navigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                getSupportActionBar());
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        MapboxMapOptions options = new MapboxMapOptions();
        switch (position) {
            case 1:
                options.styleUrl(Style.DARK);
                options.camera(new CameraPosition.Builder()
                        .target(new LatLng(39.913271, 116.413891))
                        .zoom(12)
                        .build());
                break;
            case 2:
                options.styleUrl(Style.LIGHT);
                options.camera(new CameraPosition.Builder()
                        .target(new LatLng(31.227831, 121.449076))
                        .zoom(6)
                        .build());
                break;
            case 3:
                options.styleUrl(Style.SATELLITE);
                options.camera(new CameraPosition.Builder()
                        .target(new LatLng(22.294297, 114.177891))
                        .zoom(8)
                        .build());
                break;
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, MapFragment.newInstance(options))
                .commit();
    }
}
