package com.bot2shop.model;

import java.util.ArrayList;

public class ShopItem {

    public String name; // Name of product
    public int cost;    // Cost of product (including fractional part)
    public String about; // Short description of item
    public String description; // Big description of item
    public String imageFileName; // Path to product image
    public ArrayList<String> tags; // List of tags

}
