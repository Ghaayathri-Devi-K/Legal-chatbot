package com.lucario.lawgpt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends RecyclerView.Adapter<MyPagerAdapter.ViewHolder> {

    private List<Integer> fragmentList;
    private LayoutInflater inflater;

    private ManageDots listener;
    private Context context;
    private Activity activity;

    public MyPagerAdapter(Context context, ManageDots listener, Activity activity) {
        inflater = LayoutInflater.from(context);
        fragmentList = new ArrayList<>();
        // Add your fragment layout IDs to the fragmentList
        fragmentList.add(R.layout.onboarding1_fragment);
        fragmentList.add(R.layout.onboarding2_fragment);
        fragmentList.add(R.layout.onboarding3_fragment);
        fragmentList.add(R.layout.login_activity);
        this.context = context;
        this.listener = listener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position == 3){
            Button button = holder.itemView.findViewById(R.id.loginButton);
            button.setOnClickListener(e->{
                context.startActivity(new Intent(context, ChatView.class));
                this.activity.finish();
            });
        }
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return fragmentList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface ManageDots{
        void removeDots();
        void addDots();
    }
}