package com.lyricaloriginal.pdfrendersample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

/**
 * PDFファイルを閲覧するためのActivityです。
 */
public class PdfViewerActivity extends Activity {

    private ImageView mRewindBtn;
    private ImageView mPrevBtn;
    private ImageView mNextBtn;
    private ImageView mForwardBtn;
    private TextView mPageInfoView;
    private ImageView mPageImageView;

    private PdfController mPdfController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        initOperatorUis();

        try {
            if (savedInstanceState == null) {
                File pdf = getTargetFile(getIntent());
                mPdfController = new PdfController(pdf);
            } else {
                mPdfController = (PdfController) savedInstanceState.
                        getParcelable("PDF_CONTROLLER");
            }
            updateOperatorUis();
        } catch (IOException e) {
            Log.e(PdfViewerActivity.class.getName(), e.getMessage(), e);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("PDF_CONTROLLER", mPdfController);
    }

    private File getTargetFile(Intent intent) {
        String action = getIntent().getAction();
        Uri data = getIntent().getData();
        if (data == null) {
            return new File(getFilesDir(), "sample.pdf");
        }
        return new File(data.getPath());
    }

    private void initOperatorUis() {
        mRewindBtn = (ImageView) findViewById(R.id.rewind_btn);
        mRewindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPdfController.toStartPage()) {
                    try {
                        updateOperatorUis();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        mPrevBtn = (ImageView) findViewById(R.id.prev_btn);
        mPrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPdfController.prevPage()) {
                    try {
                        updateOperatorUis();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        mNextBtn = (ImageView) findViewById(R.id.next_btn);
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPdfController.nextPage()) {
                    try {
                        updateOperatorUis();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        mForwardBtn = (ImageView) findViewById(R.id.forward_btn);
        mForwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPdfController.toEndPage()) {
                    try {
                        updateOperatorUis();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });

        mPageInfoView = (TextView) findViewById(R.id.page_info_view);
        mPageImageView = (ImageView) findViewById(R.id.page_image_view);
    }

    private void updateOperatorUis() throws IOException {
        if (mPdfController == null || mPdfController.getPageNum() == 0) {
            mPageInfoView.setText("0/0");
            return;
        }

        Bitmap bmp = mPdfController.loadPageBitmap();
        mPageImageView.setImageBitmap(bmp);

        String pageText = String.format("%d/%d", mPdfController.getCurrentPage(),
                mPdfController.getPageNum());
        mPageInfoView.setText(pageText);

        mRewindBtn.setEnabled(mPdfController.getCurrentPage() != 1);
        mNextBtn.setEnabled(mPdfController.getCurrentPage() != mPdfController.getPageNum());
    }
}
