package com.fabryka.apfabryka;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class WebPingPrint {
    public static boolean CheckWebPrint(final Context context, final String myPrintUrl) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String content = WebPingPrint.getContent(myPrintUrl);
                    if (!content.isEmpty()) {
                        PrinterBT pbtr = new PrinterBT();
                        pbtr.FindBluetoothDevice(context);
                        pbtr.openBluetoothPrinter(context);
                        pbtr.printText(content + "\n", context);
                        pbtr.disconnectBT();
                    }
                } catch (Exception e) {
                }
            }
        }).start();
        return true;
    }

    /* access modifiers changed from: private */
    public static String getContent(String path) throws IOException {
        BufferedReader reader = null;
        InputStream stream = null;
        HttpsURLConnection connection = null;
        try {
            HttpsURLConnection connection2 = (HttpsURLConnection) new URL(path).openConnection();
            connection2.setRequestMethod("GET");
            connection2.setReadTimeout(10000);
            connection2.connect();
            InputStream stream2 = connection2.getInputStream();
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(stream2));
            StringBuilder buf = new StringBuilder();
            while (true) {
                String readLine = reader2.readLine();
                String line = readLine;
                if (readLine == null) {
                    break;
                }
                buf.append(line);
                buf.append("\n");
            }
            String sb = buf.toString();
            reader2.close();
            if (stream2 != null) {
                stream2.close();
            }
            if (connection2 != null) {
                connection2.disconnect();
            }
            return sb;
        } catch (Throwable th) {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
            throw th;
        }
    }
}
