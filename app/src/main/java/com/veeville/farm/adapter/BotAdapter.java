package com.veeville.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.veeville.farm.R;
import com.veeville.farm.activity.HumidityActivity;
import com.veeville.farm.activity.ImageShowActivity;
import com.veeville.farm.activity.LightActivity;
import com.veeville.farm.activity.PriceActivity;
import com.veeville.farm.activity.SoilMoistureActivity;
import com.veeville.farm.activity.SoilPhActivity;
import com.veeville.farm.activity.TemperatureActivity;
import com.veeville.farm.activity.WeatherActivity;
import com.veeville.farm.activity.YouTubePlayerVersion2;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.ChatmessageDataClasses;
import com.veeville.farm.helper.InputImageClass;
import com.veeville.farm.helper.OptionMenuItems;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 10-07-2017.
 */

public class BotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>

{
    private List<Object> messagelist;
    private Context context;
    private final String TAG = BotAdapter.class.getSimpleName();
    private QuickReplyAdapter.QuickReplyOption option;
    private boolean showNormal = false;

    public BotAdapter(List<Object> textmessagelist, Context context, QuickReplyAdapter.QuickReplyOption quickReplyOption) {
        this.messagelist = textmessagelist;
        this.context = context;
        this.option = quickReplyOption;
    }

    @Override
    public int getItemViewType(int position) {
        if (messagelist.get(position) instanceof ChatmessageDataClasses.InputTextMessage) {
            return 0;
        } else if (messagelist.get(position) instanceof ChatmessageDataClasses.ResponseTextMessage) {
            return 1;
        } else if (messagelist.get(position) instanceof ChatmessageDataClasses.InputImageMessage) {
            return 3;
        } else if (messagelist.get(position) instanceof ChatmessageDataClasses.OptionMenu) {
            return 4;
        } else if (messagelist.get(position) instanceof ChatmessageDataClasses.ResponseVideoMessage) {
            return 5;
        } else if (messagelist.get(position) instanceof InputImageClass) {
            return 6;
        } else if (messagelist.get(position) instanceof OptionMenuItems) {
            return 8;
        } else if (messagelist.get(position) instanceof ChatmessageDataClasses.ResponseImages) {
            return 9;
        } else if (messagelist.get(position) instanceof ChatmessageDataClasses.VisualQnA) {
            return 10;
        } else if (messagelist.get(position) instanceof ChatmessageDataClasses.Humidity) {
            return 11;
        } else if (messagelist.get(position) instanceof ChatmessageDataClasses.SoilPH) {
            return 12;
        } else if (messagelist.get(position) instanceof ChatmessageDataClasses.SoilTemperature) {
            return 13;
        } else if (messagelist.get(position) instanceof ChatmessageDataClasses.SoilMoisture) {
            return 14;
        } else if (messagelist.get(position) instanceof ChatmessageDataClasses.Light) {
            return 15;
        } else if (messagelist.get(position) instanceof ChatmessageDataClasses.WeatherData) {
            return 16;
        } else if (messagelist.get(position) instanceof ChatmessageDataClasses.VegFruitPrice) {
            return 17;
        } else if (messagelist.get(position) instanceof ChatmessageDataClasses.DateInMessage) {
            return 18;
        } else if (messagelist.get(position) instanceof ChatmessageDataClasses.HealthCard) {
            return 19;
        } else {
            return -1;
        }
    }

    private String getTime(long timestamp) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestamp);
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        return dateFormat.format(calendar.getTime());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case 0:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simpletextcardinput, parent, false);
                viewHolder = new InputTextMessageHolder(view);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simpletextresponse, parent, false);
                viewHolder = new ResponseTextMessageHolder(view);
                break;
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simpletextresponse, parent, false);
                viewHolder = new ResponseTextMessageHolder(view);
                break;
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_movieresultcard, parent, false);
                viewHolder = new SingleMovieHolder(view);
                break;
            case 4:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quickreplieslayout, parent, false);
                viewHolder = new QuickReplyHolder(view);
                break;
            case 5:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_option_recyclerivew_main, parent, false);
                viewHolder = new ResponseVideoMessageHolder(view);
                break;
            case 6:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inputimagecard, parent, false);
                viewHolder = new InputImageHolder(view);
                break;
            case 7:
                break;
            case 8:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_option_recyclerivew_main, parent, false);
                viewHolder = new AvailableOPtionHolder(view);
                break;
            case 9:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_option_recyclerivew_main, parent, false);
                viewHolder = new ResponseImagesHolder(view);
                break;
            case 10:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vqna_layout_card, parent, false);
                viewHolder = new VisualQnAHolder(view);
                break;
            case 11:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.humidity_activity_card, parent, false);
                viewHolder = new HumidityCardHolder(view);
                break;
            case 12:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_ph_remaining_cards, parent, false);
                viewHolder = new SoilPhHolder(view);
                break;
            case 13:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.temperature_activity_remaining_cards, parent, false);
                viewHolder = new SoilTemperatureHolder(view);
                break;
            case 14:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_moisture_activity_remaining_cards, parent, false);
                viewHolder = new SoilMoistureHolder(view);
                break;
            case 15:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.light_activity_remaining_cards, parent, false);
                viewHolder = new LightHolder(view);
                break;
            case 16:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_activity_card, parent, false);
                viewHolder = new WeatherCardHolder(view);
                break;
            case 17:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.veg_fruit_price_card_layout, parent, false);
                viewHolder = new VegFruitPriceCardHolder(view);
                break;
            case 18:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dateview_card, parent, false);
                viewHolder = new DateInMessageHolder(view);
                break;
            case 19:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_health_card_layout, parent, false);
                viewHolder = new HealthCardHolder(view);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_health_card_layout, parent, false);
                viewHolder = new HealthCardHolder(view);
        }
        return viewHolder;
    }


    public void changeDatasetMessage(boolean showNormal) {

        this.showNormal = showNormal;
        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                handleInputTextMessage((InputTextMessageHolder) holder, position);
                break;
            case 1:
                handleOverviewOutPutData((ResponseTextMessageHolder) holder, position);
                break;
            case 3:
                handleSingleMovie((SingleMovieHolder) holder, position);
                break;
            case 4:
                handleQuickReplies((QuickReplyHolder) holder, position);
                break;
            case 5:
                handleVideoCard((ResponseVideoMessageHolder) holder, position);
                break;
            case 6:
                handleInputImageCard((InputImageHolder) holder, position);
                break;
            case 8:
                handleMainOptionMenu((AvailableOPtionHolder) holder, position);
                break;
            case 9:
                handleResponseImagesHolder((ResponseImagesHolder) holder, position);
                break;
            case 10:
                handleVisualQna((VisualQnAHolder) holder, position);
                break;
            case 11:
                handleHumidityCard((HumidityCardHolder) holder, position);
                break;
            case 12:
                handleSoilPhCard((SoilPhHolder) holder, position);
                break;
            case 13:
                handleSoilTmeprature((SoilTemperatureHolder) holder, position);
                break;
            case 14:
                handleSoilMoisture((SoilMoistureHolder) holder, position);
                break;
            case 15:
                handleLightCard((LightHolder) holder, position);
                break;
            case 16:
                handleWeatherCardData((WeatherCardHolder) holder, position);
                break;
            case 17:
                handleVegFruitPrice((VegFruitPriceCardHolder) holder, position);
                break;
            case 18:
                handleDate((DateInMessageHolder) holder, position);
                break;
            case 19:
                handleFarmHealthCard((HealthCardHolder) holder, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        logMessage("size:" + messagelist.size());
        return messagelist.size();
    }


    private void handleFarmHealthCard(HealthCardHolder holder, int position) {

        LinearLayoutManager manager = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(manager);
        ChatmessageDataClasses.HealthCard healthCard = (ChatmessageDataClasses.HealthCard) messagelist.get(position);
        FarmHealthCardAdapter adapter = new FarmHealthCardAdapter(context, healthCard.healthList);
        holder.recyclerView.setAdapter(adapter);
    }

    private void handleVegFruitPrice(VegFruitPriceCardHolder holder, int position) {

        ChatmessageDataClasses.VegFruitPrice vegFruitPrice = (ChatmessageDataClasses.VegFruitPrice) messagelist.get(position);
        holder.time.setText(getTime(vegFruitPrice.timestamp));
        holder.description.setText(vegFruitPrice.description);
        Glide.with(context).load(vegFruitPrice.imageLink).into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PriceActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void handleDate(DateInMessageHolder holder, int position) {

        ChatmessageDataClasses.DateInMessage dateInMessage = (ChatmessageDataClasses.DateInMessage) messagelist.get(position);
        holder.textView.setText(dateInMessage.date);

    }

    private void handleWeatherCardData(WeatherCardHolder holder, int position) {
        final ChatmessageDataClasses.WeatherData weatherData = (ChatmessageDataClasses.WeatherData) messagelist.get(position);
        String weather = weatherData.temp + "\u00b0";
        holder.temp.setText(weather);
        holder.date.setText(weatherData.date);
        holder.place.setText(weatherData.place);
        String humidity = "Humidty(%): " + weatherData.humidity;
        String windSpeed = "WindSpeed(kmph): " + weatherData.wind;
        String precipitation = "Precipitation: " + weatherData.precipitation;
        holder.humidity.setText(humidity);
        holder.windSpeed.setText(windSpeed);
        holder.prec.setText(precipitation);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        Glide.with(context).load(weatherData.imgLink).into(holder.weather_image);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(manager);
        WeatherTimeWithTempAdapter adapter = new WeatherTimeWithTempAdapter(weatherData.tempHourly, context);
        holder.recyclerView.setAdapter(adapter);
        holder.weahter_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WeatherActivity.class);
                intent.putExtra("city", weatherData.place);
                context.startActivity(intent);
            }
        });
    }

    private void handleLightCard(LightHolder holder, int position) {

        ChatmessageDataClasses.Light light = (ChatmessageDataClasses.Light) messagelist.get(position);
        holder.place.setText(light.place);
        holder.date.setText(light.month);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        LightTimeWithValuesAdapter adapter = new LightTimeWithValuesAdapter(light.soilMoistureValues);
        holder.valuesRecyclerview.setLayoutManager(manager);
        holder.valuesRecyclerview.setAdapter(adapter);
        holder.valuesRecyclerview.scrollToPosition(5);
        holder.light_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LightActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void handleSoilMoisture(SoilMoistureHolder holder, int position) {

        ChatmessageDataClasses.SoilMoisture soilMoisture = (ChatmessageDataClasses.SoilMoisture) messagelist.get(position);
        holder.date.setText(soilMoisture.month);
        holder.place.setText(soilMoisture.place);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        SoilMoistureValuesAdapter adapter = new SoilMoistureValuesAdapter(soilMoisture.soilMoistureValues);
        holder.valuesRecyclerview.setLayoutManager(manager);
        holder.valuesRecyclerview.setAdapter(adapter);
        holder.valuesRecyclerview.scrollToPosition(5);
        holder.soil_moisture_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SoilMoistureActivity.class);
                context.startActivity(intent);
            }
        });

    }

    private void handleSoilTmeprature(SoilTemperatureHolder holder, int position) {
        ChatmessageDataClasses.SoilTemperature soilTemperature = (ChatmessageDataClasses.SoilTemperature) messagelist.get(position);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.valuesRecyclerview.setLayoutManager(manager);
        SoilTempValuesAdapter adapter = new SoilTempValuesAdapter(soilTemperature.soilMoistureValues);
        holder.valuesRecyclerview.setAdapter(adapter);
        holder.date.setText(soilTemperature.date);
        holder.place.setText(soilTemperature.place);
        holder.soilTempCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TemperatureActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void handleSoilPhCard(SoilPhHolder holder, int position) {
        ChatmessageDataClasses.SoilPH soilPH = (ChatmessageDataClasses.SoilPH) messagelist.get(position);
        holder.date.setText(soilPH.date);
        holder.place.setText(soilPH.place);
        holder.value.setText(soilPH.value);
        holder.soilPhCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SoilPhActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void handleHumidityCard(HumidityCardHolder holder, int position) {
        ChatmessageDataClasses.Humidity humidity = (ChatmessageDataClasses.Humidity) messagelist.get(position);
        holder.date.setText(humidity.date);
        holder.place.setText(humidity.place);
        holder.humidityCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HumidityActivity.class);
                context.startActivity(intent);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.valuesRecyclerview.setLayoutManager(manager);
        HumidityValuesAdapter adapter = new HumidityValuesAdapter(humidity.humidityDataValues);
        holder.valuesRecyclerview.setAdapter(adapter);

    }

    private void handleVisualQna(VisualQnAHolder holder, int position) {
        ChatmessageDataClasses.VisualQnA qnA = (ChatmessageDataClasses.VisualQnA) messagelist.get(position);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.visualQna.setLayoutManager(manager);
        VisualQnaAdapter adapter = new VisualQnaAdapter(qnA.elements);
        holder.visualQna.setAdapter(adapter);
        holder.visualQna.scrollToPosition(qnA.elements.size() - 1);
        holder.visualQna.setNestedScrollingEnabled(true);

    }

    private void handleResponseImagesHolder(ResponseImagesHolder holder, int position) {

        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(manager);
        ChatmessageDataClasses.ResponseImages images = (ChatmessageDataClasses.ResponseImages) messagelist.get(position);
        ResponseImagesAdapter adapter = new ResponseImagesAdapter(images.imageLinks, images.dataOfImages, images.diseaseNames);
        holder.recyclerView.setAdapter(adapter);
    }

    private void handleMainOptionMenu(AvailableOPtionHolder holder, int position) {

        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(manager);
        OptionMenuItems menuItems = (OptionMenuItems) messagelist.get(position);
        MainOptionMenuAdapter adapter = new MainOptionMenuAdapter(menuItems.titles);
        holder.recyclerView.setAdapter(adapter);


    }

    private void handleInputImageCard(InputImageHolder holder, int position) {
        final InputImageClass inputImageClass = (InputImageClass) messagelist.get(position);
        byte[] decodedString = Base64.decode(inputImageClass.imagebytes, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.input_imageview.setImageBitmap(decodedByte);

        holder.time.setText(getTime(inputImageClass.timestamp));
        if (inputImageClass.isUploadSuccess) {
            holder.imageUploadProgressbar.setVisibility(View.GONE);
        } else {
            holder.imageUploadProgressbar.setVisibility(View.VISIBLE);
        }
        holder.input_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ImageShowActivity.class);
                intent.putExtra("image_data", inputImageClass.imagebytes);
                view.getContext().startActivity(intent);
            }
        });
    }

    private void handleVideoCard(ResponseVideoMessageHolder videoHolder, int position) {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        videoHolder.recyclerView.setLayoutManager(manager);
        ChatmessageDataClasses.ResponseVideoMessage responseVideoMessage = (ChatmessageDataClasses.ResponseVideoMessage) messagelist.get(position);
        VideoListAdapter adapter = new VideoListAdapter(responseVideoMessage.thumbnail, responseVideoMessage.videoIds, responseVideoMessage.timestamp);
        videoHolder.recyclerView.setAdapter(adapter);
    }

    private void handleQuickReplies(QuickReplyHolder holder, int position) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.quickreplyrecyclerview.setLayoutManager(linearLayoutManager);
        QuickReplyAdapter quickReplyAdapter = new QuickReplyAdapter((ChatmessageDataClasses.OptionMenu) messagelist.get(position), option);
        holder.quickreplyrecyclerview.setAdapter(quickReplyAdapter);
    }

    private void handleOverviewOutPutData(ResponseTextMessageHolder myholderOutput, int position) {

        ChatmessageDataClasses.ResponseTextMessage responseData = (ChatmessageDataClasses.ResponseTextMessage) messagelist.get(position);
        myholderOutput.singlemesssage.setText(responseData.responseTextMessage);
        myholderOutput.time.setText(getTime(responseData.timestamp));
        if (showNormal) {
            myholderOutput.singlemesssage.setRotation(0);
        } else {
            myholderOutput.singlemesssage.setRotation(180);
        }
    }

    private void handleInputTextMessage(final InputTextMessageHolder myholderInput, int position) {

        ChatmessageDataClasses.InputTextMessage inputData = (ChatmessageDataClasses.InputTextMessage) messagelist.get(position);
        myholderInput.singlemesssage.setText(inputData.inputTextMessage);
        myholderInput.time.setText(getTime(inputData.timestamp));
    }


    private void handleSingleMovie(SingleMovieHolder holder, int position) {

        final ChatmessageDataClasses.InputImageMessage movieClass = (ChatmessageDataClasses.InputImageMessage) messagelist.get(position);
        holder.time.setText(getTime(movieClass.timestamp));
        Picasso.with(context).load(movieClass.imageLink).into(holder.movieposter);
        holder.movieposter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ImageShowActivity.class);
                intent.putExtra("image_url", movieClass.imageLink);
                view.getContext().startActivity(intent);
            }
        });


    }

    class SoilMoistureHolder extends RecyclerView.ViewHolder {
        RecyclerView valuesRecyclerview;
        TextView date, place;
        CardView soil_moisture_card;

        SoilMoistureHolder(View view) {
            super(view);
            valuesRecyclerview = view.findViewById(R.id.recyclerview_values);
            date = view.findViewById(R.id.date);
            place = view.findViewById(R.id.place);
            soil_moisture_card = view.findViewById(R.id.soil_moisture_card);

        }
    }

    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }
    class DateInMessageHolder extends RecyclerView.ViewHolder {
        TextView textView;

        DateInMessageHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.date);
        }
    }

    class WeatherCardHolder extends RecyclerView.ViewHolder {
        TextView date, place, temp, prec, humidity, windSpeed;
        CardView weahter_card;
        ImageView weather_image;
        RecyclerView recyclerView;

        WeatherCardHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            place = view.findViewById(R.id.place);
            weahter_card = view.findViewById(R.id.weather_card);
            recyclerView = view.findViewById(R.id.recyclerview);
            prec = view.findViewById(R.id.precipitation);
            windSpeed = view.findViewById(R.id.wind_speed);
            humidity = view.findViewById(R.id.humidity);
            temp = view.findViewById(R.id.temperature);
            weather_image = view.findViewById(R.id.weather_icon);
        }
    }

    class VegFruitPriceCardHolder extends RecyclerView.ViewHolder {

        TextView description, time;
        CardView cardView;
        ImageView imageView;

        VegFruitPriceCardHolder(View view) {
            super(view);
            description = view.findViewById(R.id.description);
            cardView = view.findViewById(R.id.cardview);
            imageView = view.findViewById(R.id.imageview);
            time = view.findViewById(R.id.time);
        }

    }

    class LightHolder extends RecyclerView.ViewHolder {
        RecyclerView valuesRecyclerview;
        TextView date, place;
        CardView light_card;

        LightHolder(View view) {
            super(view);
            valuesRecyclerview = view.findViewById(R.id.recyclerview_values);
            date = view.findViewById(R.id.date);
            place = view.findViewById(R.id.place);
            light_card = view.findViewById(R.id.light_card);

        }
    }

    class SoilPhHolder extends RecyclerView.ViewHolder {
        TextView date, place, value;
        CardView soilPhCard;

        SoilPhHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            place = view.findViewById(R.id.place);
            value = view.findViewById(R.id.value);
            soilPhCard = view.findViewById(R.id.soilph_card);
        }
    }

    private class SoilTemperatureHolder extends RecyclerView.ViewHolder {
        RecyclerView valuesRecyclerview;
        TextView date, place;
        CardView soilTempCard;

        SoilTemperatureHolder(View view) {
            super(view);
            valuesRecyclerview = view.findViewById(R.id.recyclerview_values);
            date = view.findViewById(R.id.date);
            place = view.findViewById(R.id.place);
            soilTempCard = view.findViewById(R.id.soil_temperature_card);
        }
    }

    class HumidityCardHolder extends RecyclerView.ViewHolder {
        RecyclerView valuesRecyclerview;
        TextView date, place;
        CardView humidityCard;

        HumidityCardHolder(View view) {
            super(view);
            valuesRecyclerview = view.findViewById(R.id.recyclerview_values);
            date = view.findViewById(R.id.date);
            place = view.findViewById(R.id.place);
            humidityCard = view.findViewById(R.id.humidty_card);
        }
    }


    class InputImageHolder extends RecyclerView.ViewHolder {
        ImageView input_imageview;
        ProgressBar imageUploadProgressbar;
        TextView time;

        InputImageHolder(View view) {
            super(view);
            input_imageview = view.findViewById(R.id.imageinput);
            imageUploadProgressbar = view.findViewById(R.id.image_upload_progressbar);
            time = view.findViewById(R.id.time);
        }
    }

    class InputTextMessageHolder extends RecyclerView.ViewHolder {

        private TextView singlemesssage, time;

        InputTextMessageHolder(View view) {
            super(view);
            singlemesssage = view.findViewById(R.id.singletextmessage);
            time = view.findViewById(R.id.time);
        }
    }

    class ResponseVideoMessageHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        ResponseVideoMessageHolder(View view) {
            super(view);
            recyclerView = view.findViewById(R.id.recyclerview);
        }
    }

    class ResponseTextMessageHolder extends RecyclerView.ViewHolder {

        private TextView singlemesssage, time;

        ResponseTextMessageHolder(View view) {
            super(view);
            singlemesssage = view.findViewById(R.id.singletextmessage);
            time = view.findViewById(R.id.time);
        }
    }

    class SingleMovieHolder extends RecyclerView.ViewHolder {
        ImageView movieposter;
        TextView time;

        SingleMovieHolder(View view) {
            super(view);
            movieposter = view.findViewById(R.id.movieimage);
            time = view.findViewById(R.id.time);
        }
    }

    class QuickReplyHolder extends RecyclerView.ViewHolder {
        public RecyclerView quickreplyrecyclerview;

        QuickReplyHolder(View view) {
            super(view);
            quickreplyrecyclerview = view.findViewById(R.id.quickreplyrecyclerview);
        }
    }

    class ResponseImagesHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        ResponseImagesHolder(View view) {
            super(view);
            recyclerView = view.findViewById(R.id.recyclerview);
        }
    }

    class AvailableOPtionHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        AvailableOPtionHolder(View view) {
            super(view);
            recyclerView = view.findViewById(R.id.recyclerview);
        }
    }

    class VisualQnAHolder extends RecyclerView.ViewHolder {
        RecyclerView visualQna;

        VisualQnAHolder(View view) {
            super(view);
            visualQna = view.findViewById(R.id.vqna_recyclerview);
        }
    }


    class MainOptionMenuAdapter extends RecyclerView.Adapter<MainOptionMenuAdapter.ItemHolderOptionMenu> {
        List<String> menuNames;

        MainOptionMenuAdapter(List<String> menuNames) {
            this.menuNames = menuNames;
        }

        @NonNull
        @Override
        public ItemHolderOptionMenu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_option_menu_card, parent, false);
            return new ItemHolderOptionMenu(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolderOptionMenu holder, final int position_temp) {
            final int position = holder.getAdapterPosition();
            holder.title.setText(menuNames.get(position));
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "selected text is :" + menuNames.get(position), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return menuNames.size();
        }

        class ItemHolderOptionMenu extends RecyclerView.ViewHolder {
            TextView title;
            CardView card;

            ItemHolderOptionMenu(View view) {
                super(view);
                title = view.findViewById(R.id.title);
                card = view.findViewById(R.id.card);
            }
        }
    }

    class ResponseImagesAdapter extends RecyclerView.Adapter<ResponseImagesAdapter.SingleImageHolder> {

        List<String> imageLinks, imageData, diseaseNames;

        ResponseImagesAdapter(List<String> imageLinks, List<String> imageData, List<String> diseaseNames) {
            this.imageData = imageData;
            this.imageLinks = imageLinks;
            this.diseaseNames = diseaseNames;
        }

        @NonNull
        @Override
        public SingleImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.respnse_images_single_card, parent, false);
            return new SingleImageHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SingleImageHolder holder, final int position_temp) {
            final int position = holder.getAdapterPosition();
            Glide.with(context).load(imageLinks.get(position)).into(holder.imageView);
            holder.image_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    option.insertImage(imageData.get(position), imageLinks.get(position));
                    option.insertDiseaseNames(diseaseNames.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return imageLinks.size();
        }

        class SingleImageHolder extends RecyclerView.ViewHolder {
            CardView image_card;
            ImageView imageView;

            SingleImageHolder(View view) {
                super(view);
                image_card = view.findViewById(R.id.image_card);
                imageView = view.findViewById(R.id.image);
            }
        }
    }

    class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.SingleVideoHolder> {
        List<String> imageLink, videoId;
        long timestamp;

        VideoListAdapter(List<String> imageLink, List<String> videoId, long timestamp) {
            this.imageLink = imageLink;
            this.videoId = videoId;
            this.timestamp = timestamp;
        }

        @NonNull
        @Override
        public SingleVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.videocardlayout, parent, false);
            return new SingleVideoHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SingleVideoHolder holder, int position_temp) {
            final int position = holder.getAdapterPosition();
            holder.time.setText(getTime(timestamp));
            Picasso.with(context).load(imageLink.get(position)).into(holder.videothumbnail);
            holder.videothumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), YouTubePlayerVersion2.class);
                    intent.putExtra("videoKey", videoId.get(position));
                    (v.getContext()).startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return imageLink.size();
        }

        class SingleVideoHolder extends RecyclerView.ViewHolder {
            ImageView videothumbnail;
            TextView time;

            SingleVideoHolder(View view) {
                super(view);
                time = view.findViewById(R.id.time);
                videothumbnail = view.findViewById(R.id.videolinkthumbnail);
            }
        }
    }

    class HealthCardHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        HealthCardHolder(View view) {
            super(view);
            recyclerView = view.findViewById(R.id.recyclerview);
        }
    }

    class HumidityValuesAdapter extends RecyclerView.Adapter<HumidityValuesAdapter.SingleValueHolder> {
        List<ChatmessageDataClasses.Humidity.HumidityDataValues> humidityDataValues;

        HumidityValuesAdapter(List<ChatmessageDataClasses.Humidity.HumidityDataValues> humidityDataValues) {
            this.humidityDataValues = humidityDataValues;
        }

        @NonNull
        @Override
        public SingleValueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.humidity_value_card_with_time, parent, false);
            return new SingleValueHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SingleValueHolder holder, int position) {
            ChatmessageDataClasses.Humidity.HumidityDataValues values = humidityDataValues.get(position);
            holder.time.setText(values.hour);
            holder.hAbsulute.setText(values.hAbsulute);
            holder.hRelative.setText(values.hRelative);
        }

        @Override
        public int getItemCount() {
            return humidityDataValues.size();
        }

        class SingleValueHolder extends RecyclerView.ViewHolder {
            TextView time, hRelative, hAbsulute;

            SingleValueHolder(View view) {
                super(view);
                time = view.findViewById(R.id.time_humidity);
                hRelative = view.findViewById(R.id.h_relative);
                hAbsulute = view.findViewById(R.id.h_absolute);
            }
        }
    }

    class SoilTempValuesAdapter extends RecyclerView.Adapter<SoilTempValuesAdapter.SingleValueHolder> {
        List<ChatmessageDataClasses.SoilTemperature.TempValue> tempValues;

        SoilTempValuesAdapter(List<ChatmessageDataClasses.SoilTemperature.TempValue> list) {
            this.tempValues = list;
        }

        @NonNull
        @Override
        public SingleValueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_moisture_time_value, parent, false);
            return new SingleValueHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SingleValueHolder holder, int position) {

            holder.value.setText(tempValues.get(position).value1);
            holder.time.setText(tempValues.get(position).date);
        }

        @Override
        public int getItemCount() {
            return tempValues.size();
        }

        class SingleValueHolder extends RecyclerView.ViewHolder {
            TextView time, value;

            SingleValueHolder(View view) {
                super(view);
                time = view.findViewById(R.id.time);
                value = view.findViewById(R.id.value);
            }
        }
    }

    class SoilMoistureValuesAdapter extends RecyclerView.Adapter<SoilMoistureValuesAdapter.SingleValueHolder> {
        List<ChatmessageDataClasses.SoilMoisture.SoilMoistureValues> lightValues;

        SoilMoistureValuesAdapter(List<ChatmessageDataClasses.SoilMoisture.SoilMoistureValues> lightValues) {
            this.lightValues = lightValues;
        }

        @NonNull
        @Override
        public SingleValueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_moisture_time_value, parent, false);
            return new SingleValueHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SingleValueHolder holder, int position) {
            holder.value.setText(lightValues.get(position).value1);
            holder.time.setText(lightValues.get(position).date);

        }

        @Override
        public int getItemCount() {
            return lightValues.size();
        }

        class SingleValueHolder extends RecyclerView.ViewHolder {
            TextView time, value;

            SingleValueHolder(View view) {
                super(view);
                time = view.findViewById(R.id.time);
                value = view.findViewById(R.id.value);
            }
        }
    }

    class VisualQnaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List vqNaList;

        VisualQnaAdapter(List vqNaList) {
            this.vqNaList = vqNaList;
        }

        @Override
        public int getItemViewType(int position) {
            if (vqNaList.get(position) instanceof ChatmessageDataClasses.InputTextMessage) {
                return 0;
            } else if (vqNaList.get(position) instanceof ChatmessageDataClasses.ResponseTextMessage) {
                return 1;
            } else if (vqNaList.get(position) instanceof ChatmessageDataClasses.InputImageMessage) {
                return 2;
            } else {
                return -1;
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            RecyclerView.ViewHolder holder;
            switch (viewType) {
                case 0:
                    View view = LayoutInflater.from(context).inflate(R.layout.simpletextcardinput, parent, false);
                    holder = new InputTextHolder(view);
                    break;
                case 1:
                    view = LayoutInflater.from(context).inflate(R.layout.simpletextresponse, parent, false);
                    holder = new ResponseTextMessage(view);
                    break;
                case 2:
                    view = LayoutInflater.from(context).inflate(R.layout.inputimagecard, parent, false);
                    holder = new InputImageHolder(view);
                    break;
                default:
                    view = LayoutInflater.from(context).inflate(R.layout.inputimagecard, parent, false);
                    holder = new InputImageHolder(view);

            }
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case 0:
                    handleInputTextMessage((InputTextHolder) holder, position);
                    break;
                case 1:
                    handleResponseMessage((ResponseTextMessage) holder, position);
                    break;
                case 2:
                    handleInputImage((InputImageHolder) holder, position);
                    break;
            }
        }

        void handleInputImage(InputImageHolder holder, int position) {
            ChatmessageDataClasses.InputImageMessage inputImageMessage = (ChatmessageDataClasses.InputImageMessage) vqNaList.get(position);
            byte[] decodedString = Base64.decode(inputImageMessage.imageLink, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imageView.setImageBitmap(decodedByte);
            holder.time.setText(getTime(inputImageMessage.timestamp));
        }

        void handleInputTextMessage(InputTextHolder holder, int position) {
            ChatmessageDataClasses.InputTextMessage inputTextMessage = (ChatmessageDataClasses.InputTextMessage) vqNaList.get(position);
            holder.time.setText(getTime(inputTextMessage.timestamp));
            holder.textView.setText(inputTextMessage.inputTextMessage);
        }

        void handleResponseMessage(ResponseTextMessage holder, int position) {
            ChatmessageDataClasses.ResponseTextMessage message = (ChatmessageDataClasses.ResponseTextMessage) vqNaList.get(position);
            holder.time.setText(getTime(message.timestamp));
            holder.textView.setText(message.responseTextMessage);
        }

        @Override
        public int getItemCount() {
            return vqNaList.size();
        }

        class InputTextHolder extends RecyclerView.ViewHolder {
            TextView textView, time;

            InputTextHolder(View view) {
                super(view);
                textView = view.findViewById(R.id.singletextmessage);
                time = view.findViewById(R.id.time);
            }
        }

        class InputImageHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView time;

            InputImageHolder(View view) {
                super(view);
                imageView = view.findViewById(R.id.imageinput);
                time = view.findViewById(R.id.time);
            }
        }

        class ResponseTextMessage extends RecyclerView.ViewHolder {
            TextView textView, time;

            ResponseTextMessage(View view) {
                super(view);
                textView = view.findViewById(R.id.singletextmessage);
                time = view.findViewById(R.id.time);
            }
        }
    }
}
