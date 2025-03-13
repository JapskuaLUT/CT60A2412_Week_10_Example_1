package com.example.week10example1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.week10example1.R;
import com.example.week10example1.model.Category;
import com.example.week10example1.model.MenuItem;
import com.example.week10example1.util.ErrorHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // Constants for view types
    private static final int VIEW_TYPE_CATEGORY = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    // Interface for item click handling
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final Context context;
    private final List<Object> items; // Combined list of categories and menu items
    private final Map<Integer, Category> categoryMap; // Map of category IDs to category objects
    private final OnItemClickListener listener;

    /**
     * Constructor to initialize the adapter with menu data
     */
    public MenuAdapter(Context context, List<Category> categories, List<MenuItem> menuItems, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.items = new ArrayList<>();
        this.categoryMap = new HashMap<>();

        // Create the category map
        for (Category category : categories) {
            categoryMap.put(category.getId(), category);
        }

        // Organize menu items by category
        Map<Integer, List<MenuItem>> itemsByCategory = new HashMap<>();
        for (MenuItem item : menuItems) {
            int categoryId = item.getCategoryId();
            if (!itemsByCategory.containsKey(categoryId)) {
                itemsByCategory.put(categoryId, new ArrayList<>());
            }
            itemsByCategory.get(categoryId).add(item);
        }

        // Add categories and their items to the combined list
        for (Category category : categories) {
            // Add category header
            items.add(category);

            // Add all items for this category
            List<MenuItem> categoryItems = itemsByCategory.get(category.getId());
            if (categoryItems != null) {
                items.addAll(categoryItems);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= 0 && position < items.size()) {
            if (items.get(position) instanceof Category) {
                return VIEW_TYPE_CATEGORY;
            } else {
                return VIEW_TYPE_ITEM;
            }
        }
        return -1; // Invalid view type
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_CATEGORY) {
            View view = inflater.inflate(R.layout.item_category_header, parent, false);
            return new MenuViewHolder.CategoryViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_menu, parent, false);
            return new MenuViewHolder.ItemViewHolder(view, listener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            int viewType = getItemViewType(position);

            if (viewType == VIEW_TYPE_CATEGORY && holder instanceof MenuViewHolder.CategoryViewHolder) {
                // Bind category data
                Category category = (Category) items.get(position);
                ((MenuViewHolder.CategoryViewHolder) holder).bind(category.getName());
            } else if (viewType == VIEW_TYPE_ITEM && holder instanceof MenuViewHolder.ItemViewHolder) {
                // Bind menu item data
                MenuItem menuItem = (MenuItem) items.get(position);
                MenuViewHolder.ItemViewHolder itemHolder = (MenuViewHolder.ItemViewHolder) holder;

                // Set text fields
                itemHolder.getItemNameTextView().setText(menuItem.getName());
                itemHolder.getItemPriceTextView().setText(menuItem.getFormattedPrice());

                // Truncate description for list view
                String description = menuItem.getDescription();
                if (description.length() > 50) {
                    description = description.substring(0, 47) + "...";
                }
                itemHolder.getItemDescriptionTextView().setText(description);

                // Load image (with error handling)
                try {
                    int imageResourceId = ErrorHandler.getDrawableResourceId(
                            context, menuItem.getImageFileName(), android.R.drawable.ic_menu_gallery);
                    itemHolder.getItemImageView().setImageResource(imageResourceId);
                } catch (Exception e) {
                    // If all else fails, set a background color
                    itemHolder.getItemImageView().setBackgroundColor(
                            context.getResources().getColor(android.R.color.darker_gray));
                    ErrorHandler.logError("MenuAdapter", "Error loading image", e);
                }
            }
        } catch (Exception e) {
            ErrorHandler.logError("MenuAdapter", "Error binding view at position " + position, e);
            // Try to recover gracefully
            if (holder instanceof MenuViewHolder.CategoryViewHolder) {
                ((MenuViewHolder.CategoryViewHolder) holder).bind("Unknown Category");
            } else if (holder instanceof MenuViewHolder.ItemViewHolder) {
                MenuViewHolder.ItemViewHolder itemHolder = (MenuViewHolder.ItemViewHolder) holder;
                itemHolder.getItemNameTextView().setText("Error displaying item");
                itemHolder.getItemPriceTextView().setText("");
                itemHolder.getItemDescriptionTextView().setText("Please try again later");
                itemHolder.getItemImageView().setImageResource(R.drawable.placeholder_food);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    /**
     * Get the menu item at the specified position in the adapter
     * @param position The adapter position
     * @return The MenuItem or null if position is invalid or contains a category
     */
    public MenuItem getMenuItem(int position) {
        if (position >= 0 && position < items.size() && items.get(position) instanceof MenuItem) {
            return (MenuItem) items.get(position);
        }
        return null;
    }
}