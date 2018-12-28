package com.veeville.farm.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant C on 03/09/18.
 * this  class used to store fruits names which will help in further
 */
public class FruitNames {

    //private static String APPLE_FUJI_PINK = "Apple - Fuji(Pink)";
    private static String APPLE_SIMLA = "Apple - Shimla";
    private static String BANANA_ELACHI = "Banana - Elachi";
    private static String BANANA_HILLS = "Banana - Hills";
    private static String BANANA_KARPOORA = "Banana - Karpoora / Honey";
    private static String BANANA_MORIS = "Banana - Morris";
    private static String BANANA_NEINTHRAM = "Banana - Neinthram";
    private static String BANANA_POOVAM = "Banana - Poovam";
    private static String BLACK_GRAPES = "Black Grapes";
    private static String GAUVA = "Guava";
    private static String CANTALOUPE = "Cantaloupe";
    private static String MANGO = "Mango";
    private static String MOSAMBI = "Mosumbi";
    private static String ORANGE = "Orange";
    private static String PAPAYA = "Papaya";
    private static String PINEAPPLE = "Pineapple";
    private static String SAPOTA = "Sapota";
    private static String WATER_MELON = "Watermelon";
    private static String JACK_FRUIT = "Jack Fruit";
    private static String POMEGRANATE_KABUl = "Pomegranate - Kabul";
    private static String SEV_VAZHAI = "Sev-vazhai";

    public static List<Fruit> getFruitNames() {
        List<String> names = new ArrayList<>();
        names.add(APPLE_SIMLA);
        names.add(BANANA_ELACHI);
        names.add(BANANA_HILLS);
        names.add(BANANA_MORIS);
        names.add(BANANA_NEINTHRAM);
        names.add(BANANA_POOVAM);
        names.add(BLACK_GRAPES);
        names.add(GAUVA);
        names.add(CANTALOUPE);
        names.add(MANGO);
        names.add(MOSAMBI);
        names.add(ORANGE);
        names.add(PAPAYA);
        names.add(PINEAPPLE);
        names.add(SAPOTA);
        names.add(WATER_MELON);
        names.add(JACK_FRUIT);
        names.add(POMEGRANATE_KABUl);
        names.add(SEV_VAZHAI);
        List<Fruit> fruits = new ArrayList<>();

        List<String> imageLink = new ArrayList<>();
        imageLink.add("http://5.imimg.com/data5/DD/DD/GLADMIN-/image-cache-catalog-fruits-buy-20shimla-20apple-20online-20kolkata-20-20-20kolkart-com-20-2-500x500-500x500.jpg");//APPLE_SIMLA
        imageLink.add("http://d1z88p83zuviay.cloudfront.net/ProductVariantThumbnailImages/54d367b3-614b-4208-bfdb-b9a4e52cdeb9_425x425.JPG");//BANANA_ELACHI
        imageLink.add("http://www.chennaionlinegrocery.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/b/a/banana_hills_1kg.jpg");//BANANA_HILLS
        imageLink.add("https://3.imimg.com/data3/KI/HW/MY-5306761/banana-500x500.png");//BANANA_MORIS
        imageLink.add("https://4.imimg.com/data4/VO/VG/MY-31563997/fresh-nendran-banana-250x250.jpg");//BANANA_NEINTHRAM
        imageLink.add("https://5.imimg.com/data5/JW/IM/MY-46925736/poovan-banana-500x500.jpg");//BANANA_POOVAM
        imageLink.add("https://5.imimg.com/data5/PX/MO/MY-46065548/black-grapes-500x500.jpg");//BLACK_GRAPES
        imageLink.add("https://5.imimg.com/data5/RA/LA/MY-46372253/guava-2fperu-500x500.png");//GAUVA
        imageLink.add("https://images-na.ssl-images-amazon.com/images/I/91WUOCimq0L._SY355_.jpg");//CANTALOUPE
        imageLink.add("https://www.fruttaweb.com/8971-large_default/mango-fresh-ready-to-eat.jpg");//MANGO
        imageLink.add("https://5.imimg.com/data5/NS/GA/MY-52753357/sweet-lime-500x500.jpg");//MOSAMBI
        imageLink.add("http://soappotions.com/wp-content/uploads/2017/10/orange.jpg");//ORANGE
        imageLink.add("https://www.fruttaweb.com/8371-large_default/fresh-papaya-ready-to-eat.jpg");//PAPAYA
        imageLink.add("https://i5.walmartimages.ca/images/Large/083/646/6000198083646.jpg");//PINEAPPLE
        imageLink.add("https://cdn.awesomecuisine.com/wp-content/uploads/2014/03/sapota_chikoo.jpg");//SAPOTA
        imageLink.add("https://i5.walmartimages.ca/images/Large/805/2_r/6000196088052_R.jpg");//WATER_MELON
        imageLink.add("https://cdn3.volusion.com/kceqm.mleru/v/vspfiles/photos/707-3.jpg?1521734349");//JACK_FRUIT
        imageLink.add("https://gourmetdelight.in/image/cache/catalog/pdp/organicpomegranate-600x600.jpg");//POMEGRANATE_KABUl
        imageLink.add("https://5.imimg.com/data5/NY/QS/MY-58103236/red-banana-2f-red-jamaican-2f-sevvazhai-2f-chandrabale-250x250.jpg");//SEV_VAZHAI

        for (int i = 0; i < names.size(); i++) {
            Fruit fruit = new Fruit(names.get(i), null, null, imageLink.get(i), false, 0);
            fruits.add(fruit);
        }
        return fruits;
    }
}
