package com.unioncloud.newpay.data.repository.print.datasource;

import android.graphics.Bitmap;
import android.os.SystemClock;
import android.view.View;

import com.pax.api.PrintException;
import com.pax.api.PrintManager;

/**
 * Created by cwj on 16/8/18.
 */
public class PrinterUtils {
    public static final int PRINTER_BUSY = 1;              // 打印机正忙
    public static final int OUT_OF_PAPER = 2;              // 无纸
    public static final int DATA_PACKET_ERROR = 3;         // 打印数据封包错误
    public static final int PRINTER_PROBLEMS = 4;          // 打印机故障
    public static final int PRINTER_OVER_HEATING = 8;      // 打印机过热
    public static final int PRINT_UNFINISHED = -16;        // 打印未完成
    public static final int LACK_OF_FONT = -4;             // 死锁
    public static final int PACKAGE_TOO_LONG = -2;         // 打印数据过多
    public static final int ERROR_UNSUPPORTED_ENCODING = 97;   // 不支持的编码集
    public static final int ERR_NULL_POINT = 98;           // 空指针
    public static final int CONN_ERROR = 99;               // 未知错误

    public static int printBitmap(Bitmap bitmap) {
        PrintManager printManager;
        try {
            printManager = PrintManager.getInstance();
            printManager.prnInit();
            printManager.prnSetGray(100);
            printManager.prnBitmap(bitmap);
            printManager.prnStart();
            byte ret1;
            while(true) {
                ret1 = PrintManager.getInstance().prnStatus();
                if(ret1 != 1) {
                    return ret1;
                }
                SystemClock.sleep(200L);
            }
        } catch (PrintException e) {
            return e.exceptionCode;
        }
    }

    private static int getPrinterState() {
        try {
            PrintManager printManager = PrintManager.getInstance();
            return printManager.prnStatus();
        } catch (PrintException e) {
            return e.exceptionCode;
        }
    }

    public static int printView(View view) {
        Bitmap bitmap = convertViewToBitmap(view);
        try {
            return printBitmap(bitmap);
        } finally {
            // bitmap.recycle();
            bitmap = null;
        }
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }
}
