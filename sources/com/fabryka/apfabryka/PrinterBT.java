package com.fabryka.apfabryka;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class PrinterBT {
    private static BluetoothAdapter bluetoothAdapter;
    private static BluetoothDevice bluetoothDevice;
    private static boolean results;
    BluetoothSocket bluetoothSocket;
    InputStream inputStream;
    String msgtext;
    OutputStream outputStream;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    Thread thread;

    // Добавьте константы для запроса разрешений
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_PERMISSIONS = 2;

    public static boolean CheckPrint(Context context, String android_idi) {
        BluetoothDevice pairedDev = null;
        try {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAdapter = defaultAdapter;
            if (defaultAdapter == null) {
                results = false;
                return results;
            }

            // Проверка и запрос разрешений
            if (ContextCompat.checkSelfPermission(context, "android.permission.BLUETOOTH_CONNECT") != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((android.app.Activity) context, new String[]{"android.permission.BLUETOOTH_CONNECT"}, REQUEST_PERMISSIONS);
                results = false;
                return results;
            }

            if (!defaultAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                ((android.app.Activity) context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }

            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
            if (pairedDevice.size() > 0) {
                Iterator<BluetoothDevice> it = pairedDevice.iterator();
                while (it.hasNext()) {
                    pairedDev = it.next();
                    if (pairedDev.getName().equals("XP-460B") || pairedDev.getName().equals("XP-365B") || pairedDev.getName().equals("MHT-L58D")) {
                        break;
                    }
                }
                bluetoothDevice = pairedDev;
                results = true;
            } else {
                results = false;
            }
        } catch (Exception e) {
            results = false;
        }
        return results;
    }

    void FindBluetoothDevice(Context context) {
        BluetoothDevice pairedDev = null;
        try {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Log.i("myData", "Ищу адаптер");
            if (bluetoothAdapter == null) {
                Log.i("myData", "Не нашел");
                return;
            }

            // Проверка и запрос разрешений
            if (ContextCompat.checkSelfPermission(context, "android.permission.BLUETOOTH_CONNECT") != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((android.app.Activity) context, new String[]{"android.permission.BLUETOOTH_CONNECT"}, REQUEST_PERMISSIONS);
                return;
            }

            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                ((android.app.Activity) context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }

            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
            if (pairedDevice.size() > 0) {
                Iterator<BluetoothDevice> it = pairedDevice.iterator();
                while (it.hasNext()) {
                    pairedDev = it.next();
                    if (pairedDev.getName().equals("XP-460B") || pairedDev.getName().equals("XP-365B") || pairedDev.getName().equals("MHT-L58D")) {
                        break;
                    }
                }
                bluetoothDevice = pairedDev;
                Log.i("myData", pairedDev.getName() + " Подключаю");
            }
            Log.i("myData", "Подключил и жду");
        } catch (Exception ex) {
            Log.i("myData", "Ищу адаптер и свалился в ошибку");
            ex.printStackTrace();
        }
    }

    void openBluetoothPrinter(Context context) throws IOException {
        try {
            UUID uuidString = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

            // Проверка разрешений
            if (ContextCompat.checkSelfPermission(context, "android.permission.BLUETOOTH_CONNECT") != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((android.app.Activity) context, new String[]{"android.permission.BLUETOOTH_CONNECT"}, REQUEST_PERMISSIONS);
                return;
            }

            BluetoothSocket socket = bluetoothDevice.createRfcommSocketToServiceRecord(uuidString);
            this.bluetoothSocket = socket;
            socket.connect();
            this.outputStream = this.bluetoothSocket.getOutputStream();
            this.inputStream = this.bluetoothSocket.getInputStream();
            beginListenData();
        } catch (Exception e) {
            Log.e("BluetoothError", "Ошибка при подключении к принтеру", e);
        }
    }

    void beginListenData() {
        try {
            final Handler handler = new Handler();
            this.stopWorker = false;
            this.readBufferPosition = 0;
            this.readBuffer = new byte[1024];
            Thread thread2 = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted() && !this.stopWorker) {
                    try {
                        int byteAvailable = this.inputStream.available();
                        if (byteAvailable > 0) {
                            byte[] packetByte = new byte[byteAvailable];
                            this.inputStream.read(packetByte);
                            for (int i = 0; i < byteAvailable; i++) {
                                byte b = packetByte[i];
                                if (b == (byte) 10) {
                                    byte[] encodedByte = new byte[this.readBufferPosition];
                                    System.arraycopy(this.readBuffer, 0, encodedByte, 0, encodedByte.length);
                                    new String(encodedByte, "CP866");
                                    this.readBufferPosition = 0;
                                    handler.post(() -> {});
                                } else {
                                    this.readBuffer[this.readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (Exception e) {
                        this.stopWorker = true;
                    }
                }
            });
            this.thread = thread2;
            thread2.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void printText(String text, Context context) throws IOException {
        try {
            this.msgtext = text;
            String str = this.msgtext + "\n";
            this.msgtext = str;
            this.outputStream.write(str.getBytes());
            Log.i("my_tag", "Печатаю текст: " + this.msgtext);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void disconnectBT() throws IOException {
        try {
            this.stopWorker = true;
            this.outputStream.close();
            this.inputStream.close();
            this.bluetoothSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}