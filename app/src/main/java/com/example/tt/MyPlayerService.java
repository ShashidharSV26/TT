package com.example.tt;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class MyPlayerService extends Service {

    DatagramSocket socket;
    String receivedMessage;
    public static final String BROADCAST_ACTION = "com.example.tt.MESSAGE_RECEIVED";
    private static final int PORT = 8181;
    public MyPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Handler h=new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    socket = new DatagramSocket(PORT);
                    byte[] buffer;
                    DatagramPacket packet;
                    while (true) {
                        buffer = new byte[1024];
                        packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        receivedMessage = new String(packet.getData(), 0, packet.getLength());
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                int keyCode=Integer.parseInt(receivedMessage);
                                Intent intent = new Intent();
                                intent.setAction(BROADCAST_ACTION);
                                intent.putExtra("code", keyCode);
                                sendBroadcast(intent);
                            }
                        });
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    }
