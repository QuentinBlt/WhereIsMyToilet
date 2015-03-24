package net.toilet.my.is.where.com.whereismytoilet.ModelView;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.toilet.my.is.where.com.whereismytoilet.R;


public class Detail_ToiletActivity extends ActionBarActivity {

    Toolbar detailToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_toilet);
        setWidget();
    }

    private void setWidget(){
        detailToolBar = (Toolbar)findViewById(R.id.activity_detailtoilet_toolbar);
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

}
