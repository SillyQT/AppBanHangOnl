package com.example.manager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.manager.R;
import com.example.manager.model.CategoryModel;
import com.example.manager.utils.Utils;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    List<CategoryModel> arr;

    public CategoryAdapter(Context context, List<CategoryModel> arr) {
        this.arr = arr;
        this.context = context;
    }

    Context context;

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHoder {
        TextView textViewCategory;
        ImageView imageViewCategory;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder viewHoder = null;
        if (convertView == null) {
            viewHoder = new ViewHoder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_product, null);
            viewHoder.textViewCategory = convertView.findViewById(R.id.item_productname);
            viewHoder.imageViewCategory = convertView.findViewById(R.id.item_image);
            convertView.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) convertView.getTag();
        }
        viewHoder.textViewCategory.setText(arr.get(position).getTenSP());

        if (arr.get(position).getHinhAnh().contains("http")) {
            Glide.with(context).load(arr.get(position).getHinhAnh()).into(viewHoder.imageViewCategory);
        }
        else
        {
            String Img = Utils.BASE_URL + "images/" + arr.get(position).getHinhAnh();
            Glide.with(context).load(Img).into(viewHoder.imageViewCategory);
        }

        // Glide.with(context).load(arr.get(position).getHinhAnh()).into(viewHoder.imageViewCategory);
        return convertView;
    }
}
