package jav.app.monthlygrandtest1.RecyclerAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import jav.app.monthlygrandtest1.Model.Products;
import jav.app.monthlygrandtest1.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private OnProductClickListener monProductClickListener;
    private List<Products> userList;

    public RecyclerViewAdapter() {
    }

    public RecyclerViewAdapter(Context context, OnProductClickListener monProductClickListener, List<Products> userList) {
        this.context = context;
        this.monProductClickListener = monProductClickListener;
        this.userList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_product_row,parent,false);
        return new ViewHolder(view,context,monProductClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Products product = userList.get(position);

        holder.productName.setText(""+product.title);
        if (product.description.length()>16){
            holder.productDescription.setText(""+product.description.substring(0,15)+".....");
        }else {
            holder.productDescription.setText(""+product.description);
        }
        holder.productPrice.setText("Rs: "+product.price);
        holder.rate.setText(""+product.getRate());
        holder.count.setText("("+product.getCount()+")");
        Glide.with(context)
                .load(product.image)
                .placeholder(R.drawable.ic_baseline_image_24)
                .fitCenter()
                .error(R.drawable.ic_baseline_image_24)
                .into(holder.productImageView);
    }

    @Override
    public int getItemCount() {
        Log.d("size",""+userList.size());
        return userList.size();
    }

    public void filterList(List<Products> filteredList) {
        userList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        
        private final OnProductClickListener onUserClickListener;

        ImageView productImageView;
        TextView productName,productDescription,productPrice,serialNumber,rate,count;

        public ViewHolder(@NonNull View itemView, Context context, OnProductClickListener onProductClickListener) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageList);
            productName = itemView.findViewById(R.id.productNameList);
            productDescription = itemView.findViewById(R.id.productDescriptionTextList);
            productPrice = itemView.findViewById(R.id.productPrice);
            rate = itemView.findViewById(R.id.rate);
            count = itemView.findViewById(R.id.count);
            this.onUserClickListener = onProductClickListener;

            itemView.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View v) {
            Products product = userList.get(getAdapterPosition());
            onUserClickListener.onProductClick(product);
        }
    }

    public interface OnProductClickListener{
        void onProductClick(Products position);
    }

}
