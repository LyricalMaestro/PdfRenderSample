package com.lyricaloriginal.pdfrendersample;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.pdf.PdfRenderer;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 表示中のPDFの操作等を行うModeクラスです。
 * <p/>
 * Created by LyricalMaestro on 15/07/30.
 */
public class PdfController implements Parcelable {

    private final File mPdfFile;
    private int mCurrentPage = -1;
    private int mPageNum = -1;

    public PdfController(File pdfFile) throws IOException {
        if (pdfFile == null) {
            throw new IllegalArgumentException("引数がnullです。");
        }
        mPdfFile = pdfFile;

        try (ParcelFileDescriptor fd = ParcelFileDescriptor.open(mPdfFile,
                ParcelFileDescriptor.MODE_READ_ONLY);
             PdfRenderer renderer = new PdfRenderer(fd)) {
            mPageNum = renderer.getPageCount();
            mCurrentPage = 1;
        }
    }

    /**
     * 参照しているpdf ファイル名を取得します。
     *
     * @return 参照中のpdf ファイル名。
     */
    public String getFileName() {
        return mPdfFile.getName();
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public int getPageNum() {
        return mPageNum;
    }

    public boolean nextPage() {
        if (mPageNum <= 0) {
            return false;
        } else if (mCurrentPage < mPageNum) {
            mCurrentPage++;
            return true;
        }
        //  すでに最後のページに達している時
        return false;
    }

    public boolean prevPage() {
        if (mPageNum <= 0) {
            return false;
        } else if (1 < mCurrentPage && mCurrentPage <= mPageNum) {
            mCurrentPage--;
            return true;
        }
        //  すでに最初のページに達している時
        return false;
    }

    public boolean toStartPage() {
        if (mPageNum <= 0) {
            return false;
        }
        mCurrentPage = 1;
        return true;
    }

    public boolean toEndPage() {
        if (mPageNum <= 0) {
            return false;
        }
        mCurrentPage = mPageNum;
        return true;
    }

    public Bitmap loadPageBitmap() throws IOException {
        Bitmap bmp = null;
        try (ParcelFileDescriptor fd = ParcelFileDescriptor.open(mPdfFile,
                ParcelFileDescriptor.MODE_READ_ONLY);
             PdfRenderer renderer = new PdfRenderer(fd);
             PdfRenderer.Page page = renderer.openPage(mCurrentPage - 1)) {

            float pdfWidth = page.getWidth();
            float pdfHeight = page.getHeight();
            Bitmap bitmap = Bitmap.createBitmap((int) pdfWidth, (int) pdfHeight, Bitmap.Config.ARGB_8888);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            bmp = bitmap;
        }
        return bmp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPdfFile.getAbsolutePath());
        dest.writeInt(mCurrentPage);
        dest.writeInt(mPageNum);
    }

    public static final Parcelable.Creator<PdfController> CREATOR
            = new Parcelable.Creator<PdfController>() {
        public PdfController createFromParcel(Parcel in) {
            return new PdfController(in);
        }

        public PdfController[] newArray(int size) {
            return new PdfController[size];
        }
    };

    private PdfController(Parcel in) {
        mPdfFile = new File(in.readString());
        mCurrentPage = in.readInt();
        mPageNum = in.readInt();
    }
}
