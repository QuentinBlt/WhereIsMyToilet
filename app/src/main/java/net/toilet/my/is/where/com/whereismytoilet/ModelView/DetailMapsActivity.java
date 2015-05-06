package net.toilet.my.is.where.com.whereismytoilet.ModelView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.toilet.my.is.where.com.whereismytoilet.R;
import net.toilet.my.is.where.com.whereismytoilet.Models.Toilette;

import java.util.List;
import java.util.Locale;

public class DetailMapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Toolbar detailToolBar;
    private Toilette toilette;
    private TextView AddressText, CityText, ObservationText;

    private double latitude, longitude;
    private String fullAddress;

    public static final String INTENT_MAINTODETAIL_VILLE = "12345";
    public static final String INTENT_MAINTODETAIL_ADRESSE = "12346";
    public static final String INTENT_MAINTODETAIL_OBSERVATION = "12347";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_maps);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            toilette = new Toilette(extras.getString(INTENT_MAINTODETAIL_ADRESSE),
                    extras.getString(INTENT_MAINTODETAIL_VILLE),
                    extras.getString(INTENT_MAINTODETAIL_OBSERVATION));
        }
        setWidget();
        setUpMapIfNeeded();
    }

    private void setWidget(){
        detailToolBar = (Toolbar)findViewById(R.id.activity_detailtoilet_toolbar);
        AddressText = (TextView)findViewById(R.id.activity_detailtoilet_address);
        AddressText.setText(toilette.getAdresse());
        CityText = (TextView)findViewById(R.id.activity_detailtoilet_city);
        CityText.setText(toilette.getVille());
        ObservationText = (TextView)findViewById(R.id.activity_detailtoilet_observation);
        ObservationText.setText(toilette.getObservation());
        setToolBar();
    }


    private void setToolBar(){
        detailToolBar.inflateMenu(R.menu.menu_detail_toilet);
        detailToolBar.setLogo(R.drawable.ic_launcher);
        detailToolBar.setNavigationContentDescription("");
        detailToolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        detailToolBar.setTitle("");
        detailToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setListenerToolBar();
    }

    private void setListenerToolBar(){
        detailToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_detail_toilet_share:
                        ShareContent();
                        break;
                    case R.id.menu_detail_toilet_locate:
                        NavigationTo();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap() {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.FRANCE);
            fullAddress = (toilette.getAdresse() + " " + toilette.getVille());
            List<Address> addresses = geocoder.getFromLocationName(fullAddress, 1);
            latitude = addresses.get(0).getLatitude();
            longitude = addresses.get(0).getLongitude();
            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17.0f));

        }catch (Exception ex){
            Toast.makeText(this, "Location non trouvée", Toast.LENGTH_LONG).show();
        }
    }

    private void ShareContent(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Voici les toilettes publiques que je viens de trouver :\n" + fullAddress);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void NavigationTo(){
        Geocoder geocoder = new Geocoder(this, Locale.FRANCE);
        Uri routeUri = Uri.parse("http://maps.google.com/maps?" +"&daddr=" + fullAddress);

        Intent i = new Intent(Intent.ACTION_VIEW, routeUri);
        startActivity(i);
    }
}
