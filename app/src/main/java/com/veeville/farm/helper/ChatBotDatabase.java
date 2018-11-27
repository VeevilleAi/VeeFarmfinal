package com.veeville.farm.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 17-07-2017.
 */

public class ChatBotDatabase extends SQLiteOpenHelper {

    private static final String LIGHTVALUETABLE = "light_value_table";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "charbotdb";

    private static final String TABLE_CHATBOTDATA = "table_chatbot";
    private static final String DATATYPE = "datatype";
    private static final String CHATDATA = "chatdata";
    private static final String TIMESTAMP = "timestamp";
    private static final String lightValue = "LightValue";
    private final String TAG = ChatBotDatabase.class.getSimpleName();
    private static final String TABLE_VEG_FRUIT_PRICE = "vegetable_fruits_price_table";
    private static final String COLUMN_VEG_FRUIT_NAME = "veg_fruit_name";
    private static final String COLUMN_VEG_FRUIT_PRICE = "veg_fruit_price";
    private static final String COLUMN_FRUIT_IMAGELINK = "fruit_veg_img_link";
    private static final String COLUMN_VEG_FRUIT_KG_PIECE = "veg_fruit_kg_piece";
    private static final String COLUMN_IS_FRUIT = "is_fruit";
    private static final String COLUMN_DATE = "price_updated_date";

    private static final String TABLE_REGISTERED_MOBILE_NUMBER = "registered_mobile_number";
    private static final String COLUMN_MOBILE_NUMBER = "mobile_number";

    public ChatBotDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_TABLE_CHATBOTDATA = "CREATE TABLE " + TABLE_CHATBOTDATA +
                "(" + DATATYPE + " TEXT," +
                CHATDATA + " TEXT," +
                TIMESTAMP + " INTEGER" +
                ")";
        db.execSQL(CREATE_TABLE_TABLE_CHATBOTDATA);
        logMessage("table chatbotdata created");


        String CREATE_TABLE_VEG_FRUIT_PRICE = "CREATE TABLE " + TABLE_VEG_FRUIT_PRICE +
                "(" +
                COLUMN_VEG_FRUIT_NAME + " TEXT," +
                COLUMN_VEG_FRUIT_PRICE + " TEXT," +
                COLUMN_VEG_FRUIT_KG_PIECE + " TEXT," +
                COLUMN_FRUIT_IMAGELINK + " TEXT," +
                COLUMN_IS_FRUIT + " INTEGER," +
                COLUMN_DATE + " INTEGER" +

                ")";
        db.execSQL(CREATE_TABLE_VEG_FRUIT_PRICE);


        String CREATE_TABLE_REG_MOB_NUM = " CREATE TABLE " + TABLE_REGISTERED_MOBILE_NUMBER + "(" +
                COLUMN_MOBILE_NUMBER + " varchar(30)" +
                ")";
        db.execSQL(CREATE_TABLE_REG_MOB_NUM);

        String createTableLIGHTValues = "CREATE TABLE " + LIGHTVALUETABLE + "(" +
                TIMESTAMP + " INTEGER," +
                lightValue + " INTEGER " +
                ");";
        db.execSQL(createTableLIGHTValues);


    }


    public void insertMobileNumber(String mobNumber) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_MOBILE_NUMBER, mobNumber);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_REGISTERED_MOBILE_NUMBER, null, values);
        db.close();

    }

    public void insertLightValues(long timeStamp, long lightValue) {
        ContentValues values = new ContentValues();
        values.put(ChatBotDatabase.lightValue, lightValue);
        values.put(TIMESTAMP, timeStamp);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(LIGHTVALUETABLE, null, values);

    }

    public List<LightValues> getAllLightValues() {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + LIGHTVALUETABLE;
        Cursor cursor = db.rawQuery(query, null);
        List<LightValues> lightValues = new ArrayList<>();
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                long timestamp = cursor.getLong(0);
                long value = cursor.getLong(1);
                LightValues values = new LightValues(timestamp, value);
                lightValues.add(values);
            }
        }
        cursor.close();
        db.close();
        return lightValues;
    }

    public void deleteMobileNumber() {

        String query = "DELETE FROM " + TABLE_REGISTERED_MOBILE_NUMBER;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public boolean isUserRegistered() {
        String query = "SELECT * FROM " + TABLE_REGISTERED_MOBILE_NUMBER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        boolean result = cursor.moveToFirst();
        cursor.close();
        db.close();
        return result;

    }

    public void insertChats(String datatype, String chat_data) {
        long timestamp = System.currentTimeMillis();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATATYPE, datatype);
        values.put(CHATDATA, chat_data);
        values.put(TIMESTAMP, timestamp);
        db.insert(TABLE_CHATBOTDATA, null, values);
        db.close();
        AppSingletonClass.logDebugMessage(TAG, "chat data inserted of type:" + datatype + "data string:" + chat_data);
    }


    private long getDateToTimestamp(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String s = dateFormat.format(calendar.getTime());
        return calendar.getTime().getTime();
    }

    private String getDate(long time) {

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH)) {
            return "Today";
        } else {
            return DateFormat.format("dd-MMM-yyyy", cal).toString();
        }
    }

    public List<Object> fetchAllMessages() {
        List<Object> chatlist = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_CHATBOTDATA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            long timestampForDate = cursor.getLong(2);
            String date = getDate(timestampForDate);
            ChatmessageDataClasses.DateInMessage dateInMessage1 = new ChatmessageDataClasses.DateInMessage(date);
            chatlist.add(dateInMessage1);
            long dateTimestamp = getDateToTimestamp(timestampForDate);
            for (int i = 0; i < cursor.getCount(); i++) {
                String datatype = cursor.getString(0);
                long timestamp = cursor.getLong(2);
                if (timestamp - dateTimestamp > (24 * 60 * 60 * 1000)) {
                    String date1 = getDate(timestamp);
                    ChatmessageDataClasses.DateInMessage dateInMessage = new ChatmessageDataClasses.DateInMessage(date1);
                    chatlist.add(dateInMessage);
                    dateTimestamp = getDateToTimestamp(timestamp);
                }

                switch (datatype) {
                    case "inputtext":
                        ChatmessageDataClasses.InputTextMessage inputData = new ChatmessageDataClasses.InputTextMessage(cursor.getString(1), timestamp);
                        chatlist.add(inputData);
                        break;
                    case "responsetext":
                        ChatmessageDataClasses.ResponseTextMessage responseData = new ChatmessageDataClasses.ResponseTextMessage(cursor.getString(1), timestamp);
                        chatlist.add(responseData);
                        break;

                    case "videolink":
                        String link = cursor.getString(1);
                        String[] videodata = link.split(",");
                        List<String> videoIds = new ArrayList<>();
                        List<String> imageLinks = new ArrayList<>();
                        videoIds.add(videodata[0]);
                        imageLinks.add(videodata[1]);
                        ChatmessageDataClasses.ResponseVideoMessage videoClass = new ChatmessageDataClasses.ResponseVideoMessage(videoIds, imageLinks, timestamp);
                        chatlist.add(videoClass);
                        break;
                    case "quickreply":
                        String[] quickreplyarray = cursor.getString(1).split(",");
                        QuickReplyclass quickReplyclass = new QuickReplyclass(Arrays.asList(quickreplyarray));
                        chatlist.add(quickReplyclass);
                        break;
                    case "inputimage":
                        InputImageClass inputImageClass = new InputImageClass(cursor.getString(1), true, timestamp);
                        chatlist.add(inputImageClass);
                        break;
                    case "inputimagelink":
                        ChatmessageDataClasses.InputImageMessage inputImageMessage = new ChatmessageDataClasses.InputImageMessage(cursor.getString(1), timestamp);
                        chatlist.add(inputImageMessage);
                        break;
                    case "vegfruitpricecard":
                        String[] strings = cursor.getString(1).split(",");
                        ChatmessageDataClasses.VegFruitPrice vegFruitPrice = new ChatmessageDataClasses.VegFruitPrice(strings[0], strings[1], strings[2], timestamp);
                        chatlist.add(vegFruitPrice);
                        break;
                }
                cursor.moveToNext();
            }
        }
        db.close();
        cursor.close();
        return chatlist;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertVegetableAndFruitPrices(List<Fruit> names) {


        SQLiteDatabase db = this.getWritableDatabase();
        for (Fruit name : names) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_VEG_FRUIT_NAME, name.name);
            values.put(COLUMN_FRUIT_IMAGELINK, name.imageLink);
            long result = db.insert(TABLE_VEG_FRUIT_PRICE, null, values);
        }
        db.close();

    }

    public void updatePriceOfFruitAndVeg(List<Fruit> fruitList) {

        SQLiteDatabase db = this.getWritableDatabase();
        for (Fruit fruit : fruitList) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_VEG_FRUIT_PRICE, fruit.price);
            values.put(COLUMN_VEG_FRUIT_KG_PIECE, fruit.pieceOrKg);
            int update = db.update(TABLE_VEG_FRUIT_PRICE, values, COLUMN_VEG_FRUIT_NAME + "='" + fruit.name + "'", null);
        }
        db.close();
    }

    public List<Fruit> getAllPrices() {
        List<Fruit> fruits = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_VEG_FRUIT_PRICE;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                String name = cursor.getString(0);
                String price = cursor.getString(1);
                String size = cursor.getString(2);
                String imageLink = cursor.getString(3);
                boolean isFruit = false;
                int var = cursor.getInt(4);
                if (var == 1) {
                    isFruit = true;
                }
                long date = cursor.getInt(5);
                Fruit fruit = new Fruit(name, price, size, imageLink, isFruit, date);
                fruits.add(fruit);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return fruits;
    }

    public List<Fruit> getPriceOfFruitOrVegetable(String fruitName) {

        List<Fruit> priceFruitList = new ArrayList<>();
        String query = "SELECT " + COLUMN_VEG_FRUIT_NAME + "," + COLUMN_VEG_FRUIT_PRICE + "," + COLUMN_VEG_FRUIT_KG_PIECE + "," + COLUMN_FRUIT_IMAGELINK + " FROM " + TABLE_VEG_FRUIT_PRICE + " WHERE " + COLUMN_VEG_FRUIT_NAME + " LIKE '%" + fruitName + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                String name = cursor.getString(0);
                String price = cursor.getString(1);
                String piece = cursor.getString(2);
                String imgLink = cursor.getString(3);
                Fruit fruit = new Fruit(name, price, piece, imgLink, false, 0);
                priceFruitList.add(fruit);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return priceFruitList;
    }

    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }

    private void logErrorMessage(String logErrorMessage) {
        AppSingletonClass.logErrorMessage(TAG, logErrorMessage);
    }

}
