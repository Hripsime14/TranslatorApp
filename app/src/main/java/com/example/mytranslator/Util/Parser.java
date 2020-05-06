package com.example.mytranslator.Util;

import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Parser {
private List<String> supportedLanguageList = new ArrayList<>();

    public List<Pair<String, String>> parseDirs(String resposne) {
        Pair<String, String> dirs;
        List<Pair<String, String>> dirList = new ArrayList<>();
        String first, second;
        try {
            JSONObject object = new JSONObject(resposne);
            JSONArray array = object.getJSONArray("dirs");
            for (int i = 0; i < array.length(); i++) {
                first = array.get(i).toString().substring(0, 2);
                second = array.get(i).toString().substring(3, 5);
                dirs = new Pair<>(first, second);
                dirList.add(dirs);
//                Log.d("TranslatorLog", "parseDirs: f = " + first + ", s = " + second);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dirList;
    }

    public /*List<Map<String, String>>*/Map<String, String> parseLangs(String response) {
        Map<String, String> map = new HashMap<>();
//        List<Map<String, String>> langs = new ArrayList<>();
        Iterator<String> keys;
        String key, value;
        try {
            JSONObject object = new JSONObject(response);
            JSONObject obj = object.getJSONObject("langs");
            keys = obj.keys();
            while (keys.hasNext()) {
                key = keys.next();
                value = (String)obj.get(key);
                supportedLanguageList.add(value);
                map.put(key, value);
//                Log.d("TranslatorLog", "parseLangs: key = " + key + ", val = " + value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
//        langs.add(map);
//        return langs;
    }

    public List<String> getSupportedLanguageList() {
        return supportedLanguageList;
    }


    public String parseText(String response) {
        String translatedText = null;
        try {
            JSONObject object = new JSONObject(response);
            JSONArray array = object.getJSONArray("text");
            translatedText = array.get(0).toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
          return translatedText;
    }
}
