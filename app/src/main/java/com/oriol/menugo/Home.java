package com.oriol.menugo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oriol.menugo.Common.Common;
import com.oriol.menugo.Database.Database;
import com.oriol.menugo.Model.Banner;
import com.oriol.menugo.Model.Category;
import com.oriol.menugo.ViewHolder.MenuViewHolder;
import com.oriol.menugo.databinding.ActivityHomeBinding;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

/**
 * @author Oriol
 */
public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Definición variables
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    FirebaseDatabase database;
    DatabaseReference category;
    TextView txtFullName;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;

    //Slider
    HashMap<String, String> image_list;
    SliderLayout sliderLayout;

    CounterFab fab;

    /**
     * Método onCreate de la actividad Home
     * @param savedInstanceState Si la actividad se reinicializa después de
     *      * previamente cerrado, entonces este paquete contiene los datos que más
     *      * suministrado recientemente en {@link #onSaveInstanceState}.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.appBarHome.toolbar.setTitle("Menu");
        setSupportActionBar(binding.appBarHome.toolbar);

        //Firebase
        database = FirebaseDatabase.getInstance("https://menugo-9451c-default-rtdb.europe-west1.firebasedatabase.app/");
        category = database.getReference("Category");

        fab = (CounterFab) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(Home.this, Cart.class);
                startActivity(cartIntent);
            }
        });

        fab.setCount(new Database(this).getCountCart());

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_menu, R.id.nav_orders, R.id.nav_cart, R.id.nav_log_out)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //Nombre Usuario
        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView)headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.current_User.getName());


        //Load Menu
        recycler_menu = (RecyclerView)findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        binding.navView.setNavigationItemSelectedListener(this);

        loadMenu();
        //Setup Slider
        setupSlider();
    }

    /**
     * Método para configurar la slider de la página principal
     */
    private void setupSlider() {
        sliderLayout = (SliderLayout) findViewById(R.id.slider);
        image_list = new HashMap<>();

        DatabaseReference banner = database.getReference("Banner");

        banner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot:snapshot.getChildren())
                {
                    Banner banner = postSnapshot.getValue(Banner.class);
                    image_list.put(banner.getName()+"@@@"+banner.getId(), banner.getImage());
                }
                for (String key:image_list.keySet())
                {
                    //Split by _ to take name and id of the food
                    String[] keySplit = key.split("@@@");
                    String nameOfFood = keySplit[0];
                    String idOfFood = keySplit[1];

                    TextSliderView textSliderView = new TextSliderView(getBaseContext());
                    textSliderView
                            .description(nameOfFood)
                            .image(image_list.get(key))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    Intent intent = new Intent(Home.this, FoodDetails.class);
                                    //Sending food id to FoodDetails
                                    intent.putExtras(textSliderView.getBundle());
                                    startActivity(intent);
                                }
                            });
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("foodId", idOfFood);

                    sliderLayout.addSlider(textSliderView);

                    banner.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);
    }

    /**
     * Método para cargar las distintas categorías
     */
    public void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class, R.layout.menu_item, MenuViewHolder.class, category) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageView);
                final Category clickItem = model;
                viewHolder.setItemClickListener(new MenuViewHolder.ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Consigue el ID de cada categoria y va a cada actividad
                        Intent foodList = new Intent(Home.this, FootList.class);

                        foodList.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }

                });
            }
        };
        recycler_menu.setAdapter(adapter);
    }

    /**
     * Método para controlar cuando para la slider al finalizar las imagenes.
     */
    @Override
    protected void onStop() {
        super.onStop();
        sliderLayout.stopAutoCycle();
    }

    /**
     * Método para mostrar cuantos productos hay en el carrito
     */
    @Override
    protected void onResume() {
        super.onResume();
        fab.setCount(new Database(this).getCountCart());
    }

    /**
     * Método para inflar/crear el menú lateral
     * @param menu Opciones del menú donde ubicar nuestros items.
     *
     * @return devuelve true en caso de crear el menú correctamente.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    /**
     * Método para poder navegar por la página de home
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Método para controlar cual de las opciones del menú eliges.
     * @param item item seleccionado
     * @return devuelve true en caso de que se inicien las activdades correctamente.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_menu)
        {
            
        } else if (id == R.id.nav_cart)
        {
            Intent cartIntent = new Intent(Home.this, Cart.class);
            startActivity(cartIntent);
        } else if (id == R.id.nav_orders)
        {
            Intent orderIntent = new Intent(Home.this, OrderStatus.class);
            startActivity(orderIntent);
        } else if (id == R.id.nav_log_out)
        {
            Intent signIn = new Intent(Home.this, SignIn.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);
        } else if (id == R.id.nav_chgPsswd)
        {
            showChangePasswordDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Método para mostrar el diálogo para cambiar la contraseña
     */
    private void showChangePasswordDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle(R.string.changePassword).setMessage(R.string.info);

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_pwd = inflater.inflate(R.layout.change_password, null);

        MaterialEditText edtPassword = (MaterialEditText) layout_pwd.findViewById(R.id.editPassword);
        MaterialEditText edtNewPassword = (MaterialEditText) layout_pwd.findViewById(R.id.edtNewPassword);
        MaterialEditText edtRepeatPassword = (MaterialEditText) layout_pwd.findViewById(R.id.edtRepeatPassword);

        alertDialog.setView(layout_pwd);

        alertDialog.setPositiveButton(R.string.change, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.app.AlertDialog waitingDialog = new SpotsDialog(Home.this);
                waitingDialog.show();

                if (edtPassword.getText().toString().equals(Common.current_User.getPassword()))
                {
                    if (edtNewPassword.getText().toString().equals(edtRepeatPassword.getText().toString()))
                    {

                        if (isStrongPassword(edtNewPassword.getText().toString()) == false)
                        {

                        }
                        else
                        {
                            Map<String, Object> passwordUpdate = new HashMap<>();

                            passwordUpdate.put("password", edtNewPassword.getText().toString());

                            DatabaseReference user = FirebaseDatabase
                                    .getInstance("https://menugo-9451c-default-rtdb.europe-west1.firebasedatabase.app/")
                                    .getReference("User");

                            user.child(Common.current_User.getPhone()).updateChildren(passwordUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    waitingDialog.dismiss();
                                    Toast.makeText(Home.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Home.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    else
                    {
                        Toast.makeText(Home.this, "Las contraseñas no coincide", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(Home.this, "Contraseña anterior incorrecta", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public boolean isStrongPassword(String password)
    {
        // Minimum length of 8 characters
        if (password.length() < 8) {
            Toast.makeText(this, "La contraseña debe tener mínimo 8 carácteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        // At least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            Toast.makeText(this, "La contraseña debe incluir mínimo una mayúscula", Toast.LENGTH_SHORT).show();
            return false;
        }

        // At least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            Toast.makeText(this, "La contraseña debe incluir mínimo una minúscula", Toast.LENGTH_SHORT).show();
            return false;
        }

        // At least one digit
        if (!password.matches(".*\\d.*")) {
            Toast.makeText(this, "La contraseña debe incluir mínimo un número", Toast.LENGTH_SHORT).show();
            return false;
        }

        // At least one special character
        Pattern specialCharPattern = Pattern.compile("[!@#$%^&*()_+=|<>?{}\\[\\]~-]");
        Matcher specialCharMatcher = specialCharPattern.matcher(password);
        if (!specialCharMatcher.find()) {
            Toast.makeText(this, "La contraseña debe incluir mínimo un carácter especial", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Passed all the checks, password is strong
        return true;
    }
}