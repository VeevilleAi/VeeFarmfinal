package com.veevillefarm.vfarm.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant C on 10/12/18.
 * chat message like images texts audios and videos
 * used to store above media in to local Sqlite database
 */
public class ChatMessageDatabase extends SQLiteOpenHelper{

    private static final String TAG = ChatMessageDatabase.class.getSimpleName();
    private static final String CHAT_MESSAGES_DATABASE = "chat_messages_database";
    private static final int DATABSE_VERSION = 1;
    private static final String TABLE_CHAT_MESSAGES = "table_chat_message";
    private static final String COLUMN_MESSAGE_ID = "message_id";
    private static final String COLUMN_CHAT_FROM = "chat_from";
    private static final String COLUMN_CHAT_TO = "chat_to";
    private static final String COLUMN_CHAT_MESSAGE_TYPE = "message_type";
    private static final String COLUMN_CHAT_MESSAGE_ID = "chat_message_id";
    private static final String COLUMN_CHAT_TIME = "chat_time";



    private static final String TABLE_USER_CRDENTIALS = "USER_CREDENTIALS";
    private static final String COLUMN_USEREMAIL = "user_email";
    private static final String COLUMN_FCM_TOKEN = "fcm_token";


    private static final String TABLE_CONTACTS = "table_contacts";
    private static final String COLUMN_CONTACT_NAME = "contact_name";
    private static final String COLUMN_CONTACT_EMAIL = "contact_email";
    private static final String COLUMN_PROFILE_PIC_URL=  "profile_pic";
    private static final String COLUMN_USER_FCM_TOKEN=  "user_fcm_token";

    private static final String TABLE_CHAT_TEXT_MESSAGE = "text_message_table";
    private static final String COLUMN_TEXT_MESSAGE_ID = "text_message_id";
    private static final String COLUMN_TEXT_MESSAGE_MESSAGE = "text_message";


    private static final String TABLE_CHAT_IMAGE_MESSAGE = "image_message_table";
    private static final String COLUMN_IMAGE_MESSAGE_ID = "image_message_id";
    private static final String COLUMN_IMAGE_MESSAGE_AWS_STORAGE_ID = "aws_message_object_id";
    private static final String COLUMN_IMAGE_MESSAGE_THUMBNAIL = "image_message_thumbnail";
    private static final String COLUMN_IMAGE_DOWNLOADED = "is_image_downloaded";
    private static final String COLUMN_IMAGE_DOWNLOADED_PATH = "image_downloaded_path";

    private static final String TABLE_CHAT_GROUP = "chat_group_table";
    private static final String COLUMN_CHAT_GROUP_ID = "chat_group_id";
    private static final String COLUMN_CHAT_GROUP_NAME = "chat_group_name";

    private static final String TABLE_CHAT_GROUP_CONTACTS = "chat_group_contacts";
    private static final String COLUMN_CHAT_GROUP_CONTACT_ID = "chat_group_id";
    private static final String COLUMN_CHAT_GROUP_CONTACT_NAME = "chat_group_contct_name";
    private static final String COLUMN_CHAT_GROUP_CONTACT_NUMBER = "chat_group_contact_number";

//    private static final String TABLE_CHAT_AUDIO_MESSAGE = "audio_message_table";
//    private static final String COLUMN_AUDIO_MESSAGE_ID = "audio_message_id";
//    private static final String COLUMN_AUDIO_MESSAGE = "audio_message";


    public ChatMessageDatabase(Context context) {
        super(context, CHAT_MESSAGES_DATABASE, null, DATABSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_CHAT_MESSAGE_TABLE = "CREATE TABLE "+TABLE_CHAT_MESSAGES + "("+
                COLUMN_MESSAGE_ID + " INTEGER,"+
                COLUMN_CHAT_MESSAGE_TYPE + " TEXT,"+
                COLUMN_CHAT_FROM + " TEXT,"+
                COLUMN_CHAT_TO + " TEXT,"+
                COLUMN_CHAT_MESSAGE_ID + " INTEGER,"+
                COLUMN_CHAT_TIME + " INTEGER"+
                ")";
        logMessage("query: "+CREATE_CHAT_MESSAGE_TABLE);
        db.execSQL(CREATE_CHAT_MESSAGE_TABLE);


        String CREATE_TABLE_USER_CREDENTIALS = "CREATE TABLE "+TABLE_USER_CRDENTIALS + "("+
                COLUMN_USEREMAIL + " TEXT UNIQUE,"+
                COLUMN_FCM_TOKEN + " TEXT"+
                ")";
        db.execSQL(CREATE_TABLE_USER_CREDENTIALS);


        String CREATE_TABLE_CHAT_TEXT_MESSAGES = "CREATE TABLE "+TABLE_CHAT_TEXT_MESSAGE + "("+
                COLUMN_TEXT_MESSAGE_ID +" INTEGER,"+
                COLUMN_TEXT_MESSAGE_MESSAGE + " TEXT"+
                ")";
        db.execSQL(CREATE_TABLE_CHAT_TEXT_MESSAGES);



        String CREATE_TABLE_CHAT_IMAGE_MESSAGES = "CREATE TABLE "+TABLE_CHAT_IMAGE_MESSAGE + "("+
                COLUMN_IMAGE_MESSAGE_ID +" INTEGER,"+
                COLUMN_IMAGE_MESSAGE_AWS_STORAGE_ID + " TEXT,"+
                COLUMN_IMAGE_MESSAGE_THUMBNAIL + " BLOB,"+
                COLUMN_IMAGE_DOWNLOADED + " BOOLEAN,"+
                COLUMN_IMAGE_DOWNLOADED_PATH + " TEXT"+
                ")";
        db.execSQL(CREATE_TABLE_CHAT_IMAGE_MESSAGES);


        String CREATE_CONTACT_TABLE = "CREATE TABLE "+TABLE_CONTACTS + "("+
                COLUMN_CONTACT_NAME +" TEXT,"+
                COLUMN_CONTACT_EMAIL + " TEXT UNIQUE,"+
                COLUMN_PROFILE_PIC_URL + " TEXT,"+
                COLUMN_USER_FCM_TOKEN + " TEXT"+
                ")";
        db.execSQL(CREATE_CONTACT_TABLE);

        String CREATE_TABLE_CHAT_GROUP = "CREATE TABLE "+TABLE_CHAT_GROUP + "("+
                COLUMN_CHAT_GROUP_ID + " TEXT,"+
                COLUMN_CHAT_GROUP_NAME + " TEXT"+
                ")";
        db.execSQL(CREATE_TABLE_CHAT_GROUP);

        String CREATE_CHAT_GROUP_CONTACTS = "CREATE TABLE "+TABLE_CHAT_GROUP_CONTACTS + "("+
                COLUMN_CHAT_GROUP_CONTACT_ID + " TEXT,"+
                COLUMN_CHAT_GROUP_CONTACT_NAME + " TEXT,"+
                COLUMN_CHAT_GROUP_CONTACT_NUMBER + " TEXT"+
                ")";
        db.execSQL(CREATE_CHAT_GROUP_CONTACTS);
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
                values.put(COLUMN_USER_FCM_TOKEN,contact.fcmToken);
                db.insert(TABLE_CONTACTS, null, values);
                logMessage( "insertContacts: success");
            }
        }catch (Exception e){
            logErrorMessage("insertContacts: "+e.toString() );
        }
        db.close();
    }

    public List<ChatContact> getChatContacts(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS,null,null,null,null,null,null);
        List<ChatContact> contacts = new ArrayList<>();
        if(cursor.moveToFirst()){
            String name,email,pic,fcmToken;
            for (int i = 0; i < cursor.getCount(); i++) {
                name = cursor.getString(0);
                email = cursor.getString(1);
                pic = cursor.getString(2);
                fcmToken = cursor.getString(3);
                ChatContact contact = new ChatContact(name,email,pic,"",fcmToken);
                contacts.add(contact);
                cursor.moveToNext();
            }
        }
        db.close();
        cursor.close();
        return contacts;
    }

    private String getMostRecentmesage(String from){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CHAT_MESSAGES,new String[]{COLUMN_CHAT_MESSAGE_ID},COLUMN_CHAT_FROM + "=? OR "+COLUMN_CHAT_TO +"=?",new String[]{from,from},null,null,COLUMN_CHAT_MESSAGE_ID +" DESC");
        String recentMessage = "";
        if(cursor.moveToFirst()){
            recentMessage = cursor.getString(0);
        }
        cursor.close();
        return recentMessage;

    }
    public void insertChatTextMessage(long messageId,String chatMessage){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TEXT_MESSAGE_ID,messageId);
        contentValues.put(COLUMN_TEXT_MESSAGE_MESSAGE,chatMessage);
        db.insert(TABLE_CHAT_TEXT_MESSAGE,null,contentValues);
        logMessage("inserted mesage : "+chatMessage + " into "+TABLE_CHAT_TEXT_MESSAGE);
    }

    public void insertChatMessage(ChatMessage chatMessage){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MESSAGE_ID,chatMessage.messageId);
        values.put(COLUMN_CHAT_MESSAGE_TYPE,chatMessage.messageType);
        values.put(COLUMN_CHAT_FROM,chatMessage.from);
        values.put(COLUMN_CHAT_TO,chatMessage.to);
        values.put(COLUMN_CHAT_MESSAGE_ID,chatMessage.messageValueId);
        values.put(COLUMN_CHAT_TIME,chatMessage.timestamp);
        long result = db.insert(TABLE_CHAT_MESSAGES,null,values);
        logMessage("insertChatMessage: success:"+result);
    }

    public void updateNewFileDownloedpath(long imgId,String path){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_DOWNLOADED_PATH,path);
        values.put(COLUMN_IMAGE_DOWNLOADED,true);
        int isUpdated = db.update(TABLE_CHAT_IMAGE_MESSAGE,values,COLUMN_IMAGE_MESSAGE_ID+"="+imgId,null);
        logMessage("updateNewFileDownloedpath:"+isUpdated);
    }

    public List<Object> getChatMessages(String from,String to){
        logMessage("getChatMessages: from:"+from+"\tto:"+to);
        List<Object> chatMessages = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CHAT_MESSAGES,null,COLUMN_CHAT_FROM+"=? AND "+COLUMN_CHAT_TO+"=? OR "+COLUMN_CHAT_TO+"=? AND "+COLUMN_CHAT_FROM+"=?",new String[]{from,to,from,to},null,null,null);
        if(cursor.moveToFirst()){
            logMessage("getChatMessages: has messages");
            for (int i = 0; i <cursor.getCount() ; i++) {
                long chatMessageId = cursor.getLong(0);
                String messageType = cursor.getString(1);
                String fromAddress = cursor.getString(2);
                String toAddress= cursor.getString(3);
                logMessage("getChatMessages: from:"+fromAddress+"\tto:"+toAddress);
                long chatmessageValueId=  cursor.getLong(4);
                long timeStamp = cursor.getLong(5);
                String messageValueOrUri = null;
                switch (messageType){
                    case ChatMediaType.TYPE_TEXT:
                        logMessage("getChatMessages:"+messageType);
                        messageValueOrUri = getChatTextMessage(chatmessageValueId);
                        if(from.equals(fromAddress)) {
                            ChatmessageDataClasses.InputTextMessage  inputTextMessage = new ChatmessageDataClasses.InputTextMessage(messageValueOrUri,timeStamp);
                            chatMessages.add(inputTextMessage);
                        }else {
                            ChatmessageDataClasses.ResponseTextMessage responseTextMessage = new ChatmessageDataClasses.ResponseTextMessage(messageValueOrUri,timeStamp);
                            chatMessages.add(responseTextMessage);
                        }

                        break;
                    case ChatMediaType.TYPE_IMAGE:
                        logMessage("getChatMessages:"+messageType);
                        if(from.equals(fromAddress)){
                            ChatmessageDataClasses.ResponseBitMapImage data = getImageFromDb(chatmessageValueId);
                            ChatmessageDataClasses.InputBitMapImage inputBitMapImage = new ChatmessageDataClasses.InputBitMapImage(data.imgId,data.awsImageId,data.isDownloaded,data.filePath,null);
                            chatMessages.add(inputBitMapImage);
                        }else {
                            ChatmessageDataClasses.ResponseBitMapImage responseBitMapImage = getImageFromDb(chatmessageValueId);
                            chatMessages.add(responseBitMapImage);
                        }

                        break;
                    case ChatMediaType.TYPE_AUDIO:
                        break;
                    case ChatMediaType.TYPE_VIDEO:
                        break;
                }
                cursor.moveToNext();
            }
        }else {
            logMessage("getChatMessages: dont have messages to get");
        }
        cursor.close();
        return chatMessages;
    }


    private ChatmessageDataClasses.ResponseBitMapImage getImageFromDb(long imageMessageId){
        SQLiteDatabase db =this.getReadableDatabase();
        String[] params = new String[]{COLUMN_IMAGE_MESSAGE_ID,COLUMN_IMAGE_MESSAGE_AWS_STORAGE_ID,COLUMN_IMAGE_MESSAGE_THUMBNAIL,COLUMN_IMAGE_DOWNLOADED,COLUMN_IMAGE_DOWNLOADED_PATH};
        Cursor cursor = db.query(TABLE_CHAT_IMAGE_MESSAGE,params,COLUMN_IMAGE_MESSAGE_ID+"=?",new String[]{String.valueOf(imageMessageId)},null,null,null);
        if(cursor.moveToFirst()){
            long imgId = cursor.getLong(0);
            String awsStorageId = cursor.getString(1);
            byte[] thumbNailByte = cursor.getBlob(2);
            boolean isDownloaded = cursor.getInt(3) > 0;
            String filePath = cursor.getString(4);
            Bitmap thumbNail = null;
            if(awsStorageId!=null)
                thumbNail = BitmapFactory.decodeByteArray(thumbNailByte, 0, thumbNailByte.length);

            Log.d(TAG, "getImageFromDb: "+imgId + " \t "+awsStorageId+"\tthumbnail length:"+"\tdownloaded:"+isDownloaded+"\tpath:"+filePath);
            ChatmessageDataClasses.ResponseBitMapImage responseBitMapImage =  new ChatmessageDataClasses.ResponseBitMapImage(imgId,awsStorageId,thumbNail,isDownloaded,filePath);
            cursor.close();
            return responseBitMapImage;
        }
        cursor.close();
        return null;

    }


    private byte[] convertBitMapToByteArray(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void insertInputImage(ChatmessageDataClasses.InputBitMapImage data){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_MESSAGE_ID,data.imgId);
        values.put(COLUMN_IMAGE_MESSAGE_AWS_STORAGE_ID,data.awsImageId);
        if(data.awsImageId!=null)
        values.put(COLUMN_IMAGE_MESSAGE_THUMBNAIL,data.byteImge);
        values.put(COLUMN_IMAGE_DOWNLOADED,data.isDownloaded);
        values.put(COLUMN_IMAGE_DOWNLOADED_PATH,data.filePath);
        db.insert(TABLE_CHAT_IMAGE_MESSAGE,null,values);
        logMessage("successfully inserted image to database");
    }
    private String getChatTextMessage(long messageid){
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CHAT_TEXT_MESSAGE,new String[]{COLUMN_TEXT_MESSAGE_MESSAGE},COLUMN_TEXT_MESSAGE_ID+"=?",new String[]{String.valueOf(messageid)},null,null,null);
        String message ="";
        if(cursor.moveToFirst()){
            message = cursor.getString(0);
        }

        logMessage("getChatTextMessage: "+message);
        cursor.close();
        return message;
    }
    public long getMostupdatedTime(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CHAT_MESSAGES,new String[]{COLUMN_CHAT_TIME},null,null,null,null,COLUMN_CHAT_TIME + " DESC");
        long timeStamp = 0;
        if(cursor.moveToFirst()){
            timeStamp = cursor.getLong(0);
        }
        logMessage("getMostupdatedTime: "+timeStamp);
        cursor.close();
        return timeStamp;
    }

    private byte[]  getImageAsBlob(long imageMessageId){
        SQLiteDatabase db =this.getReadableDatabase();
        byte[] blob = null;
        Cursor cursor = db.query(TABLE_CHAT_IMAGE_MESSAGE,new String[]{COLUMN_IMAGE_MESSAGE_THUMBNAIL},COLUMN_IMAGE_MESSAGE_ID+"=?",new String[]{String.valueOf(imageMessageId)},null,null,null);
        if(cursor.moveToFirst()){
            blob = cursor.getBlob(0);
        }
        cursor.close();
        return blob;
    }


    public void insertNewChatGroup(String groupId,String groupName,List<ChatContact> contacts){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHAT_GROUP_ID,groupId);
        values.put(COLUMN_CHAT_GROUP_NAME,groupName);
        db.insert(TABLE_CHAT_GROUP,null,values);
        for (ChatContact contact: contacts) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_CHAT_GROUP_CONTACT_ID, groupId);
            contentValues.put(COLUMN_CHAT_GROUP_CONTACT_NAME,contact.name);
            contentValues.put(COLUMN_CHAT_GROUP_CONTACT_NUMBER,contact.email);
            db.insert(TABLE_CHAT_GROUP_CONTACTS,null,contentValues);
        }

        Log.d(TAG, "insertNewChatGroup: success");
    }

    public List<ChatGroup> getAllChatGroups(){

        List<ChatGroup> chatGroups = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CHAT_GROUP,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                String groupId = cursor.getString(0);
                String groupName = cursor.getString(1);
                Cursor cursor1 = db.query(TABLE_CHAT_GROUP_CONTACTS,new String[]{COLUMN_CHAT_GROUP_CONTACT_NAME,COLUMN_CHAT_GROUP_CONTACT_NUMBER},COLUMN_CHAT_GROUP_CONTACT_ID +"=?",new String[]{groupId},null,null,null);
                List<ChatContact> contacts = new ArrayList<>();
                if(cursor1.moveToFirst()){
                    do{
                        String name = cursor1.getString(0);
                        String mobNumber = cursor1.getString(1);
                        ChatContact contact = new ChatContact(name,mobNumber,"picUrl","recentMesage","fcmToken");
                        contacts.add(contact);
                    }while (cursor1.moveToNext());
                    ChatGroup group = new ChatGroup(groupName,"",contacts);
                    chatGroups.add(group);

                }
                cursor1.close();

            }while (cursor.moveToNext());
        }
        cursor.close();
        return chatGroups;

    }




    //use this function everywhere for logging debug mesage
    private void logMessage(String message){
        //AppSingletonClass.logMessage(TAG,message);
    }

    private void logErrorMessage(String errorMessage){
        AppSingletonClass.logErrorMessage(TAG,errorMessage);
    }
}
