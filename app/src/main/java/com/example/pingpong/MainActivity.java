package com.example.pingpong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    Game game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout layout = findViewById(R.id.myLayout);
        game = new Game();
        DrawView drawView = new DrawView(this, game);
        layout.addView(drawView);
        Client client = new Client();
        client.execute();
    }
    class Client extends AsyncTask<Void, Void, Void> {
        String dstAddress = "192.168.100.4"; // adres IP serwera
        int dstPort = 8080;
        String response = "TU BĘDZIE WIADOMOŚĆ Z SERWERA";
        String previousOutMsg = "", currentOutMsg = "";
        public PrintWriter out;
        @Override
        protected Void doInBackground(Void... arg0) {
            Socket socket = null;
            try {
                System.out.println("Try to create socket!");
                socket = new Socket(dstAddress, dstPort);
                System.out.println("created!");
                ByteArrayOutputStream byteArrayOutputStream = new
                        ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];
                int bytesRead;
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();

                while(true){
                    System.out.println("Started new cycle!");
                    currentOutMsg = String.valueOf(game.getMyOffset());
                    out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(outputStream)), true);
                    //if(currentOutMsg != previousOutMsg){
                        out.println(currentOutMsg);
                        System.out.println(currentOutMsg);
                        previousOutMsg = currentOutMsg;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            byteArrayOutputStream.write(buffer, 0, bytesRead);
                            response = byteArrayOutputStream.toString("UTF-8");
                            System.out.println(response);
                            game.updateGameState(response);
                            // send the message to the server
                        }
                    //}
                }

            } catch (Exception e) { e.printStackTrace(); }
            finally {
                if (socket != null) {
                    try { socket.close(); }
                    catch (IOException e) { e.printStackTrace(); }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }
}