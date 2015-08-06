package com.lyricaloriginal.pdfrendersample;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by LyricalMaestro on 15/08/02.
 */
public final class AssetsUtils {

    private AssetsUtils() {
    }

    /**
     *
     *
     * @param context
     * @param assetFilename
     * @param out
     * @throws IOException
     */
    public static void copy(Context context, String assetFilename, File out) throws IOException {
        if (context == null) {
            return;
        } else if (out == null) {
            return;
        }

        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }

        AssetManager assetManager = context.getAssets();
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = assetManager.open(assetFilename);
            fos = new FileOutputStream(out);
            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = is.read(buf, 0, 1024)) != -1) {
                fos.write(buf, 0, len);
            }
        } finally {
            if (fos != null)
                fos.close();

            if (is != null)
                is.close();
        }
    }
}
