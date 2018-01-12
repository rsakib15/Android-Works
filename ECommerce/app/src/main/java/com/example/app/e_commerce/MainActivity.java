package com.example.app.e_commerce;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProductAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar appBar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(appBar);

        ArrayList<ProductEntry> products = readProductsList();
        ImageRequester imageRequester = ImageRequester.getInstance(this);

        ProductEntry headerProduct = getHeaderProduct(products);
        NetworkImageView headerImage = (NetworkImageView) findViewById(R.id.app_bar_image);

        imageRequester.setImageFromUrl(headerImage, headerProduct.url);


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.product_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.column_count)));


        adapter = new ProductAdapter(products, imageRequester);
        recyclerView.setAdapter(adapter);

        BottomNavigationView bottomNavigation =
                (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        LinearLayoutManager layoutManager =
                                (LinearLayoutManager) recyclerView.getLayoutManager();
                        layoutManager.scrollToPositionWithOffset(0, 0);

                        shuffleProducts();
                        return true;
                    }
                });





        bottomNavigation.setOnNavigationItemReselectedListener(
                new BottomNavigationView.OnNavigationItemReselectedListener() {
                    @Override
                    public void onNavigationItemReselected(@NonNull MenuItem item) {
                        LinearLayoutManager layoutManager =
                                (LinearLayoutManager) recyclerView.getLayoutManager();
                        layoutManager.scrollToPositionWithOffset(0, 0);
                    }
                });



        if (savedInstanceState == null) {
            bottomNavigation.setSelectedItemId(R.id.category_home);
        }

    }


    private ArrayList<ProductEntry> readProductsList() {
        InputStream inputStream = getResources().openRawResource(R.raw.product);
        Type productListType = new TypeToken<ArrayList<ProductEntry>>() {}.getType();
        try {
            return JsonR.readJsonStream(inputStream, productListType);
        }

        catch (IOException e) {
            Log.e("Array List", "Error reading JSON product list", e);
            return new ArrayList<>();
        }
    }

    private ProductEntry getHeaderProduct(List<ProductEntry> products) {
        if (products.size() == 0) {
            throw new IllegalArgumentException("There must be at least one product");
        }

        for (int i = 0; i < products.size(); i++) {
            if ("Perfect Goldfish Bowl".equals(products.get(i).title)) {
                return products.get(i);
            }
        }
        return products.get(0);
    }


    private static final class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder>{
        private List<ProductEntry> products;
        private final ImageRequester imageRequester;

        ProductAdapter(List<ProductEntry> products, ImageRequester imageRequester) {
            this.products = products;
            this.imageRequester = imageRequester;
        }

        void setProducts(List<ProductEntry> products) {
            this.products = products;
            notifyDataSetChanged();
        }

        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ProductViewHolder(viewGroup);
        }

        @Override
        public void onBindViewHolder(ProductViewHolder viewHolder, int i) {
            viewHolder.bind(products.get(i), imageRequester);
        }

        @Override
        public int getItemCount() {
            return products.size();
        }
    }


    private static final class ProductViewHolder extends RecyclerView.ViewHolder {
        private final NetworkImageView imageView;
        private final TextView priceView;

        ProductViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.product_entry, parent, false));
            imageView = (NetworkImageView) itemView.findViewById(R.id.image);
            priceView = (TextView) itemView.findViewById(R.id.price);

            itemView.setOnClickListener(clickListener);
        }

        private final View.OnClickListener clickListener =
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProductEntry product = (ProductEntry) v.getTag(R.id.product_entry);

                    }
                };

        void bind(ProductEntry product, ImageRequester imageRequester) {
            itemView.setTag(R.id.product_entry, product);
            imageRequester.setImageFromUrl(imageView, product.url);
            priceView.setText(product.price);
        }
    }

    private void shuffleProducts() {
        ArrayList<ProductEntry> products = readProductsList();
        Collections.shuffle(products);
        adapter.setProducts(products);
    }


}
