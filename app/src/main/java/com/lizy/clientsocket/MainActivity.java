package com.lizy.clientsocket;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    TextView serverMessage;
    Thread m_objectThread;
    Socket clientSocket;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverMessage = (TextView) findViewById(R.id.server_message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Start (View view){
        m_objectThread = new Thread(new Runnable() {
            public void run() {
                try{
                    clientSocket = new Socket("127.0.0.1",2001);

                    ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                    oos.writeObject("Hello there");
                    Message serverMessage = Message.obtain();

                    ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                    String strMessage = (String) ois.readObject();
                    serverMessage.obj = strMessage;

                    mHandler.sendMessage(serverMessage);
                    oos.close();
                    ois.close();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            messageDisplay(msg.obj.toString());
        }
    };
    public void messageDisplay(String servermessage){
        serverMessage.setText(""+servermessage);
    }
}
