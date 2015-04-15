package net.toilet.my.is.where.com.whereismytoilet.ModelView;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.toilet.my.is.where.com.whereismytoilet.R;
import net.toilet.my.is.where.com.whereismytoilet.View.Toilette;

import java.util.List;
import java.util.Locale;

public class DetailMapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Toolbar detailToolBar;
    private Toilette toilette;
    private TextView AddressText, CityText, ObservationText;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
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

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.FRANCE);
            String locationName = (toilette.getAdresse() + " " + toilette.getVille());
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
            mMap.addMarker(new MarkerOptions().position(new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude())).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(addresses.get(0).getLatitude(),
                    addresses.get(0).getLongitude()), 17.0f));

        }catch (Exception ex){
            Toast.makeText(this, "Location non trouvée", Toast.LENGTH_LONG).show();
        }
    }
}
