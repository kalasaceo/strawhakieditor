package com.daasuu.camerarecorder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.kalasa.library.R;

import java.util.Arrays;

public class CustomAutoPlace extends AppCompatActivity {
    private String thisAddress="empty";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_placeo);
        newplacefun();
    }
    void newplacefun()
    {
        String apikey="AIzaSyCELR-ypcDe9TrShikNYZddcGK01XOfN4o";
        Places.initialize(getApplicationContext(),apikey);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setCountries("IN");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.NAME));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                thisAddress=place.getAddress();
                finish();
            }
            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }
    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("places", thisAddress);
        this.setResult(RESULT_OK, data);
        super.finish();
    }
}
