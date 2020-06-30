package com.ftx.mvvm_template;

import android.app.Application;
import android.arch.persistence.room.Room;
import com.facebook.FacebookSdk;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ftx.mvvm_template.model.db.MyDatabase;
import com.ftx.mvvm_template.utils.AppLog;
import com.ftx.mvvm_template.utils.DeviceUuidFactory;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.io.IOException;

import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * Name : TemplateApplication
 * <br> Purpose :    Application class to manage the app level configurations.
 */
public class TemplateApplication extends Application {

    public static TemplateApplication mContext;
    private final String DATABASE_NAME = "demoroom";
//    private MyDatabase db;

    private RefWatcher mRefWatcher;
    private DeviceUuidFactory mDeviceUuidFactory;
    private ObjectMapper jsonMapper;
    private MyDatabase db;
    public Converter.Factory JACKSON ;
    public Converter.Factory GSON ;

    public static TemplateApplication getAppInstance() {
        return mContext;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
        //  Fabric.with(this, new Crashlytics());


        mContext = this;

        if (BuildConfig.DEBUG)
            mRefWatcher = LeakCanary.install(this);
        mDeviceUuidFactory = new DeviceUuidFactory(this);
//        getDbInstance();
    }

    public RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

    public String getDeviceUuidFactory() {
        return mDeviceUuidFactory.getDeviceUuid().toString();
    }

    public MyDatabase getDbInstance(){

        if (db == null)
            db = Room.databaseBuilder(this, MyDatabase.class, DATABASE_NAME)

                    /**
                     * Room ensures that Database is never accessed on the main thread because it may lock the main thread and trigger an ANR.
                     * If you need to access the database from the main thread, you should always use async alternatives or manually move the call to a background thread.
                     */
//                    .allowMainThreadQueries()
                    .addMigrations(MyDatabase.MIGRATION_1_2)
                    .build();

        return db;
    }

    public Converter.Factory getJaksonConverter(){
        if (JACKSON == null)
            return JACKSON = JacksonConverterFactory.create(getJsonMapper());
        else
            return JACKSON;
    }

    public Converter.Factory getGsonConverter(){
        if (GSON == null)
            return GSON = GsonConverterFactory.create();
        else
            return GSON;
    }

    /**
     * Name : getDbInstance
     *<br> Purpose : To get instance of RoomDatabase class
     * @return Database instance
     */
//    public MyDatabase getDbInstance(){
//
//        if (db == null)
//            db = Room.databaseBuilder(mContext, MyDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
//
//        return db;
//    }

    public ObjectMapper getJsonMapper() {
        if (jsonMapper == null) {
            jsonMapper = new ObjectMapper();


// to enable standard indentation ("pretty-printing"):
            jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
// to allow serialization of "empty" POJOs (no properties to serialize)
// (without this setting, an exception is thrown in those cases)
            jsonMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
// to write java.util.Date, Calendar as number (timestamp):
            jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

// DeserializationFeature for changing how JSON is read as POJOs:

// to prevent exception when encountering unknown property:
            jsonMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
// to allow coercion of JSON empty String ("") to null Object value:
            jsonMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

            jsonMapper.setVisibility(jsonMapper.getSerializationConfig()
                    .getDefaultVisibilityChecker()
                    .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                    .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                    .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                    .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        }
        return jsonMapper;
    }



    /**
     * Name : TemplateApplication getJSONFromObject
     *<br> Purpose :This method will return json String from Object.
     *
     * @param o : class object of which we want to get json
     * @return : json string of POJO class.
     */
    public String getJSONFromObject(Object o) {
        try {
            return getJsonMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Name : TemplateApplication convertJsonToObject
     *<br> Purpose : Convert any json string to model java class.
     *
     * @param jsonString    : json string
     * @param objectClass   : class for which we want to get object
     * @return  : object of class
     */
    public Object convertJsonToObject(String jsonString, Class objectClass) {
        try {
            return getJsonMapper().readValue(jsonString, objectClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Name : TemplateApplication getGson
     * <br> Purpose :
     * To get singleton instance of {@link } with exclude field without expose. It will generate {@link } instance only if existing instance is null
     *
     * @return OkHttpClient instance
     */
//    public Gson getGson()
//    {
//        if (mGson == null)
//        {
//            GsonFactory.Builder builder = new GsonFactory.Builder();
//            mGson = builder.buildByExcludeFieldsWithoutExpose();
//        }
//        return mGson;
//    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        AppLog.e(TAG, "onLowMemory()");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        AppLog.e(TAG, "onTrimMemory()");
        AppLog.e(TAG, "level: " + level);
    }


}
