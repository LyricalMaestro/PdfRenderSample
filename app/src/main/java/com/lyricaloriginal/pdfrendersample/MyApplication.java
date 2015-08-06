package com.lyricaloriginal.pdfrendersample;

import android.app.Application;

import java.io.File;
import java.io.IOException;

/**
 * アプリケーション起動時の処理を記述するためのクラスです。
 *
 * Created by LyricalMaestro on 15/08/02.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        File out = new File(getFilesDir(), "sample.pdf");
        try {
            AssetsUtils.copy(this, "sample.pdf", out);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
