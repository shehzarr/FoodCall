package com.example.foodcall;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodcall.Menu.Menu;
import com.example.foodcall.Restaurant.Data;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerView_CustomerView extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    public static final String TAG = "RecyclerView_Customer";

    List<Data> list = new ArrayList<>();
    private Filter filter;
    List<Data> filter_list = new ArrayList<>();

    private Context context;

    public RecyclerView_CustomerView(List<Data> list, Context context) {
        this.list = list;
        this.filter_list = list;
        this.context = context;
    }

    //    public RecyclerView_CustomerView(Data_Notification list, Context context) {
////        this.image_Recycle = image_Recycle;
////        this.image_Name = image_Name;
////        this.image_Price = image_Price;
//        this.context = context;
//        this.list = list;
//        this.filter_list = list;
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_item_customer_view, parent, false);
        RecyclerView_CustomerView.ViewHolder holder = new RecyclerView_CustomerView.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        ViewHolder temp = (ViewHolder) holder;

//        byte[] imageBytes = Base64.decode(image_Recycle.get(position), Base64.DEFAULT);
//        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//        temp.image.setImageBitmap(decodedImage);

//        Glide.with(context)
//                .asBitmap()
//                .load(image_Recycle.get(position))
//                .into(temp.image);

        ArrayList<Integer> image_Recycle;
        ArrayList<String> image_Name;
        ArrayList<String> image_Price;

        final Data obj = filter_list.get(position);

        Log.d(TAG, "image loading with URI: " + obj.getImage_Recycle());
        Picasso.get().load(obj.getImage_Recycle()).into(((ViewHolder) holder).image);
//        Glide.with(context).load(obj.getImage_Recycle()).into(((ViewHolder) holder).image);
        ((ViewHolder) holder).res_name.setText(obj.getName());
        ((ViewHolder) holder).delivery_price.setText(obj.getPrice());
        ((ViewHolder) holder).layout_Parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "on Click: clicked on: " + obj.getName());

                View temp = holder.itemView;
                Intent i = new Intent(context, Menu.class);
                i.putExtra("uid", obj.getVendor_UID());
                i.putExtra("name", obj.getName());
//                i.putExtra("contact", image_Contact.get(position));
//                i.putExtra("email", image_Email.get(position));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filter_list.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new StudentsFilter();
        }
        return filter;
    }

    private class StudentsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Data> filteredList = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getName().contains(constraint.toString())) {
                        filteredList.add(list.get(i));
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;

            } else {
                results.count = list.size();
                results.values = list;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filter_list = (ArrayList<Data>) results.values;
            notifyDataSetChanged();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView res_name;
        TextView delivery_price;
        ConstraintLayout layout_Parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.res_img);
            res_name = itemView.findViewById(R.id.res_name);
            delivery_price = itemView.findViewById(R.id.res_del);
            layout_Parent = itemView.findViewById(R.id.parent);
        }
    }
}
