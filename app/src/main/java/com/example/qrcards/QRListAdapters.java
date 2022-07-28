package com.example.qrcards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QRListAdapters extends RecyclerView.Adapter<QRListAdapters.viewHolder> {

    Context context;
    List<Record> list;
    RecordsClickListener recordsClickListener;


    public QRListAdapters(Context context, List<Record> list, RecordsClickListener recordsClickListener) {
        this.context = context;
        this.list = list;
        this.recordsClickListener = recordsClickListener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Record model=list.get(position);
        holder.full_name.setText(model.getName());
        holder.full_name.setSelected(true);
        holder.date.setText(model.getDate());
        holder.arrival_delay.setText(model.getState());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordsClickListener.onclick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                recordsClickListener.onLongClick(list.get(holder.getAdapterPosition()),holder.container);
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void FilteredList(List<Record> filtered_list){
        list=filtered_list;
        notifyDataSetChanged();
    }

    class viewHolder extends RecyclerView.ViewHolder {

       TextView full_name,date,arrival_delay;
       CardView container;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            full_name=itemView.findViewById(R.id.full_name);
            date=itemView.findViewById(R.id.date);
            arrival_delay=itemView.findViewById(R.id.arrival_departure);
            container=itemView.findViewById(R.id.card_container);
        }
    }
}
