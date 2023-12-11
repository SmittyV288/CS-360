package com.cs360.mattsmithsinventoryappcs360;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnItemClickListener listener;
    private OnItemDeleteClickListener deleteListener;

    public ProductAdapter(List<Product> productList, OnItemClickListener listener, OnItemDeleteClickListener deleteListener) {
        this.productList = productList;
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {
        final Product product = productList.get(position);
        holder.textViewProductId.setText(product.getProductId());
        holder.textViewProductDesc.setText(product.getProductDesc());
        holder.textViewProductQuantity.setText(String.valueOf(product.getQuantity()));

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method from the activity or fragment to handle the deletion
                if (deleteListener != null) {
                    deleteListener.onItemDelete(product, position);
                }
            }
        });

        // Set the click listener for the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use the interface to pass the click event up to the Activity
                if (listener != null) {
                    listener.onItemClick(product);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }


    public interface OnItemDeleteClickListener {
        void onItemDelete(Product product, int position);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProductId, textViewProductDesc, textViewProductQuantity;
        Button buttonDelete;

        ProductViewHolder(View itemView) {
            super(itemView);
            textViewProductId = itemView.findViewById(R.id.lblProductId);
            textViewProductDesc = itemView.findViewById(R.id.lblProductDescription);
            textViewProductQuantity = itemView.findViewById(R.id.lblQuantity);
            buttonDelete = itemView.findViewById(R.id.btnDelete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Use the interface to pass the click event up to the Activity
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(productList.get(position));
                    }
                }
            });
        }
    }

    public void removeItem(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productList.size());
    }

}
