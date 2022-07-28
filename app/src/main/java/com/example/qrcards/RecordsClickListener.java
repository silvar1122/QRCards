package com.example.qrcards;

import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;

public interface RecordsClickListener {
    void onclick(Record record);
    void onLongClick(Record record, CardView container);
}
