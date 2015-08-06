package com.lyricaloriginal.pdfrendersample;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

/**
 * Created by LyricalMaestro on 15/07/30.
 */
@RunWith(AndroidJUnit4.class)
public class PdfControllerTest extends AndroidTestCase {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor1() throws Exception {

        new PdfController(null);
    }

    @Test
    public void testConstructor2() throws Exception {
        File file = new File("sample.pdf");
        PdfController controller = new PdfController(file);

    }

}
