package com.veevillefarm.vfarm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.adapter.WorkFlowAdapter;
import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.DashBoardDataClasses;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/*
    contain list of works for farmers
    1 - finished
    2 - pending
    3 - upcoming
    with setting alaram for each category
 */

public class CropWorkFlow extends AppCompatActivity {

    private final String TAG = CropWorkFlow.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_work_flow);
        setUpToolbar();
        setUpRecyclerview();
    }

    //setup custom toolbar
    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("WorkFlow");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        AppSingletonClass.logMessage(TAG, " toolbar setup done");
    }

    //setup recyclerview for workflow
    private void setUpRecyclerview() {
        RecyclerView workFlowRecyclerview = findViewById(R.id.workflow_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        workFlowRecyclerview.setLayoutManager(manager);
        WorkFlowAdapter adapter = new WorkFlowAdapter(formDataWorkFlow(), CropWorkFlow.this);
        workFlowRecyclerview.setAdapter(adapter);
        logMessage("workflow  recyclerview setup done");
    }

    @Override
    protected void onStart() {
        super.onStart();
        logMessage("onStart called");
    }

    //start syncing works from server
    @Override
    protected void onResume() {
        super.onResume();
        logMessage("onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logMessage("onPause called");
    }


    @Override
    protected void onStop() {
        super.onStop();
        logMessage("onStop called");
    }


    //stop sync works from Server
    @Override
    protected void onDestroy() {
        super.onDestroy();
        logMessage("onDestroy called");
    }


    //when option menu item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    //getting list of works like pending , upcoming and finished.
    // presently workflows genarating manually, later it should come from server
    private List<DashBoardDataClasses.WorkFlowData> formDataWorkFlow() {

        List<DashBoardDataClasses.WorkFlowData> workFlowDataList = new ArrayList<>();
        String title, date;
        List<String> subtitle = new ArrayList<>();
        List<String> description = new ArrayList<>();
        title = "Completed";

        date = getDate(-1);
        subtitle.add("Check water level");
        subtitle.add("Take Light Measurement");
        subtitle.add("Check soil moisture");

        description.add("Tell my son to check water level of tank");
        description.add("Take light meter and go to middle of the field and measure the light");
        description.add("Take soil moisture in 4 different corner of field");

        DashBoardDataClasses.WorkFlowData workFlowData = new DashBoardDataClasses.WorkFlowData(title, date, subtitle, description);
        workFlowDataList.add(workFlowData);
        String title1, date1;
        List<String> subtitle1 = new ArrayList<>();
        List<String> description1 = new ArrayList<>();
        title1 = "Scheduled";
        date1 = "Today";

        subtitle1.add("Check soil moisture");
        subtitle1.add("Check for weeds");
        subtitle1.add("Check water level");

        description1.add("Take soil moisture in 4 different corner of field");
        description1.add("Go to all corners and center of field check for weeds");
        description1.add("Tell my son to check water level of tank");

        DashBoardDataClasses.WorkFlowData workFlowData1 = new DashBoardDataClasses.WorkFlowData(title1, date1, subtitle1, description1);
        workFlowDataList.add(workFlowData1);
        String title2, date2;
        List<String> subtitle2 = new ArrayList<>();
        List<String> description2 = new ArrayList<>();
        title2 = "Upcoming";
        date2 = getDate(1);

        subtitle2.add("Check for weeds");
        subtitle2.add("Check water level");
        subtitle2.add("Check for  Soil Moisture");
        subtitle2.add("Take Light Measurement");

        description2.add("Go to all corners and center of field check for weeds");
        description2.add("Go and turn on Pump if Water level less than half of the tank");
        description2.add("Take the Soil Moisture measurement unit and check in all corners and center of the field");
        description2.add("Take light meter and go to middle of the field and measure the light");
        DashBoardDataClasses.WorkFlowData workFlowData2 = new DashBoardDataClasses.WorkFlowData(title2, date2, subtitle2, description2);
        workFlowDataList.add(workFlowData2);
        logErrorMessage("got all the messages ok bull bull");
        updateWorkFlowFromBeginning();
        return workFlowDataList;

    }

    private void updateWorkFlowFromBeginning(){
        logMessage("i lost hope in life ..");
        logErrorMessage("i know this is error message ok got it from all of you and i am aware of these fallowing things ");
        logMessage("i know i dont want to loose hope in life that is why i am here and exists from long time ok got it from above all the things ok how are you i am here from long time");
    }

    //getting date in dd MMM yyyy format by adding  no of days to current date
    private String getDate(int days) {
        String format = "dd MMM yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return dateFormat.format(calendar.getTime());
    }

    //use this function everywhere for logging debug mesage
    private void logMessage(String message){
        AppSingletonClass.logMessage(TAG,message);
    }

    private void logErrorMessage(String errorMessage){
        AppSingletonClass.logErrorMessage(TAG,errorMessage);
    }
}
