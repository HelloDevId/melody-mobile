package com.melodybeauty.melody_mobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.melodybeauty.melody_mobile.AuthServices.AuthServices;
import com.melodybeauty.melody_mobile.DetailProduct;
import com.melodybeauty.melody_mobile.Model.Product;
import com.melodybeauty.melody_mobile.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.VH> {

    private List<Product> productList;
    private Context context;

    public ProductAdapter(List<Product> productList, Context context){
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_product, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.VH holder, int position) {
        Product product = productList.get(position);
        if (product == null) {
            holder.price.setText(productList.get(position).getPrice());
            holder.jumlah.setText(productList.get(position).getJumlahTerjual());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            String formattedDate = dateFormat.format(productList.get(position).getCreatedAt());
            holder.date.setText(formattedDate);
            holder.name.setText(productList.get(position).getName());
            holder.desc.setText(productList.get(position).getDescription());
            Glide.with(context).load(AuthServices.getImageProduct() + productList.get(position).getImage()).into(holder.img);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, DetailProduct.class);
                        intent.putExtra("data", productList.get(pos));
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return productList.size() == 0 ? 0:productList.size();
    }

    public class VH extends RecyclerView.ViewHolder{
        ImageView logo,img;
        TextView price,jumlah,date,name,desc;
        public VH(@NonNull View itemView){
            super(itemView);
            logo = itemView.findViewById(R.id.logo);
            img = itemView.findViewById(R.id.pimage);
            price = itemView.findViewById(R.id.pprice);
            jumlah = itemView.findViewById(R.id.pjumlah);
            date = itemView.findViewById(R.id.ptanggal);
            name = itemView.findViewById(R.id.pname);
            desc = itemView.findViewById(R.id.pdesc);

            desc.setMaxLines(1);
            desc.setEllipsize(TextUtils.TruncateAt.END);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                logo.setBackgroundResource(R.drawable.bg_logo);
            }
        }
    }

}
