package com.example.week10example1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.week10example1.model.MenuItem;
import com.example.week10example1.util.ErrorHandler;

public class MenuDetailActivity extends AppCompatActivity {
    private static final String TAG = "MenuDetailActivity";

    // Intent extras
    public static final String EXTRA_MENU_ITEM = "extra_menu_item";
    public static final String EXTRA_CATEGORY_NAME = "extra_category_name";

    // UI components
    private ImageView itemImageView;
    private TextView itemNameTextView;
    private TextView itemPriceTextView;
    private TextView itemCategoryTextView;
    private TextView itemDescriptionTextView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);

        // Initialize UI components
        initializeViews();

        // Display menu item details
        displayMenuItemDetails();

        // Set up back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Initialize UI components
     */
    private void initializeViews() {
        itemImageView = findViewById(R.id.imageViewDetail);
        itemNameTextView = findViewById(R.id.textViewDetailName);
        itemPriceTextView = findViewById(R.id.textViewDetailPrice);
        itemCategoryTextView = findViewById(R.id.textViewDetailCategory);
        itemDescriptionTextView = findViewById(R.id.textViewDetailDescription);
        backButton = findViewById(R.id.buttonBack);
    }

    /**
     * Display menu item details
     */
    private void displayMenuItemDetails() {
        try {
            // Get menu item from intent
            MenuItem menuItem = (MenuItem) getIntent().getSerializableExtra(EXTRA_MENU_ITEM);
            String categoryName = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);

            if (menuItem == null) {
                throw new IllegalArgumentException("Menu item not found");
            }

            // Display menu item details
            itemNameTextView.setText(menuItem.getName());
            itemPriceTextView.setText(menuItem.getFormattedPrice());
            itemDescriptionTextView.setText(menuItem.getDescription());

            // Display category name if available
            if (categoryName != null && !categoryName.isEmpty()) {
                itemCategoryTextView.setText(categoryName);
            } else {
                itemCategoryTextView.setText("Unknown Category");
            }

            // Load image
            try {
                int imageResourceId = ErrorHandler.getDrawableResourceId(
                        this, menuItem.getImageFileName(), android.R.drawable.ic_menu_gallery);
                itemImageView.setImageResource(imageResourceId);
            } catch (Exception e) {
                // If all else fails, set a background color
                itemImageView.setBackgroundColor(
                        getResources().getColor(android.R.color.darker_gray));
                Log.e(TAG, "Error loading image", e);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error displaying menu item details", e);
            ErrorHandler.handleError(
                    this,
                    e,
                    "Failed to display item details. Please try again."
            );
            // Finish activity if we can't display details
            finish();
        }
    }
}
