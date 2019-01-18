package com.veevillefarm.vfarm.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.adapter.SelectContactsAdapter;
import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.ChatContact;
import com.veevillefarm.vfarm.helper.ChatMessageDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class SelectContactActivity extends AppCompatActivity implements SelectContactsAdapter.SelectedContactsInterface, LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = SelectContactActivity.class.getSimpleName();
    private List<ChatContact> selectedContacts = new ArrayList<>();
    private final int LOCATION_REQUEST = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        setUpToolbar();
        if (checkCameraPermission()) {
            accessPhoneBookContacts();
        } else {
            askLocationPermission();
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Select Contacts");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void setUpRecyclerview(List<ChatContact> contacts) {
        try {
            LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            RecyclerView recyclerView = findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(manager);
            SelectContactsAdapter adapter = new SelectContactsAdapter(getApplicationContext(), contacts, this);
            recyclerView.setAdapter(adapter);
        }catch (Exception e){
            Log.e(TAG, "setUpRecyclerview: "+e.toString() );
        }
    }

    void addContactToDelectedList(ChatContact contact) {
        selectedContacts.add(contact);
        Log.d(TAG, "addContactToDelectedList: " + selectedContacts.size());
    }

    void removeCOntactFromSelectedList(ChatContact contact) {
        ListIterator<ChatContact> iterator = selectedContacts.listIterator();
        try{
        while (iterator.hasNext()) {
            ChatContact temp = iterator.next();
            if (temp.email.equals(contact.email)) {
                selectedContacts.remove(temp);
            }
        }}catch (Exception e){
            Log.e(TAG, "removeCOntactFromSelectedList: "+e.toString() );
        }
        Log.d(TAG, "removeCOntactFromSelectedList: " + selectedContacts.size());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getTitle().toString()) {
            case "Select Contacts":
                onBackPressed();
                break;
            case "select":
                if (selectedContacts.size() > 1) {
                    Intent intent = new Intent(getApplicationContext(), NameChatGroupActivity.class);

                    intent.putParcelableArrayListExtra("SelectedContacts", (ArrayList<? extends Parcelable>) selectedContacts);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "atleast 2 contact should be there to create group", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_contact_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private String getUserAddressAsEmail() {
        ChatMessageDatabase database = new ChatMessageDatabase(Objects.requireNonNull(getApplicationContext()).getApplicationContext());
        return database.getUserEmailAsFromAddress();
    }

    private List<ChatContact> getContacts() {
        String fromAddress = getUserAddressAsEmail();
        ChatMessageDatabase database = new ChatMessageDatabase(Objects.requireNonNull(getApplicationContext()).getApplicationContext());
        List<ChatContact> temp = database.getChatContacts();
        List<ChatContact> chatContacts = new ArrayList<>();
        for (ChatContact contact : temp) {
            if (!contact.email.equals(fromAddress)) {
                chatContacts.add(contact);
            }
        }
        return chatContacts;
    }

    @Override
    public void addCOntactToSelectedList(ChatContact contact) {
        addContactToDelectedList(contact);
        logErrorMessage(contact.fcmToken);
    }

    @Override
    public void removeContactFromSelectedList(ChatContact contact) {
        removeCOntactFromSelectedList(contact);
        logMessage(contact.email);
    }

    //use this method only  to ebug message
    private void logMessage(String message) {
        AppSingletonClass.logMessage(TAG, message);
    }

    private void logErrorMessage(String errorMessage) {
        AppSingletonClass.logErrorMessage(TAG, errorMessage);
    }

    // checking camera permission
    private boolean checkCameraPermission() {

        int permissionReadContact = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS);
        return permissionReadContact == PackageManager.PERMISSION_GRANTED;
    }


    private void accessPhoneBookContacts() {
        getSupportLoaderManager().initLoader(0, null, this);
    }

    //asking camera permission and external storage
    private void askLocationPermission() {
        String[] permissionarray = new String[]{Manifest.permission.READ_CONTACTS};
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
            Toast.makeText(getApplicationContext(), "Enable Contacts access in App Settings", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, permissionarray, LOCATION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    accessPhoneBookContacts();
                else
                    Toast.makeText(this, "please enable permission to create group", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(
                this,
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME +" ASC"
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            Toast.makeText(this, "fetch successfull", Toast.LENGTH_SHORT).show();
            readAndLoadContacts(data);
        } else {
            Toast.makeText(this, "fetch failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }


    private void readAndLoadContacts(final Cursor cur) {
        Log.d(TAG, "readAndLoadContacts main thread : "+Thread.currentThread().getId());
        final List<ChatContact> contacts = new ArrayList<>();
        final ContentResolver cr = getContentResolver();
        if ((cur != null ? cur.getCount() : 0) > 0) {
            new Runnable(){
                @Override
                public void run() {
                    while (cur.moveToNext()) {
                        Log.d(TAG, "run: "+Thread.currentThread().getId());
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            if (pCur != null && pCur.moveToFirst()) {
                                String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                Log.i(TAG, "Name: " + name);
                                Log.i(TAG, "Phone Number: " + phoneNo);
                                ChatContact contact = new ChatContact(name, phoneNo, "https://static.wixstatic.com/media/200fe1_06ce139451b6433d95342587d4542c01.png", "", "");
                                contacts.add(contact);
                                pCur.close();
                            }
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setUpRecyclerview(contacts);
                        }
                    });
                }
            }.run();





        }
    }

}
