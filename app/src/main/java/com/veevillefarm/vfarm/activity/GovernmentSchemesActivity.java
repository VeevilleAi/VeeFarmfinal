package com.veevillefarm.vfarm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.adapter.GovernmentSchemesAdapter;
import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.News;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/*
 * show farmer related schemes by governments and any private sectors
 * as of now creating manualy
 * find an api and replace it
 */
public class GovernmentSchemesActivity extends AppCompatActivity {

    private final String TAG = GovernmentSchemesActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_government_schemes);
        setUpToolbar();
        setUpRecyclerview();
    }

    //setup custom toolbar
    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("News");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    //setup news article recyclerview
    private void setUpRecyclerview() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(manager);
        List<News> newsList = getNewsCards();
        GovernmentSchemesAdapter adapter = new GovernmentSchemesAdapter(this, newsList);
        recyclerView.setAdapter(adapter);

    }

    //genarating news cards
    private List<News> getNewsCards() {
        List<News> newsList = new ArrayList<>();
        newsList.add(new News("Integrated Scheme for Agricultural Marketing(ISAM) (effective since 01.04.2014)", "https://previews.123rf.com/images/elwynn/elwynn0908/elwynn090800185/5378333-there-is-a-farmer-working-in-the-farm-.jpg", ""));
        newsList.add(new News("National Agriculture Market (e-NAM)", "https://previews.123rf.com/images/kiankhoon/kiankhoon1103/kiankhoon110300006/9057483-a-farmer-working-at-paddy-field-in-sekinchan-malaysia-.jpg", ""));
        newsList.add(new News("Model Agricultural Produce and Livestock Marketing(Promotion & Facilitation) Act, 2017", "https://smedia2.intoday.in/btmt/images/stories/farmingindia_660_081117025209_031518034058.jpg", ""));
        newsList.add(new News("Formulation of Model Contract Farming (Promotion & Facilitation) Act, 2017", "https://gdb.voanews.com/0BC293BF-48D6-46CA-B5FC-6A6A7750BBAB_w1023_r1_s.jpg", ""));
        return newsList;
    }

    //perform action when optionmenu item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    private void logMessage(String message){
        AppSingletonClass.logMessage(TAG,message);
    }

    private void logErrorMessage(String errorMessage){
        AppSingletonClass.logErrorMessage(TAG,errorMessage);
    }
}
