package com.cyberviy.ViyP.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cyberviy.ViyP.AESUtils;
import com.cyberviy.ViyP.R;
import com.cyberviy.ViyP.models.ViyCred;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {
    private List<ViyCred> credsList = new ArrayList<>();
    private onItemClickListener listener;

    @NotNull
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items, parent, false);

        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        ViyCred creds = credsList.get(position);
        holder.provider.setText(creds.getProviderName());
        try {
            String dec = creds.getEmail();
            String decEmail = AESUtils.decrypt(dec);
            holder.cat1.setText(decEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //holder.cat1.setText(creds.getEmail());
        //holder.cat2.setText(creds.getCat2());
    }

    @Override
    public int getItemCount() {
        return credsList.size();
    }

    public void setCreds(List<ViyCred> credsList) {
        this.credsList = credsList;
        notifyDataSetChanged();
    }

    public ViyCred getCredAt(int position) {
        return credsList.get(position);
    }

    class viewHolder extends RecyclerView.ViewHolder {
        private TextView provider, cat1, cat2;

        public viewHolder(View view) {
            super(view);
            provider = view.findViewById(R.id.provider);
            //Email field
            cat1 = view.findViewById(R.id.imp_cat);
            //cat2 = view.findViewById(R.id.imp_cat2);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onItemClick(credsList.get(pos));
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(ViyCred viyCred);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }
}