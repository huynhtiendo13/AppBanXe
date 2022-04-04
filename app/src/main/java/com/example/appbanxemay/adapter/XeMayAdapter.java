package com.example.appbanxemay.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanxemay.Interface.ItemClickListener;
import com.example.appbanxemay.R;
import com.example.appbanxemay.activity.ChiTietActivity;
import com.example.appbanxemay.model.SanPhamMoi;

import java.text.DecimalFormat;
import java.util.List;

public class XeMayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPhamMoi> array;
    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    public XeMayAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xemay,parent, false);
            return new MyViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  MyViewHolder){
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            SanPhamMoi sanPham = array.get(position);
            myViewHolder.tensanpham.setText(sanPham.getTensanpham().trim());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            myViewHolder.giasanpham.setText("Giá: "+decimalFormat.format(Double.parseDouble(sanPham.getGiasanpham()))+"Đ");
            myViewHolder.motasanpham.setText(sanPham.getMotasanpham());
            Glide.with(context).load(sanPham.getHinhanhsanpham()).into(myViewHolder.hinhanhsanpham);
            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if (!isLongClick){
                        Intent intent = new Intent(context, ChiTietActivity.class);
                        intent.putExtra("chitiet",sanPham);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }
        else{
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return array.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);

        }
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tensanpham, giasanpham, motasanpham;
        ImageView hinhanhsanpham;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tensanpham = itemView.findViewById(R.id.itemxm_ten);
            giasanpham = itemView.findViewById(R.id.itemxm_gia);
            motasanpham = itemView.findViewById(R.id.itemxm_mota);
            hinhanhsanpham = itemView.findViewById(R.id.itemxm_image);
            itemView.setOnClickListener(this);
       }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);

        }
    }

}
