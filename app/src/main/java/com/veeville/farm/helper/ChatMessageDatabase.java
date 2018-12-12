package com.veeville.farm.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant C on 10/12/18.
 */
public class ChatMessageDatabase extends SQLiteOpenHelper{

    private static final String TAG = ChatMessageDatabase.class.getSimpleName();
    private static final String CHAT_MESSAGES_DATABASE = "chat_messages_database";
    private static final int DATABSE_VERSION = 1;
    private static final String TABLE_CHAT_MESSAGES = "table_chat_message";
    private static final String COLUMN_MESSAGE_ID = "message_id";
    private static final String COLUMN_CHAT_FROM = "chat_from";
    private static final String COLUMN_CHAT_TO = "chat_to";
    private static final String COLUMN_CHAT_MESSAGE = "chat_message";
    private static final String COLUMN_CHAT_TIME = "chat_time";


    private static final String TABLE_USER_CRDENTIALS = "USER_CREDENTIALS";
    private static final String COLUMN_USEREMAIL = "user_email";

    private static final String TABLE_CONTACTS = "table_contacts";
    private static final String COLUMN_CONTACT_NAME = "contact_name";
    private static final String COLUMN_CONTACT_EMAIL = "contact_email";
    private static final String COLUMN_PROFILE_PIC_URL=  "profile_pic";


    public ChatMessageDatabase(Context context) {
        super(context, CHAT_MESSAGES_DATABASE, null, DATABSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CHAT_MESSAGE_TABLE = "CREATE TABLE "+TABLE_CHAT_MESSAGES + "("+
                COLUMN_MESSAGE_ID + " INTEGER AUTO INCREMENT,"+
                COLUMN_CHAT_FROM + " TEXT,"+
                COLUMN_CHAT_TO + " TEXT,"+
                COLUMN_CHAT_MESSAGE + " TEXT,"+
                COLUMN_CHAT_TIME + " INTEGER"+
                ")";
        Log.d(TAG, "query: "+CREATE_CHAT_MESSAGE_TABLE);
        db.execSQL(CREATE_CHAT_MESSAGE_TABLE);
        String CREATE_TABLE_USER_CREDENTIALS = "CREATE TABLE "+TABLE_USER_CRDENTIALS + "("+
                COLUMN_USEREMAIL + " TEXT UNIQUE"+
                ")";
        db.execSQL(CREATE_TABLE_USER_CREDENTIALS);

        String CREATE_CONTACT_TABLE = "CREATE TABLE "+TABLE_CONTACTS + "("+
                COLUMN_CONTACT_NAME +" TEXT,"+
                COLUMN_CONTACT_EMAIL + " TEXT UNIQUE,"+
                COLUMN_PROFILE_PIC_URL + " TEXT"+
                ")";
        db.execSQL(CREATE_CONTACT_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public String getUserEmailAsFromAddress(){
        String query = "SELECT "+COLUMN_USEREMAIL + " FROM "+TABLE_USER_CRDENTIALS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        String email = "";
        if(cursor.moveToFirst()){
            email = cursor.getString(0);
        }
        cursor.close();
        return email;
    }

    public void insertuserCredentials(String email){
        ContentValues values = new ContentValues();
        values.put(COLUMN_USEREMAIL,email);
        SQLiteDatabase  db = this.getWritableDatabase();
        db.insert(TABLE_USER_CRDENTIALS,null,values);
        db.close();
    }


    public void insertContacts(List<ChatContact> chatContacts){

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (ChatContact contact : chatContacts) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_CONTACT_NAME, contact.name);
                values.put(COLUMN_CONTACT_EMAIL, contact.email);
                values.put(COLUMN_PROFILE_PIC_URL, contact.picUrl);
                db.insert(TABLE_CONTACTS, null, values);
                Log.d(TAG, "insertContacts: success");
            }
        }catch (Exception e){
            Log.e(TAG, "insertContacts: "+e.toString() );
        }
        db.close();
    }

    public List<ChatContact> getChatContacts(){

        List<ChatContact> chatContacts = new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_CONTACTS;
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        String userEmail = getUserEmailAsFromAddress();
        if(cursor.moveToFirst()){

            for (int i = 0; i < cursor.getCount(); i++) {
                String name = cursor.getString(0);
                String email = cursor.getString(1);
                String picUrl = cursor.getString(2);
                Log.d(TAG, "getChatContacts: "+userEmail+email);
                if(!userEmail.equals(email)) {
                    ChatContact contact = new ChatContact(name, email, picUrl,getMostRecentMessage(email));
                    chatContacts.add(contact);
                }
                cursor.moveToNext();
            }

        }
        cursor.close();
        db.close();
        return chatContacts;
    }
    public void insertChatMessage(ChatMessage chatMessage){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHAT_FROM,chatMessage.from);
        values.put(COLUMN_CHAT_TO,chatMessage.to);
        values.put(COLUMN_CHAT_MESSAGE,chatMessage.message);
        values.put(COLUMN_CHAT_TIME,chatMessage.timestamp);
        db.insert(TABLE_CHAT_MESSAGES,null,values);
        Log.d(TAG, "insertChatMessage: message successfully inserted "+chatMessage.message );
    }

    public List<ChatMessage> getChatMessages(String from,String to){
        List<ChatMessage> chatMessages = new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_CHAT_MESSAGES + " WHERE "+COLUMN_CHAT_FROM +"='"+from+"' AND "+COLUMN_CHAT_TO+"='"+to+"' OR "+COLUMN_CHAT_FROM +"='"+to+"' AND "+COLUMN_CHAT_TO+"='"+from+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){

            for (int i = 0; i <cursor.getCount() ; i++) {
                String messageId = cursor.getString(0);
                String fromAddress = cursor.getString(1);
                String toAddress = cursor.getString(2);
                String message = cursor.getString(3);
                long timestamp = cursor.getLong(4);
                ChatMessage chatMessage = new ChatMessage(messageId, fromAddress, toAddress, message, timestamp);
                chatMessages.add(chatMessage);
                cursor.moveToNext();
            }

        }
        return chatMessages;

    }

    public String getMostRecentMessage(String from){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+COLUMN_CHAT_MESSAGE + " FROM "+TABLE_CHAT_MESSAGES +" WHERE "+COLUMN_CHAT_FROM +"='"+from+"' OR "+ COLUMN_CHAT_TO +"='"+from+"' ORDER BY "+COLUMN_CHAT_TIME +" DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query,null);
        String message = "";
        if(cursor.moveToFirst()){
             message = cursor.getString(0);
        }

        return message;
    }


    public long getMostupdatedTime(){

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+COLUMN_CHAT_TIME + " FROM "+TABLE_CHAT_MESSAGES+" ORDER BY "+COLUMN_CHAT_TIME +" DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query,null);
        long timestamp ;
        if(cursor.moveToFirst()){
            timestamp = cursor.getLong(0);
        }else {
            timestamp =  -1;
        }
        cursor.close();
        db.close();
        Log.d(TAG, "getMostupdatedTime: "+timestamp);
        return timestamp;
    }

}
