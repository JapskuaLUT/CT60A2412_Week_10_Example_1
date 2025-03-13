package com.example.week10example1.util;

import android.content.Context;
import android.util.Log;

import com.example.week10example1.model.Category;
import com.example.week10example1.model.MenuItem;
import com.example.week10example1.model.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    private static final String TAG = "JsonUtil";

    // Loads and parses the menu data from a JSON file
    public static MenuData loadMenuData(Context context, int resourceId) throws IOException, JSONException {
        // Read the JSON file content
        String jsonContent = readJsonFile(context, resourceId);

        // Parse the JSON content
        JSONObject jsonObject = new JSONObject(jsonContent);

        // Create the MenuData container
        MenuData menuData = new MenuData();

        // Parse restaurant info
        if (jsonObject.has("restaurant")) {
            JSONObject restaurantJson = jsonObject.getJSONObject("restaurant");
            Restaurant restaurant = new Restaurant();

            if (restaurantJson.has("name")) {
                restaurant.setName(restaurantJson.getString("name"));
            } else {
                Log.w(TAG, "Restaurant name is missing in JSON");
            }

            if (restaurantJson.has("description")) {
                restaurant.setDescription(restaurantJson.getString("description"));
            } else {
                Log.w(TAG, "Restaurant description is missing in JSON");
            }

            menuData.setRestaurant(restaurant);
        } else {
            Log.w(TAG, "Restaurant information is missing in JSON");
            // Provide default restaurant info
            menuData.setRestaurant(new Restaurant("Restaurant", "No description available"));
        }

        // Parse categories
        List<Category> categories = new ArrayList<>();
        if (jsonObject.has("categories")) {
            JSONArray categoriesArray = jsonObject.getJSONArray("categories");
            for (int i = 0; i < categoriesArray.length(); i++) {
                try {
                    JSONObject categoryJson = categoriesArray.getJSONObject(i);
                    Category category = new Category();

                    if (categoryJson.has("id")) {
                        category.setId(categoryJson.getInt("id"));
                    } else {
                        throw new JSONException("Category ID is required");
                    }

                    if (categoryJson.has("name")) {
                        category.setName(categoryJson.getString("name"));
                    } else {
                        throw new JSONException("Category name is required");
                    }

                    categories.add(category);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing category at index " + i, e);
                    // Skip this category and continue with others
                }
            }
        } else {
            Log.w(TAG, "Categories array is missing in JSON");
        }
        menuData.setCategories(categories);

        // Build category map for easier lookup
        Map<Integer, String> categoryMap = new HashMap<>();
        for (Category category : categories) {
            categoryMap.put(category.getId(), category.getName());
        }

        // Parse menu items
        List<MenuItem> menuItems = new ArrayList<>();
        if (jsonObject.has("menuItems")) {
            JSONArray menuItemsArray = jsonObject.getJSONArray("menuItems");
            for (int i = 0; i < menuItemsArray.length(); i++) {
                try {
                    JSONObject itemJson = menuItemsArray.getJSONObject(i);
                    MenuItem menuItem = new MenuItem();

                    if (itemJson.has("id")) {
                        menuItem.setId(itemJson.getInt("id"));
                    } else {
                        throw new JSONException("Menu item ID is required");
                    }

                    if (itemJson.has("name")) {
                        menuItem.setName(itemJson.getString("name"));
                    } else {
                        throw new JSONException("Menu item name is required");
                    }

                    if (itemJson.has("description")) {
                        menuItem.setDescription(itemJson.getString("description"));
                    } else {
                        menuItem.setDescription("No description available");
                        Log.w(TAG, "Description missing for item " + menuItem.getId());
                    }

                    if (itemJson.has("price")) {
                        menuItem.setPrice(itemJson.getDouble("price"));
                    } else {
                        menuItem.setPrice(0.0);
                        Log.w(TAG, "Price missing for item " + menuItem.getId());
                    }

                    if (itemJson.has("categoryId")) {
                        int categoryId = itemJson.getInt("categoryId");
                        menuItem.setCategoryId(categoryId);
                        // Validate that the category exists
                        if (!categoryMap.containsKey(categoryId)) {
                            Log.w(TAG, "Invalid category ID " + categoryId + " for item " + menuItem.getId());
                        }
                    } else {
                        throw new JSONException("Menu item category ID is required");
                    }

                    if (itemJson.has("imageFileName")) {
                        menuItem.setImageFileName(itemJson.getString("imageFileName"));
                    } else {
                        menuItem.setImageFileName("placeholder_food");
                        Log.w(TAG, "Image file name missing for item " + menuItem.getId());
                    }

                    menuItems.add(menuItem);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing menu item at index " + i, e);
                    // Skip this item and continue with others
                }
            }
        } else {
            Log.w(TAG, "Menu items array is missing in JSON");
        }
        menuData.setMenuItems(menuItems);

        return menuData;
    }

    // Helper method to read a JSON file from resources
    private static String readJsonFile(Context context, int resourceId) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = context.getResources().openRawResource(resourceId);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading JSON file", e);
            throw e;
        }
        return stringBuilder.toString();
    }

    // Container class for all menu data
    public static class MenuData {
        private Restaurant restaurant;
        private List<Category> categories;
        private List<MenuItem> menuItems;

        public Restaurant getRestaurant() {
            return restaurant;
        }

        public void setRestaurant(Restaurant restaurant) {
            this.restaurant = restaurant;
        }

        public List<Category> getCategories() {
            return categories;
        }

        public void setCategories(List<Category> categories) {
            this.categories = categories;
        }

        public List<MenuItem> getMenuItems() {
            return menuItems;
        }

        public void setMenuItems(List<MenuItem> menuItems) {
            this.menuItems = menuItems;
        }

        // Get menu items for a specific category
        public List<MenuItem> getMenuItemsByCategory(int categoryId) {
            List<MenuItem> items = new ArrayList<>();
            if (menuItems != null) {
                for (MenuItem item : menuItems) {
                    if (item.getCategoryId() == categoryId) {
                        items.add(item);
                    }
                }
            }
            return items;
        }
    }
}
