package net.toilet.my.is.where.com.whereismytoilet.ModelView;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import net.toilet.my.is.where.com.whereismytoilet.Adapters.ToiletListAdapter;
import net.toilet.my.is.where.com.whereismytoilet.R;
import net.toilet.my.is.where.com.whereismytoilet.View.Toilette;
import net.toilet.my.is.where.com.whereismytoilet.View.ToiletteList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MainInterfaceActivity extends Activity implements ToiletteList.ToiletteListEvent {

    private Toolbar toolbar;
    private SwipeRefreshLayout RefreshLayout;
    private ListView ToiletListView;
    private ToiletteList toilets;
    ToiletListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface);
        SetWidget();
        RefreshToiletsWebService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.menu_main_interface_search);

        return super.onCreateOptionsMenu(menu);
    }

    private void RefreshToiletsWebService() {
        toilets = new ToiletteList(this, this);
        toilets.execute(new String[]{});
    }

    private void RefreshListView(){
        List<String> wc = toilets.GetListAddresses();
        String[] toiletslist = wc.toArray(new String[wc.size()]);

        mAdapter =  new ToiletListAdapter(this);
        HashMap maps = toilets.getToiletsByCity();
        String[] keys = (String[])( maps.keySet().toArray( new String[maps.size()] ) );
        Arrays.sort(keys);
        for (String key : keys) {
            mAdapter.addSectionHeaderItem(key);
            for (Toilette wcs : toilets.toilettesToShow) {
                if(key.equals(wcs.getVille()))
                    mAdapter.addItem(wcs);
            }
        }
        ToiletListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void SetWidget(){
        toolbar = (Toolbar) findViewById(R.id.activity_maininterface_toolbar);
        ToiletListView = (ListView) findViewById(R.id.activity_maininterface_listview);
        RefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_maininterface_swipe_refresh_layout);
        RefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                RefreshLayout.setRefreshing(true);
            }
        });
        RefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshToiletsWebService();
            }
        });
        ToiletListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Toilette toilette = (Toilette)parent.getItemAtPosition(position);
                    callDetailInterface(toilette);
                }catch (Exception ex){

                }
            }
        }) ;

        SetToolBar();
    }

    private void SetToolBar(){
        toolbar.inflateMenu(R.menu.menu_main_interface);
        toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.setTitle("Where is my Toilet?");
        SetListenerToolBar();
    }

    private void SetListenerToolBar(){
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_main_interface_localisation:
                        break;
                }
                return true;
            }
        });
    }

    private void callDetailInterface(Toilette toilette){
        Intent intent = new Intent(this, DetailMapsActivity.class);
        intent.putExtra(DetailMapsActivity.INTENT_MAINTODETAIL_ADRESSE, toilette.getAdresse());
        intent.putExtra(DetailMapsActivity.INTENT_MAINTODETAIL_VILLE, toilette.getVille());
        intent.putExtra(DetailMapsActivity.INTENT_MAINTODETAIL_OBSERVATION, toilette.getObservation());
        startActivity(intent);
    }

    @Override
    public void onToiletteListFinished(ArrayList<Toilette> toilettes) {
        RefreshLayout.setRefreshing(false);
        RefreshListView();
    }

    @Override
    public void onToiletteListFailed(Exception ex) {
        Toast.makeText(this, "Erreur pendant la récupération des toilettes", Toast.LENGTH_LONG).show();
        RefreshLayout.setRefreshing(false);
    }
}
