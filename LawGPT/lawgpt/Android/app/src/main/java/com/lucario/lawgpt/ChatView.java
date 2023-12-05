package com.lucario.lawgpt;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatView extends AppCompatActivity {
    private final int SENT_BY_USER = 1;
    private final int SENT_BY_BOT = 0;
    private RecyclerView chatRecyclerView;
    private ImageButton microphoneSendButton;
    private EditText userMessageField;
    private TextView recordTime;
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .build();

    private String responseText = "";
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    private boolean isMicrophone = true;

    private long recordStartTime = 0;
    private ChatViewAdapter chatViewAdapter;

    private ArrayList<Message> messageList;
    private Drawable sendIconDrawable, microphoneIconDrawable;

    private JSONArray messageArray;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_view);
        messageArray = new JSONArray();
        messageList = new ArrayList<>();
//        messageList.add(new Message("Hi", SENT_BY_USER));
        chatRecyclerView = findViewById(R.id.recycler_view);
        microphoneSendButton = findViewById(R.id.send_button);
        userMessageField = findViewById(R.id.message_edit_text);
        recordTime = findViewById(R.id.record_time);
        chatViewAdapter = new ChatViewAdapter(messageList, "Ronith");
        chatRecyclerView.setAdapter(chatViewAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(llm);

        sendIconDrawable = ContextCompat.getDrawable(this, R.drawable.send_icon);
        microphoneIconDrawable = ContextCompat.getDrawable(this, R.drawable.black_mic);

        microphoneSendButton.setOnLongClickListener(v -> {
            recordStartTime = System.currentTimeMillis();
            userMessageField.setHint("");
            recordTime.setVisibility(View.VISIBLE);
            recordTime.setText("1");
            return true; // Return true to indicate that long click is consumed
        });
        userMessageField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    microphoneSendButton.setImageDrawable(sendIconDrawable);
                    isMicrophone = false;
                } else {
                    isMicrophone = true;
                    microphoneSendButton.setImageDrawable(microphoneIconDrawable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        microphoneSendButton.setOnClickListener(e->{
            String text = userMessageField.getText().toString();
            userMessageField.setText("");
            new Thread(() -> {
                sendMessage(text);
            }).start();
        });
    }

    private void sendMessage(String message){
        addToChat(message, SENT_BY_USER);
        JSONObject obj = new JSONObject();
        try {
            obj.put("role", "user");
            obj.put("content", message);
            messageArray.put(obj);
            System.out.println(messageArray.toString());
            String response = callAPI();
            System.out.println(response);
            messageArray.put(new JSONObject().put("role", "assistant").put("content", response));
        } catch (JSONException e) {
            System.out.println("some error");
            throw new RuntimeException(e);
        }
    }

    private String callAPI() throws JSONException {
        System.out.println("hello from call");
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("messages",messageArray);
        RequestBody body = RequestBody.create(jsonBody.toString(),JSON);
        Request request = new Request.Builder()
                .url("https://law.lucario.site/chat")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("d");
                responseText = "Failed to get response";
                addToChat(responseText, SENT_BY_BOT);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONObject finalJsonObject = jsonObject;
                        String result = jsonObject.getString("content");
                        responseText = result.trim();
                        addToChat(responseText, SENT_BY_BOT);
                    } catch (JSONException e) {
                        System.out.println("DED");
                        e.printStackTrace();
                    }
                }else{
                    responseText = "Failed to load response due to "+response.body().string();
                    addToChat(responseText, SENT_BY_BOT);
                }
            }
        });
        return responseText;
    }

    void addToChat(String message, int sentBy) {
        runOnUiThread(() -> {
            messageList.add(new Message(message, sentBy));
            chatViewAdapter.notifyDataSetChanged();
            chatRecyclerView.smoothScrollToPosition(chatViewAdapter.getItemCount());
        });
    }

    private String timeToString(int startTime, int stopTime){
        long elapsedTime = stopTime - startTime;
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timeFormatted = dateFormat.format(new Date(elapsedTime));
        return timeFormatted;
    }
}
