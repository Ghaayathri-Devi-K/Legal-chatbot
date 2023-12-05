package com.lucario.lawgpt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatViewAdapter extends RecyclerView.Adapter<ChatViewAdapter.ChatViewHolder>{
    List<Message> messageList;
    String userName;
    public ChatViewAdapter(List<Message> messageList, String userName){
        this.messageList = messageList;
        this.userName = userName;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_items,null);
        return new ChatViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position){
        Message message = messageList.get(position);
//        holder.profileImageView.setImageDrawable();
        if(message.sentBy() == 0){
            holder.profileNameTV.setText("LawGPT");
        } else {
            holder.profileNameTV.setText(userName);
        }
        holder.messageTV.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    public class ChatViewHolder extends RecyclerView.ViewHolder{
        ImageView profileImageView;
        TextView profileNameTV, messageTV;

        public ChatViewHolder(@NonNull View itemView){
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImage);
            profileNameTV = itemView.findViewById(R.id.profileName);
            messageTV = itemView.findViewById(R.id.message_text);
        }
    }
}
