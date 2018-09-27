package com.veeville.farm.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by user on 17-07-2017.
 */

public class ChatBotDatabase extends SQLiteOpenHelper {

    private String TAG = "ChatBotDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "charbotdb";

    private static final String TABLE_CHATBOTDATA = "table_chatbot";
    private static final String DATATYPE = "datatype";
    private static final String CHATDATA = "chatdata";

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
                CHATDATA + " TEXT" +
                ")";
        db.execSQL(CREATE_TABLE_TABLE_CHATBOTDATA);
        Log.d(TAG, "table chatbotdata created");

        String CREATE_TABLE_VEG_FRUIT_PRICE = "CREATE TABLE " + TABLE_VEG_FRUIT_PRICE +
                "(" +
                COLUMN_VEG_FRUIT_NAME + " TEXT," +
                COLUMN_VEG_FRUIT_PRICE + " TEXT," +
                COLUMN_VEG_FRUIT_KG_PIECE + " TEXT," +
                COLUMN_FRUIT_IMAGELINK + " TEXT," +
                COLUMN_IS_FRUIT + " INTEGER," +
                COLUMN_DATE + " INTEGER" +

                ")";
        Log.d(TAG, "onCreate: TABLE_VEG_FRUIT_PRICE query :" + CREATE_TABLE_VEG_FRUIT_PRICE);
        db.execSQL(CREATE_TABLE_VEG_FRUIT_PRICE);


        String CREATE_TABLE_REG_MOB_NUM = " CREATE TABLE " + TABLE_REGISTERED_MOBILE_NUMBER + "(" +
                COLUMN_MOBILE_NUMBER + " varchar(30)" +
                ")";
        db.execSQL(CREATE_TABLE_REG_MOB_NUM);
        Log.d(TAG, "onCreate: table created :TABLE_REGISTERED_MOBILE_NUMBER");


    }


    public void insertMobileNumber(String mobNumber) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_MOBILE_NUMBER, mobNumber);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_REGISTERED_MOBILE_NUMBER, null, values);
        db.close();

    }

    public void deleteMobileNumber() {

        String query = "DELETE FROM " + TABLE_REGISTERED_MOBILE_NUMBER;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
        Log.d(TAG, "deleteMobileNumber: number deleted successfully");
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

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATATYPE, datatype);
        values.put(CHATDATA, chat_data);
        db.insert(TABLE_CHATBOTDATA, null, values);
        db.close();
        AppSingletonClass.logDebugMessage(TAG, "chat data inserted of type:" + datatype + "data string:" + chat_data);

    }

    public List<Object> fetchAllMessages() {
        List<Object> chatlist = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_CHATBOTDATA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                String datatype = cursor.getString(0);
                switch (datatype) {
                    case "inputtext":
                        ChatmessageDataClasses.InputTextMessage inputData = new ChatmessageDataClasses.InputTextMessage(cursor.getString(1));
                        chatlist.add(inputData);
                        break;
                    case "responsetext":
                        ChatmessageDataClasses.ResponseTextMessage responseData = new ChatmessageDataClasses.ResponseTextMessage(cursor.getString(1));
                        chatlist.add(responseData);
                        break;

                    case "videolink":
                        String link = cursor.getString(1);
                        String[] videodata = link.split(",");
                        List<String> videoIds = new ArrayList<>();
                        List<String> imageLinks = new ArrayList<>();
                        videoIds.add(videodata[0]);
                        imageLinks.add(videodata[1]);
                        ChatmessageDataClasses.ResponseVideoMessage videoClass = new ChatmessageDataClasses.ResponseVideoMessage(videoIds, imageLinks);
                        chatlist.add(videoClass);
                        break;
                    case "quickreply":
                        String[] quickreplyarray = cursor.getString(1).split(",");
                        QuickReplyclass quickReplyclass = new QuickReplyclass(Arrays.asList(quickreplyarray));
                        chatlist.add(quickReplyclass);
                        break;
                    case "inputimage":
                        InputImageClass inputImageClass = new InputImageClass(cursor.getString(1), true);
                        chatlist.add(inputImageClass);
                        break;
                    case "inputimagelink":
                        ChatmessageDataClasses.InputImageMessage inputImageMessage = new ChatmessageDataClasses.InputImageMessage(cursor.getString(1));
                        chatlist.add(inputImageMessage);
                        break;
                    case "weather":
//                            ChatmessageDataClasses.WeatherData weatherData = new ChatmessageDataClasses.WeatherData(cursor.getString(1));
//                            chatlist.add(weatherData);
                        break;
                    case "vegfruitpricecard":
                        String[] strings = cursor.getString(1).split(",");
                        ChatmessageDataClasses.VegFruitPrice vegFruitPrice = new ChatmessageDataClasses.VegFruitPrice(strings[0], strings[1], strings[2]);
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
            Log.d(TAG, "insertVegetableAndFruitPrices: result:" + result);
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
            Log.d(TAG, "updatePriceOfFruitAndVeg: result:" + update);
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
                Log.d(TAG, "getAllPrices: " + var);
                if (var == 1) {
                    isFruit = true;
                }
                long date = cursor.getInt(5);
                Fruit fruit = new Fruit(name, price, size, imageLink, isFruit, date);
                Log.d(TAG, "getAllPrices: " + fruit.toString());
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
}
