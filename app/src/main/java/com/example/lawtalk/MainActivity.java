package com.example.lawtalk;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

public class MainActivity extends Navigation_BaseActivity{

    TextView textView;
    EditText editText;
    Button button;

    StreamPlayer streamPlayer;

    private TextToSpeech initTextToSpeechService(){
        TextToSpeech service = new TextToSpeech();
        String username = "afbf1a34-aa29-4b24-b737-e412228acea2";
        String password = "OHexP6LOgaY8";
        service.setUsernameAndPassword(username,password);
        return service;
    }

    TextToSpeech textToSpeech;

    private class WatsonTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... textToSpeak){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("running the Watson thread");
                }
            });


            streamPlayer = new StreamPlayer();
            streamPlayer.playStream(textToSpeech.synthesize(editText.getText().toString(), Voice.EN_MICHAEL).execute());

            return "text to speech done";
        }
        @Override
        protected void onPostExecute(String result){
            textView.setText("TTS status: " + result);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("LawTalk - 首頁");
        setUpAll(toolbar);

        textToSpeech = initTextToSpeechService();

        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("the text to speech: " + editText.getText());
                textView.setText("TTS: " + editText.getText());

                WatsonTask task = new WatsonTask();
                task.execute(new String[]{});
            }
        });

    }
}
