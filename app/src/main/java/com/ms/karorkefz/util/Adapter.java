package com.ms.karorkefz.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Adapter {
    JSONObject adapterJsonObject;

    public Adapter(String name) throws JSONException {
        try {
            String adapterString = FileUtil.readFileSdcardFile( "/adapter.json" );
            JSONObject jsonObject = new JSONObject( adapterString );
            String dataString = jsonObject.getString( "data" );
            JSONObject dataObject = new JSONObject( dataString );
            String jsonString = dataObject.getString( name );
            this.adapterJsonObject = new JSONObject( jsonString );
        } catch (Exception e) {
            String adapterString = "{\"version\": \"6.5.3.275\",\"data\": {\"adapter\": {\"version\": \"false\"}}}";
            JSONObject jsonObject = new JSONObject( adapterString );
            String dataString = jsonObject.getString( "data" );
            JSONObject dataObject = new JSONObject( dataString );
            String jsonString = dataObject.getString( name );
            this.adapterJsonObject = new JSONObject( jsonString );
        }
    }

    public JSONObject getObject(JSONArray jsonArray, int i) throws JSONException {
        JSONObject adapterJsonObject = jsonArray.getJSONObject( i );
        return adapterJsonObject;
    }

    public Adapter getObject(String string) throws JSONException, IOException {
        Adapter adapterJsonObject = new Adapter( string );
        return adapterJsonObject;
    }

    public String getString(JSONObject jsonbject, String name) throws JSONException {
        String JsonString = jsonbject.getString( name );
        return JsonString;
    }

    public String getString(String name) {
        try {
            String JsonString = this.adapterJsonObject.getString( name );
            return JsonString;
        } catch (Exception e) {
            return null;
        }
    }

}
