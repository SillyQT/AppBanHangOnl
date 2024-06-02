package com.example.manager.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.manager.Interface.ItemClickListener;
import com.example.manager.R;
import com.example.manager.activity.ProductDetailsActivity;
import com.example.manager.model.EventBus.SuaXoaEvent;
import com.example.manager.model.ProductModel;
import com.example.manager.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    Context context;
    List<ProductModel> arr;

    public ProductAdapter(Context context, List<ProductModel> arr) {
        this.context = context;
        this.arr = arr;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_new, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductModel productModel = arr.get(position);
        holder.textViewName.setText(productModel.getTenSP());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.textViewPrice.setText("Giá: " +decimalFormat.format(Double.parseDouble(productModel.getGiaSP()))+" VND");
        if (productModel.getHinhAnh().contains("http"))
        {
            Glide.with(context).load(productModel.getHinhAnh()).into(holder.imageViewImage);
        }
        else
        {
            String hinh = Utils.BASE_URL + "images/" + productModel.getHinhAnh();
            Glide.with(context).load(hinh).into(holder.imageViewImage);
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent = new Intent(context, ProductDetailsActivity.class);

                    intent.putExtra("data", productModel);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                else
                {
                    EventBus.getDefault().postSticky(new SuaXoaEvent(productModel));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {
        TextView textViewPrice, textViewName;
        ImageView imageViewImage;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPrice = itemView.findViewById(R.id.item_productPrice);
            textViewName = itemView.findViewById(R.id.item_productName);
            imageViewImage = itemView.findViewById(R.id.item_productImage);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            contextMenu.add(0, 0, getAdapterPosition(), "Sửa");
            contextMenu.add(0, 1, getAdapterPosition(), "Xóa");
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), true);
            return false;
        }
    }
}
