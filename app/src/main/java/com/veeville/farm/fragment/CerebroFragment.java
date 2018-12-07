package com.veeville.farm.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.veeville.farm.R;
import com.veeville.farm.helper.AppSingletonClass;


public class CerebroFragment extends Fragment {


    private final String TAG = CerebroFragment.class.getSimpleName();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        logMesage("onCreateView");
        return inflater.inflate(R.layout.fragment_cerebro, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.languagemenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        logMesage("onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        logMesage("onStart");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        logMesage("onActivityCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        logMesage("onResmue");
    }

    @Override
    public void onPause() {
        super.onPause();
        logMesage("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        logMesage("onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logMesage("onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        logMesage("onDestroyView");
        logErrormeesage("error - test");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        logMesage("onDetach");
        logErrormeesage("hi");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        logMesage("onAttach");
    }

    private void logMesage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }

    private void logErrormeesage(String errorMessage) {
        AppSingletonClass.logErrorMessage(TAG, errorMessage);
    }
}
