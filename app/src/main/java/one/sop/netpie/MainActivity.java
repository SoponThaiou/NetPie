package one.sop.netpie;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.netpie.microgear.Microgear;
import io.netpie.microgear.MicrogearEventListener;

public class MainActivity extends AppCompatActivity {

    private Microgear microgear = new Microgear(this);
    private String appid = "Metheepittaya";
    private String key = "J9Kf8JeV6ikpYU5";
    private String secret = "TAreTCkEdswAtxJgnDrJjjlmn";
    private String alias = "FromAndroidDevice";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey");
            TextView myTextView = (TextView) findViewById(R.id.textView);
            myTextView.append("\n" + string);
        }   //handleMessage
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonSend = (Button) findViewById(R.id.buttonSend);
        final EditText editTextSend = (EditText) findViewById(R.id.editTextSend);
        final Button buttonChat = (Button) findViewById(R.id.buttonChat);
        final EditText editTextChat = (EditText) findViewById(R.id.editTextChat);

        MicrogearCallBack callback = new MicrogearCallBack();
        microgear.connect(appid, key, secret, alias);
        microgear.setCallback(callback);
        microgear.subscribe("Topictest");
        /*(new Thread(new Runnable() {
            int count = 1;

            @Override
            public void run() {
                while (!Thread.interrupted())
                    try {
                        runOnUiThread(new Runnable() // start actions in UI thread
                        {
                            @Override
                            public void run() {
                                microgear.publish("Topictest", String.valueOf(count) + ".  Test message");
                                count++;
                            }
                        });
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // ooops
                    }
            }
        })).start();*/

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                microgear.publish("Topictest", String.valueOf(editTextSend.getText()));
                editTextSend.setText("");
            }
        });

        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                microgear.chat("esp8266",String.valueOf(editTextChat.getText()));
                editTextChat.setText("");
            }
        });

    }   //conCreate

    protected void onDestroy() {
        super.onDestroy();
        microgear.disconnect();
    }   //onDestroy

    protected void onResume() {
        super.onResume();
        microgear.bindServiceResume();
    }   //onDestroy

    class MicrogearCallBack implements MicrogearEventListener {
        @Override
        public void onConnect() {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Now I'm connected with netpie");
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("Connected", "Now I'm connected with netpie 555");
        }

        @Override
        public void onMessage(String topic, String message) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", topic + " : " + message);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("Message", topic + " : " + message);
        }

        @Override
        public void onPresent(String token) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "New friend Connect :" + token);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("present", "New friend Connect :" + token);
        }

        @Override
        public void onAbsent(String token) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Friend lost :" + token);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("absent", "Friend lost :" + token);
        }

        @Override
        public void onDisconnect() {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Disconnected");
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("disconnect", "Disconnected");
        }

        @Override
        public void onError(String error) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Exception : " + error);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("exception", "Exception : " + error);
        }

        @Override
        public void onInfo(String info) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Exception : " + info);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("info", "Info : " + info);
        }
    }   //Class MicrogearCallback
}
