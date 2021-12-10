package jav.app.monthlygrandtest1.RecyclerAdapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import jav.app.monthlygrandtest1.R;

public class RecyclerViewAdapterCategory extends RecyclerView.Adapter<RecyclerViewAdapterCategory.ViewHolder> {
    private Context context;
    private OnCategoryClickListener monCategoryClickListener;
    private List<String> categoryList;
    private int selectedCategoryPosition = 0;
    boolean flag;

    public RecyclerViewAdapterCategory() {
    }

    public RecyclerViewAdapterCategory(Context context, OnCategoryClickListener onCategoryClickListener, List<String> userList) {
        this.context = context;
        this.monCategoryClickListener = onCategoryClickListener;
        this.categoryList = userList;
        this.selectedCategoryPosition = 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_category_row,parent,false);
        return new ViewHolder(view,context,monCategoryClickListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String category = categoryList.get(position);
//        recyclerViewAdapter.filterList(filteredList);
        if (selectedCategoryPosition == position){
            holder.category.setTextColor(context.getColor(R.color.white));
            holder.category.setBackground(context.getDrawable(R.drawable.rectangle_unfill));
        }else {
            holder.category.setText(category);
            holder.category.setTextColor(context.getColor(R.color.orange_500));
            holder.category.setBackground(context.getDrawable(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnCategoryClickListener onUserClickListener;
        TextView category;

        public ViewHolder(@NonNull View itemView, Context context, OnCategoryClickListener onProductClickListener) {
            super(itemView);

            category = itemView.findViewById(R.id.category_textView);

            this.onUserClickListener = onProductClickListener;
            itemView.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View v) {

            String category = categoryList.get(getAdapterPosition());

            onUserClickListener.onCategoryClick(category,selectedCategoryPosition,itemView);
            selectedCategoryPosition = getAdapterPosition();
            notifyDataSetChanged();
        }
    }

    public interface OnCategoryClickListener{
        void onCategoryClick(String category,int position,View view);
    }

}
