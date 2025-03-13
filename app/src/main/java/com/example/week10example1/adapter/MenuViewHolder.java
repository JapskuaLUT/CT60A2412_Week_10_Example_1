package com.example.week10example1.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.week10example1.R;

/**
 * ViewHolder for menu items in the RecyclerView
 */
public class MenuViewHolder {

    /**
     * ViewHolder for category header items
     */
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryNameTextView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.textViewCategoryName);
        }

        public void bind(String categoryName) {
            categoryNameTextView.setText(categoryName);
        }
    }

    /**
     * ViewHolder for menu item entries
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView itemImageView;
        private final TextView itemNameTextView;
        private final TextView itemPriceTextView;
        private final TextView itemDescriptionTextView;

        public ItemViewHolder(@NonNull View itemView, final MenuAdapter.OnItemClickListener listener) {
            super(itemView);

            itemImageView = itemView.findViewById(R.id.imageViewMenuItem);
            itemNameTextView = itemView.findViewById(R.id.textViewItemName);
            itemPriceTextView = itemView.findViewById(R.id.textViewItemPrice);
            itemDescriptionTextView = itemView.findViewById(R.id.textViewItemDescription);

            // Set click listener for the entire item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });
        }

        public ImageView getItemImageView() {
            return itemImageView;
        }

        public TextView getItemNameTextView() {
            return itemNameTextView;
        }

        public TextView getItemPriceTextView() {
            return itemPriceTextView;
        }

        public TextView getItemDescriptionTextView() {
            return itemDescriptionTextView;
        }
    }
}
