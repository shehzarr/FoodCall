package com.example.foodcall.Checkout;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.example.foodcall.R;

import java.util.ArrayList;


public class RecyclerView_Adapter_Checkout extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "Recycler_View";
    private View priceIdentifier;
    private ArrayList<String> itemName;
    private ArrayList<String> itemPrice;
    private ArrayList<Integer> count;
    private Context context;

    public RecyclerView_Adapter_Checkout(ArrayList<String> itemName, ArrayList<String> itemPrice, ArrayList<Integer> count, Context context) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.count = count;
        this.context = context;
    }

    public View getPriceIdentifier() {
        return priceIdentifier;
    }

    public void setPriceIdentifier(View priceIdentifier) {
        this.priceIdentifier = priceIdentifier;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        ViewHolder temp = (ViewHolder) holder;

        ((ViewHolder) holder).text.setText(itemName.get(position));
        ((ViewHolder) holder).counter.setText(Integer.toString(count.get(position)));

        ((ViewHolder) holder).plus.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

        ((ViewHolder) holder).plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View temp = holder.itemView;
                int temp1 = Integer.parseInt((String) ((ViewHolder) holder).counter.getText());
                temp1++;
                ((ViewHolder) holder).counter.setText(Integer.toString(temp1));

                String[] split = itemPrice.get(position).split(" ");
                Checkout.func_add(split[1], position);
            }
        });

        ((ViewHolder) holder).minus.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

        ((ViewHolder) holder).minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View temp = holder.itemView;
                int temp1 = Integer.parseInt((String) ((ViewHolder) holder).counter.getText());
                if (temp1 > 1) {
                    temp1--;
                    ((ViewHolder) holder).counter.setText(Integer.toString(temp1));

                    String[] split = itemPrice.get(position).split(" ");
                    Checkout.func_sub(split[1], position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        TextView price;
        Button plus;
        Button minus;
        TextView counter;
        ConstraintLayout layout_Parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.food_name);
            price = itemView.findViewById(R.id.tot_price);
            layout_Parent = itemView.findViewById(R.id.parent);
            plus = itemView.findViewById(R.id.add);
            minus = itemView.findViewById(R.id.minus);
            counter = itemView.findViewById(R.id.count);
        }
    }
}

