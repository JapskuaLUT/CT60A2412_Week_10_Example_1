package com.example.week10example1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.week10example1.adapter.MenuAdapter;
import com.example.week10example1.model.MenuItem;
import com.example.week10example1.util.ErrorHandler;
import com.example.week10example1.util.JsonUtil;

public class MainActivity extends AppCompatActivity implements MenuAdapter.OnItemClickListener {
    private static final String TAG = "MainActivity";

    // UI components
    private TextView restaurantNameTextView;
    private TextView restaurantDescriptionTextView;
    private RecyclerView menuRecyclerView;

    // Data
    private JsonUtil.MenuData menuData;
    private MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        initializeViews();

        // Load and display menu data
        loadMenuData();
    }

    /**
     * Initialize UI components
     */
    private void initializeViews() {
        restaurantNameTextView = findViewById(R.id.textViewRestaurantName);
        restaurantDescriptionTextView = findViewById(R.id.textViewRestaurantDescription);
        menuRecyclerView = findViewById(R.id.recyclerViewMenu);

        // Set up RecyclerView
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuRecyclerView.setHasFixedSize(true);
    }

    /**
     * Load menu data from JSON file
     */
    private void loadMenuData() {
        try {
            // Load menu data from JSON file
            menuData = JsonUtil.loadMenuData(this, R.raw.menu_data);

            // Display restaurant info
            if (menuData.getRestaurant() != null) {
                restaurantNameTextView.setText(menuData.getRestaurant().getName());
                restaurantDescriptionTextView.setText(menuData.getRestaurant().getDescription());
            }

            // Create and set adapter
            menuAdapter = new MenuAdapter(
                    this,
                    menuData.getCategories(),
                    menuData.getMenuItems(),
                    this
            );
            menuRecyclerView.setAdapter(menuAdapter);

        } catch (Exception e) {
            Log.e(TAG, "Error loading menu data", e);
            ErrorHandler.handleError(
                    this,
                    e,
                    "Failed to load menu data. Please try again later."
            );
        }
    }

    /**
     * Handle menu item click
     */
    @Override
    public void onItemClick(int position) {
        try {
            MenuItem menuItem = menuAdapter.getMenuItem(position);
            if (menuItem != null) {
                // Launch detail activity
                Intent intent = new Intent(this, MenuDetailActivity.class);
                intent.putExtra(MenuDetailActivity.EXTRA_MENU_ITEM, menuItem);

                // Find category name for this item
                for (int i = 0; i < menuData.getCategories().size(); i++) {
                    if (menuData.getCategories().get(i).getId() == menuItem.getCategoryId()) {
                        intent.putExtra(MenuDetailActivity.EXTRA_CATEGORY_NAME,
                                menuData.getCategories().get(i).getName());
                        break;
                    }
                }

                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error handling item click", e);
            ErrorHandler.handleError(
                    this,
                    e,
                    "Failed to open item details. Please try again."
            );
        }
    }
}