package com.example.lhc.share;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by a1 on 2017/6/29.
 */
public class GetJsonDataUtil {
    public String getJson(Context context, String fileName) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        AssetManager assetManager = context.getAssets();
        BufferedReader bf = new BufferedReader(new InputStreamReader(
                assetManager.open(fileName)));
        String line;
        while ((line = bf.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}
