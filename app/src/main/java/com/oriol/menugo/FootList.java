package com.oriol.menugo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.oriol.menugo.Common.Common;
import com.oriol.menugo.Database.Database;
import com.oriol.menugo.Model.Food;
import com.oriol.menugo.Model.Order;
import com.oriol.menugo.ViewHolder.FoodViewHolder;
import com.oriol.menugo.ViewHolder.MenuViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FootList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;
    String categoryId;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    //Search Functionality
    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    //Favorites
    Database localDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_list);

        database = FirebaseDatabase.getInstance("https://menugo-9451c-default-rtdb.europe-west1.firebasedatabase.app/");
        foodList = database.getReference("Food");

        //Local DB
        localDB = new Database(this);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(getIntent() != null){
            categoryId = getIntent().getStringExtra("CategoryId");
        }
        if(!categoryId.isEmpty() && categoryId != null){

            if(Common.isConnectedToInternet(getBaseContext())) {
                loadListFood(categoryId);
            } else
            {
                Toast.makeText(FootList.this, R.string.connection, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //Search
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter your food");

        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<String> suggest = new ArrayList<>();

                for(String search:suggestList)
                {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                    {
                        suggest.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled)
                {
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //When search finish show results
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {

        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class, R.layout.food_item, FoodViewHolder.class, foodList.orderByChild("name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
                foodViewHolder.food_name.setText(food.getName());
                Picasso.with(getBaseContext()).load(food.getImage()).into(foodViewHolder.food_image);

                final Food local = food;
                foodViewHolder.setItemClickListener(new FoodViewHolder.ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new activity
                        Intent foodDetail = new Intent(FootList.this, FoodDetails.class);
                        //Sending the food
                        foodDetail.putExtra("foodId", searchAdapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadSuggest() {
        foodList.orderByChild("menuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot:snapshot.getChildren())
                {
                    Food item = postSnapshot.getValue(Food.class);
                    suggestList.add(item.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //Lista de comida
    private void loadListFood(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("menuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_image);


                //Quick cart
                viewHolder.quick_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Database(getBaseContext()).addToCart(new Order(
                                adapter.getRef(position).getKey(),
                                model.getName(),
                                "1",
                                model.getPrice(),
                                model.getDiscount(),
                                model.getImage()
                        ));

                        Toast.makeText(FootList.this, "Añadido el carrito", Toast.LENGTH_SHORT).show();
                    }
                });


                //Add Favorites
                if(localDB.isFavorite(adapter.getRef(position).getKey()))
                {
                    viewHolder.fav_image.setImageResource(R.drawable.baseline_favorite_24);
                }

                //Change status of favorite on click
                viewHolder.fav_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!localDB.isFavorite(adapter.getRef(position).getKey()))
                        {
                            localDB.addToFavorites(adapter.getRef(position).getKey());
                            viewHolder.fav_image.setImageResource(R.drawable.baseline_favorite_24);
                            Toast.makeText(FootList.this, ""+model.getName()+" ha sido añadido a Favoritos", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            localDB.removefromFavorites(adapter.getRef(position).getKey());
                            viewHolder.fav_image.setImageResource(R.drawable.baseline_favorite_border_24);
                            Toast.makeText(FootList.this, ""+model.getName()+" ha sido eliminado de Favoritos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final Food local = model;
                viewHolder.setItemClickListener(new FoodViewHolder.ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new activity
                        Intent foodDetail = new Intent(FootList.this, FoodDetails.class);
                        //Sending the food
                        foodDetail.putExtra("foodId", adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });

            }
        };
        Log.d("TAG",""+ adapter.getItemCount());
        recyclerView.setAdapter(adapter);
    }
}