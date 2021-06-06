package com.example.foodcall.Order;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodcall.R;

import java.util.List;

public class RecyclerView_Order_History extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "RecyclerV_OrderHist";

    private List<Order> items;
    Boolean vendor_trigger = false;
    private Context context;

    //Used to format text for table
    TableRow.LayoutParams textViewParam = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT, 1.0f);

    //Used to format rows for table
    TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT);

    public RecyclerView_Order_History(List<Order> items, Context context) {
        this.items = items;
        this.context = context;
        this.vendor_trigger = false;
    }

    public Boolean getVendor_trigger() {
        return vendor_trigger;
    }

    public void setVendor_trigger(Boolean vendor_trigger) {
        this.vendor_trigger = vendor_trigger;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_item_order_history, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        RecyclerView_Order_History.ViewHolder temp = (RecyclerView_Order_History.ViewHolder) holder;
        //15:00 in video
        ((ViewHolder) holder).order_date.setText(items.get(position).getOrderDate());
        ((ViewHolder) holder).order_address.setText(items.get(position).getOrder_address());
        if (vendor_trigger) {
            ((ViewHolder) holder).res_name.setText(items.get(position).getCustomer_name());
        } else
            ((ViewHolder) holder).res_name.setText(items.get(position).getRestaurant_name());
        ((ViewHolder) holder).total_price.setText(items.get(position).getOrder_total());

        int count = 0;
        count = items.get(position).getItems().size();

        for (int i = 0; i < count; i++) {
            TableRow header = new TableRow(context);

            TextView temp1 = new TextView(context);
            temp1.setText(items.get(position).getItems().get(i).name);
            Log.d(TAG, i + "Name: " + items.get(position).getItems().get(i).name);
            //temp1.setTypeface(null, Typeface.BOLD);
            temp1.setGravity(Gravity.CENTER);
            temp1.setLayoutParams(textViewParam);

            TextView temp2 = new TextView(context);
            temp2.setText(items.get(position).getItems().get(i).price);
            Log.d(TAG, i + "Price: " + items.get(position).getItems().get(i).price);
            //temp2.setTypeface(null, Typeface.BOLD);
            temp2.setGravity(Gravity.CENTER);
            temp2.setLayoutParams(textViewParam);

            TextView temp3 = new TextView(context);
            Log.d(TAG, i + "Quantity: " + items.get(position).getItems().get(i).quantity);
            temp3.setText(Integer.toString(items.get(position).getItems().get(i).quantity));
            //temp3.setTypeface(null, Typeface.BOLD);
            temp3.setGravity(Gravity.CENTER);
            temp3.setLayoutParams(textViewParam);

            header.addView(temp1);
            header.addView(temp2);
            header.addView(temp3);

            //order_items.removeAllViews();
            ((ViewHolder) holder).order_items.addView(header);
        }

        //((ViewHolder) holder).order_date.setText(items.get(position).orderDate);


//        ((RecyclerView_Order_History.ViewHolder) holder).counter.setText(Integer.toString(count.get(position)));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView order_date;
        TextView order_address;
        TextView res_name;
        TextView total_price;
        TableLayout order_items;
        ConstraintLayout layout_Parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order_date = itemView.findViewById(R.id.order_date);
            order_address = itemView.findViewById(R.id.order_address);
            res_name = itemView.findViewById(R.id.rest_name);
            total_price = itemView.findViewById(R.id.order_price);
            order_items = itemView.findViewById(R.id.order_items);
            layout_Parent = itemView.findViewById(R.id.const_order);

            TableRow header = new TableRow(context);

            TextView temp1 = new TextView(context);
            temp1.setText("Name");
            temp1.setTypeface(null, Typeface.BOLD);
            temp1.setGravity(Gravity.CENTER);
            temp1.setLayoutParams(textViewParam);

            TextView temp2 = new TextView(context);
            temp2.setText("Price / Serving");
            temp2.setTypeface(null, Typeface.BOLD);
            temp2.setGravity(Gravity.CENTER);
            temp2.setLayoutParams(textViewParam);

            TextView temp3 = new TextView(context);
            temp3.setText("Quantity");
            temp3.setTypeface(null, Typeface.BOLD);
            temp3.setGravity(Gravity.CENTER);
            temp3.setLayoutParams(textViewParam);

            header.addView(temp1);
            header.addView(temp2);
            header.addView(temp3);

            order_items.removeAllViews();
            order_items.addView(header);
        }
    }
}

