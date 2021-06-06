package com.example.foodcall.Menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodcall.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Menu_Screen extends ArrayAdapter {

    int mResource;
    private Context context;
    List<menu_class> menuClassList = new ArrayList<>();

    public Adapter_Menu_Screen(@NonNull Context context, int resource, @NonNull List<menu_class> objects) {
        super(context, resource, objects);
        this.context = getContext();
        this.mResource = resource;
        menuClassList = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolderItem viewHolder = new ViewHolderItem();

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(mResource, parent, false);

            viewHolder.Item = (TextView) convertView.findViewById(R.id.itemname);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.selected = (CheckBox) convertView.findViewById(R.id.checkBox2);


            viewHolder.selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "Checkbox " + position + " clicked!", Toast.LENGTH_SHORT).show();

                    if (!menuClassList.get(position).getSelected()) {
                        menuClassList.get(position).setSelected(true);
                    }
                    else
                        menuClassList.get(position).setSelected(false);
                }
            });


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }
        menu_class temp = menuClassList.get(position);
        viewHolder.Item.setText(temp.getItemName());
        viewHolder.price.setText(temp.getPrice());
        return convertView;
    }


    @Override
    public int getCount() {
        return menuClassList.size();
    }

    @Override
    public Object getItem(int position) {
        return menuClassList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    static class ViewHolderItem {
        TextView Item;
        TextView price;
        CheckBox selected;

    }
}

