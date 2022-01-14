package br.edu.ifpe.tads.pdm.projeto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.edu.ifpe.tads.pdm.projeto.interfaces.AddBanheiroObserver;
import br.edu.ifpe.tads.pdm.projeto.models.Banheiro;
import br.edu.ifpe.tads.pdm.projeto.models.CustomLatLng;

public class MapsFragment extends Fragment implements AddBanheiroObserver, OnMapReadyCallback {

    private boolean fine_location;
    private int FINE_LOCATION_REQUEST = 2;
    private GoogleMap mMap;
    private AddBanheiroSubject addBanheiroSubject;
    private Banheiro banheiro;
    private DatabaseReference banheirosDr;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_maps, container, false);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);


        if (mapFragment != null) {
            mapFragment.onCreate(null);
            mapFragment.onResume();
            mapFragment.getMapAsync(this);
        }

        Intent i = getActivity().getIntent();
        addBanheiroSubject = (AddBanheiroSubject) i.getSerializableExtra("addBanheiroSubject");
        this.addBanheiroSubject.attach(this);

        FirebaseDatabase fbDB = FirebaseDatabase.getInstance();
        banheirosDr = fbDB.getReference("banheiros");

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(banheiro != null) {
                    banheiro.setLatLng(latLng);
//                    mMap.addMarker(new MarkerOptions().
//                            position(latLng).
//                            title(banheiro.getLocal()).
//                            icon(BitmapDescriptorFactory.defaultMarker(0)));
                    banheirosDr.push().setValue(banheiro);
                    banheiro = null;
                }
            }
        });

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_REQUEST);
            }
            return;
        } else {
            mMap.setMyLocationEnabled(true);
            goToCurrentLocation();
        }

        banheiroListener();

    }

    private void goToCurrentLocation() {
        FusedLocationProviderClient fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(getActivity());
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if(location!=null) {
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            }
        });
    }

    public void banheiroListener() {
        banheirosDr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Banheiro banheiro = dataSnapshot.getValue(Banheiro.class);
                if (banheiro != null) {
                    CustomLatLng customLatLng = banheiro.getCustomLatLng();
                    LatLng latLng = new LatLng(customLatLng.getLatitude(), customLatLng.getLongitude());
                    mMap.addMarker(new MarkerOptions().
                            position(latLng).
                            title(banheiro.getLocal()).
                            snippet(banheiro.getTipo()).
                            icon(BitmapDescriptorFactory.defaultMarker(0)));
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean granted = (grantResults.length > 0) &&
                (grantResults[0] == PackageManager.PERMISSION_GRANTED);
        this.fine_location = (requestCode == FINE_LOCATION_REQUEST) && granted;

        if (mMap != null) {
            mMap.setMyLocationEnabled(this.fine_location);
            goToCurrentLocation();
        }
    }

    @Override
    public void addBanheiro() {
        this.banheiro = this.addBanheiroSubject.getBanheiro();
    }
}