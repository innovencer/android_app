package com.advante.golazzos;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.advante.golazzos.Adapters.LeftMenuAdapter;
import com.advante.golazzos.Fragments.Aciertos_Fragment;
import com.advante.golazzos.Fragments.Amigos_Fragment;
import com.advante.golazzos.Fragments.Busqueda_Fragment;
import com.advante.golazzos.Fragments.Cuenta_Fragment;
import com.advante.golazzos.Fragments.Fanaticada_Fragment;
import com.advante.golazzos.Fragments.Favoritos1_Fragment;
import com.advante.golazzos.Fragments.Fragment_Ganadores;
import com.advante.golazzos.Fragments.Golazzos_Fragment;
import com.advante.golazzos.Fragments.Jugadas_Fragment;
import com.advante.golazzos.Fragments.Nivel_Fragmet;
import com.advante.golazzos.Fragments.PartidosPorJugar_Fragment;
import com.advante.golazzos.Fragments.Perfil_Fragment;
import com.advante.golazzos.Fragments.RankingFragment;
import com.advante.golazzos.Fragments.Trofeos_Fragment;
import com.advante.golazzos.Helpers.API;
import com.advante.golazzos.Helpers.CircleTransform;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Helpers.JSONBuilder;
import com.advante.golazzos.Interface.API_Listener;
import com.advante.golazzos.Interface.IGetUser_Listener;
import com.advante.golazzos.Model.LeftMenu_Item;
import com.advante.golazzos.Model.User;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.mlsdev.rximagepicker.RxImageConverters;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

import bolts.AppLinks;
import io.npay.activity.NPay;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Ruben Flores on 4/19/2016.
 */
public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    ListView LeftMenu;
    FragmentManager fragmentManager;
    ImageView imageFanaticada,imagePartidos,imageEquipos,imageRanking,imageAmigos,imageProfile,imgTypeProfile;
    ImageView imageFanaticada1,imagePartidos1,imageEquipos1,imageRanking1,imageAmigos1;
    TextView textFanaticada,textPartidos,textEquipos,textRanking,textAmigos,textName,textPuntos,textNivel;
    TextView textFanaticada1,textPartidos1,textEquipos1,textRanking1,textAmigos1,textPuntosGlobal,textTypeProfile;
    LeftMenuAdapter leftMenuAdapter;
    LinearLayout bottomMenu1, bottomMenu2,linearPuntos, linearTitular;
    TextView textTitle;
    String pic_name;
    General gnr;
    ActionBarDrawerToggle toggle;
    Menu _menu;
    SearchView searchView;
    ImageView searchClose;
    ImageView searchGo;
    ImageView search;

    public String sku;
    public int items_qty = 0;
    int normalHeight = 0;
    boolean no_menu = false;
    NPay npay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer_w);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer_w);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        fragmentManager = getSupportFragmentManager();
        linearPuntos = (LinearLayout) findViewById(R.id.linearPuntos);
        linearTitular = (LinearLayout) findViewById(R.id.linearTitular);
        linearTitular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(8);
                pintarMenu(3);
                textTitle.setText("PERFIL");
                bottomMenu1.setVisibility(View.GONE);
                bottomMenu2.setVisibility(View.GONE);
                no_menu = true;
            }
        });
        gnr = new General(this);

        if(gnr.getLoggedUser() == null){
            gnr.getUser(new IGetUser_Listener() {
                @Override
                public void onComplete(Boolean complete, User user) {

                }
            });
        }

        setTitle("");
        showFragment(1);

        leftMenuAdapter = new LeftMenuAdapter(this, getMenu());
        LeftMenu = (ListView)findViewById(R.id.list_leftmenu_items);
        LeftMenu.setAdapter(leftMenuAdapter);
        LeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                loadProfile();
                no_menu = false;
                switch (i){
                    case 0:
                        showFragment(7);
                        pintarMenu(3);
                        textTitle.setText("PERFIL");
                        bottomMenu1.setVisibility(View.GONE);
                        bottomMenu2.setVisibility(View.GONE);
                        no_menu = true;
                        break;
                    case 1:
                        /*
                        showFragment(0);
                        pintarMenu(1);
                        textTitle.setText("FANATICADA");
                        bottomMenu1.setVisibility(View.VISIBLE);
                        bottomMenu2.setVisibility(View.GONE);
                        */
                        onClickBottomMenu(findViewById(R.id.menuFanaticada));
                        break;
                    case 2:
                        showFragment(1);
                        pintarMenu(1);
                        textTitle.setText("PARTIDOS");
                        bottomMenu1.setVisibility(View.VISIBLE);
                        bottomMenu2.setVisibility(View.GONE);
                        break;
                    case 3:
                        showFragment(11);
                        pintarMenu(4);
                        textTitle.setText("JUGADAS");
                        bottomMenu1.setVisibility(View.GONE);
                        bottomMenu2.setVisibility(View.GONE);
                        no_menu = true;
                        break;
                    case 4:
                        showFragment(8);
                        pintarMenu(3);
                        textTitle.setText("PERFIL");
                        bottomMenu1.setVisibility(View.GONE);
                        bottomMenu2.setVisibility(View.GONE);
                        no_menu = true;
                        break;
                    case 5:
                        showFragment(12);
                        pintarMenu(3);
                        textTitle.setText("GOLAZZOS");
                        bottomMenu1.setVisibility(View.GONE);
                        bottomMenu2.setVisibility(View.GONE);
                        no_menu = true;
                        break;
                    case 6:
                        finish();
                        break;
                }
            }
        });

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        imageProfile =(ImageView) findViewById(R.id.imageProfile);
        imageFanaticada = (ImageView) findViewById(R.id.imageFanaticada);
        imagePartidos = (ImageView) findViewById(R.id.imagePartidos);
        imageEquipos = (ImageView) findViewById(R.id.imageEquipos);
        imageRanking = (ImageView) findViewById(R.id.imageRanking);
        imageAmigos = (ImageView) findViewById(R.id.imageAmigos);

        imageFanaticada1 = (ImageView) findViewById(R.id.imageFanaticada1);
        imagePartidos1 = (ImageView) findViewById(R.id.imagePartidos1);
        imageEquipos1 = (ImageView) findViewById(R.id.imageEquipos1);
        imageRanking1 = (ImageView) findViewById(R.id.imageRanking1);
        imageAmigos1 = (ImageView) findViewById(R.id.imageAmigos1);

        imgTypeProfile = (ImageView) findViewById(R.id.imgTypeProfile);

        textFanaticada = (TextView) findViewById(R.id.textFanaticada);
        textPartidos = (TextView) findViewById(R.id.textPartidos);
        textEquipos = (TextView) findViewById(R.id.textEquipos);
        textRanking = (TextView) findViewById(R.id.textRanking);
        textAmigos = (TextView) findViewById(R.id.textAmigos);
        textTitle = (TextView) findViewById(R.id.toolbar_title);
        textName =  (TextView) findViewById(R.id.textName);
        textPuntos =  (TextView) findViewById(R.id.textPuntos);
        textNivel =  (TextView) findViewById(R.id.textNivel);
        textPuntosGlobal = (TextView) findViewById(R.id.textPuntosGlobal);
        textTypeProfile = (TextView) findViewById(R.id.textTypeProfile);

        textFanaticada1 = (TextView) findViewById(R.id.textFanaticada1);
        textPartidos1 = (TextView) findViewById(R.id.textPartidos1);
        textEquipos1 = (TextView) findViewById(R.id.textEquipos1);
        textRanking1 = (TextView) findViewById(R.id.textRanking1);
        textAmigos1 = (TextView) findViewById(R.id.textAmigos1);

        bottomMenu1 = (LinearLayout) findViewById(R.id.bottomMenu1);
        bottomMenu2 = (LinearLayout) findViewById(R.id.bottomMenu2);

        loadProfile();

        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        npay = new NPay(this);

        final Resources r = getResources();
        final int dp50 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                50,
                r.getDisplayMetrics()
        );

        final FrameLayout view = (FrameLayout) findViewById(R.id.flContent);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            public void onGlobalLayout(){
                if(!no_menu) {
                    if (normalHeight == 0) {
                        normalHeight = view.getHeight();
                    }
                    if (view.getHeight() < normalHeight) {
                        bottomMenu1.setVisibility(View.GONE);
                    } else if (view.getHeight() == normalHeight) {
                        bottomMenu1.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    private void showFragment(int position){
        Fragment fragment = null;
        Class fragmentClass;
        switch(position) {
            case 0:
                fragmentClass = Fanaticada_Fragment.class;
                linearPuntos.setVisibility(View.INVISIBLE);
                break;
            case 1:
                fragmentClass = PartidosPorJugar_Fragment.class;
                linearPuntos.setVisibility(View.VISIBLE);
                break;
            case 2:
                fragmentClass = Favoritos1_Fragment.class;
                linearPuntos.setVisibility(View.INVISIBLE);
                break;
            case 3:
                fragmentClass = RankingFragment.class;
                linearPuntos.setVisibility(View.INVISIBLE);
                break;
            case 4:
                fragmentClass = Nivel_Fragmet.class;
                linearPuntos.setVisibility(View.INVISIBLE);
                break;
            case 5:
                fragmentClass = Amigos_Fragment.class;
                linearPuntos.setVisibility(View.INVISIBLE);
                break;
            case 6:
                fragmentClass = Trofeos_Fragment.class;
                linearPuntos.setVisibility(View.INVISIBLE);
                break;
            case 7:
                fragmentClass = Perfil_Fragment.class;
                linearPuntos.setVisibility(View.INVISIBLE);
                break;
            case 8:
                fragmentClass = Cuenta_Fragment.class;
                linearPuntos.setVisibility(View.INVISIBLE);
                break;
            case 9:
                fragmentClass = Aciertos_Fragment.class;
                linearPuntos.setVisibility(View.INVISIBLE);
                break;
            case 10:
                fragmentClass = Fragment_Ganadores.class;
                linearPuntos.setVisibility(View.INVISIBLE);
                break;
            case 11:
                fragmentClass = Jugadas_Fragment.class;
                linearPuntos.setVisibility(View.VISIBLE);
                break;
            case 12:
                fragmentClass = Golazzos_Fragment.class;
                linearPuntos.setVisibility(View.GONE);
                break;
            default:
                fragmentClass = PartidosPorJugar_Fragment.class;
                linearPuntos.setVisibility(View.VISIBLE);
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void onClickBottomMenu(View v){
        loadProfile();
        switch (v.getId()){
            case R.id.menuFanaticada:
                imageFanaticada.setColorFilter(Color.rgb(223, 253, 0));
                imagePartidos.setColorFilter(Color.rgb(255, 255, 255));
                imageEquipos.setColorFilter(Color.rgb(255, 255, 255));
                imageRanking.setColorFilter(Color.rgb(255, 255, 255));
                imageAmigos.setColorFilter(Color.rgb(255, 255, 255));

                textFanaticada.setTextColor(Color.rgb(223, 253, 0));
                textPartidos.setTextColor(Color.rgb(255, 255, 255));
                textEquipos.setTextColor(Color.rgb(255, 255, 255));
                textRanking.setTextColor(Color.rgb(255, 255, 255));
                textAmigos.setTextColor(Color.rgb(255, 255, 255));

                textTitle.setText("FANATICADA");
                bottomMenu1.setVisibility(View.VISIBLE);
                bottomMenu2.setVisibility(View.GONE);

                showFragment(0);
                pintarMenu(1);
                break;
            case R.id.menuPartidos:
                imageFanaticada.setColorFilter(Color.rgb(255, 255, 255));
                imagePartidos.setColorFilter(Color.rgb(223, 253, 0));
                imageEquipos.setColorFilter(Color.rgb(255, 255, 255));
                imageRanking.setColorFilter(Color.rgb(255, 255, 255));
                imageAmigos.setColorFilter(Color.rgb(255, 255, 255));

                textFanaticada.setTextColor(Color.rgb(255, 255, 255));
                textPartidos.setTextColor(Color.rgb(223, 253, 0));
                textEquipos.setTextColor(Color.rgb(255, 255, 255));
                textRanking.setTextColor(Color.rgb(255, 255, 255));
                textAmigos.setTextColor(Color.rgb(255, 255, 255));

                textTitle.setText("PARTIDOS");
                bottomMenu1.setVisibility(View.VISIBLE);
                bottomMenu2.setVisibility(View.GONE);

                showFragment(1);
                pintarMenu(1);
                break;
            case R.id.menuEquipos:
                imageFanaticada.setColorFilter(Color.rgb(255, 255, 255));
                imagePartidos.setColorFilter(Color.rgb(255, 255, 255));
                imageEquipos.setColorFilter(Color.rgb(223, 253, 0));
                imageRanking.setColorFilter(Color.rgb(255, 255, 255));
                imageAmigos.setColorFilter(Color.rgb(255, 255, 255));

                textFanaticada.setTextColor(Color.rgb(255, 255, 255));
                textPartidos.setTextColor(Color.rgb(255, 255, 255));
                textEquipos.setTextColor(Color.rgb(223, 253, 0));
                textRanking.setTextColor(Color.rgb(255, 255, 255));
                textAmigos.setTextColor(Color.rgb(255, 255, 255));

                textTitle.setText("EQUIPOS");

                showFragment(2);
                pintarMenu(1);
                break;
            case R.id.menuRanking:
                imageFanaticada.setColorFilter(Color.rgb(255, 255, 255));
                imagePartidos.setColorFilter(Color.rgb(255, 255, 255));
                imageEquipos.setColorFilter(Color.rgb(255, 255, 255));
                imageRanking.setColorFilter(Color.rgb(223, 253, 0));
                imageAmigos.setColorFilter(Color.rgb(255, 255, 255));

                textFanaticada.setTextColor(Color.rgb(255, 255, 255));
                textPartidos.setTextColor(Color.rgb(255, 255, 255));
                textEquipos.setTextColor(Color.rgb(255, 255, 255));
                textRanking.setTextColor(Color.rgb(223, 253, 0));
                textAmigos.setTextColor(Color.rgb(255, 255, 255));

                bottomMenu1.setVisibility(View.GONE);
                bottomMenu2.setVisibility(View.VISIBLE);

                _menu.findItem(R.id.menuSalir).setVisible(true);

                pintarMenu(2);

                textTitle.setText("RANKING");

                //toggle.setDrawerIndicatorEnabled(false);
                showFragment(3);
                break;
            case R.id.menuAmigos:
                imageFanaticada.setColorFilter(Color.rgb(255, 255, 255));
                imagePartidos.setColorFilter(Color.rgb(255, 255, 255));
                imageEquipos.setColorFilter(Color.rgb(255, 255, 255));
                imageRanking.setColorFilter(Color.rgb(255, 255, 255));
                imageAmigos.setColorFilter(Color.rgb(223, 253, 0));

                textFanaticada.setTextColor(Color.rgb(255, 255, 255));
                textPartidos.setTextColor(Color.rgb(255, 255, 255));
                textEquipos.setTextColor(Color.rgb(255, 255, 255));
                textRanking.setTextColor(Color.rgb(255, 255, 255));
                textAmigos.setTextColor(Color.rgb(223, 253, 0));

                textTitle.setText("AMIGOS");
                showFragment(5);
                pintarMenu(1);
                break;
            case R.id.menuFanaticada1:
                imageFanaticada1.setColorFilter(Color.rgb(14, 90, 118));
                imagePartidos1.setColorFilter(Color.rgb(191, 191, 191));
                imageEquipos1.setColorFilter(Color.rgb(191, 191, 191));
                imageRanking1.setColorFilter(Color.rgb(191, 191, 191));
                imageAmigos1.setColorFilter(Color.rgb(191, 191, 191));

                textFanaticada1.setTextColor(Color.rgb(14, 90, 118));
                textPartidos1.setTextColor(Color.rgb(191, 191, 191));
                textEquipos1.setTextColor(Color.rgb(191, 191, 191));
                textRanking1.setTextColor(Color.rgb(191, 191, 191));
                textAmigos1.setTextColor(Color.rgb(191, 191, 191));

                textTitle.setText("RANKING");
                showFragment(3);
                break;
            case R.id.menuPartidos1:
                imageFanaticada1.setColorFilter(Color.rgb(191, 191, 191));
                imagePartidos1.setColorFilter(Color.rgb(14, 90, 118));
                imageEquipos1.setColorFilter(Color.rgb(191, 191, 191));
                imageRanking1.setColorFilter(Color.rgb(191, 191, 191));
                imageAmigos1.setColorFilter(Color.rgb(191, 191, 191));

                textFanaticada1.setTextColor(Color.rgb(191, 191, 191));
                textPartidos1.setTextColor(Color.rgb(14, 90, 118));
                textEquipos1.setTextColor(Color.rgb(191, 191, 191));
                textRanking1.setTextColor(Color.rgb(191, 191, 191));
                textAmigos1.setTextColor(Color.rgb(191, 191, 191));

                textTitle.setText("NIVEL");
                showFragment(4);
                break;
            case R.id.menuEquipos1:
                Log.d("Golazz", "Entro");
                imageFanaticada1.setColorFilter(Color.rgb(191, 191, 191));
                imagePartidos1.setColorFilter(Color.rgb(191, 191, 191));
                imageEquipos1.setColorFilter(Color.rgb(14, 90, 118));
                imageRanking1.setColorFilter(Color.rgb(191, 191, 191));
                imageAmigos1.setColorFilter(Color.rgb(191, 191, 191));

                textFanaticada1.setTextColor(Color.rgb(191, 191, 191));
                textPartidos1.setTextColor(Color.rgb(191, 191, 191));
                textEquipos1.setTextColor(Color.rgb(14, 90, 118));
                textRanking1.setTextColor(Color.rgb(191, 191, 191));
                textAmigos1.setTextColor(Color.rgb(191, 191, 191));

                textTitle.setText("TROFEOS");
                showFragment(6);
                break;
            case R.id.menuRanking2:
                imageFanaticada1.setColorFilter(Color.rgb(191, 191, 191));
                imagePartidos1.setColorFilter(Color.rgb(191, 191, 191));
                imageEquipos1.setColorFilter(Color.rgb(191, 191, 191));
                imageRanking1.setColorFilter(Color.rgb(14, 90, 118));
                imageAmigos1.setColorFilter(Color.rgb(191, 191, 191));

                textFanaticada1.setTextColor(Color.rgb(191, 191, 191));
                textPartidos1.setTextColor(Color.rgb(191, 191, 191));
                textEquipos1.setTextColor(Color.rgb(191, 191, 191));
                textRanking1.setTextColor(Color.rgb(14, 90, 118));
                textAmigos1.setTextColor(Color.rgb(191, 191, 191));

                textTitle.setText("ACIERTOS");
                showFragment(9);
                break;
            case R.id.menuAmigos1:
                imageFanaticada1.setColorFilter(Color.rgb(191, 191, 191));
                imagePartidos1.setColorFilter(Color.rgb(191, 191, 191));
                imageEquipos1.setColorFilter(Color.rgb(191, 191, 191));
                imageRanking1.setColorFilter(Color.rgb(191, 191, 191));
                imageAmigos1.setColorFilter(Color.rgb(14, 90, 118));

                textFanaticada1.setTextColor(Color.rgb(191, 191, 191));
                textPartidos1.setTextColor(Color.rgb(191, 191, 191));
                textEquipos1.setTextColor(Color.rgb(191, 191, 191));
                textRanking1.setTextColor(Color.rgb(191, 191, 191));
                textAmigos1.setTextColor(Color.rgb(14, 90, 118));

                textTitle.setText("GANADORES");
                showFragment(10);
                break;
        }
    }

    private ArrayList<LeftMenu_Item> getMenu(){
        ArrayList<LeftMenu_Item> leftMenu_items = new ArrayList<LeftMenu_Item>();
        leftMenu_items.add(new LeftMenu_Item(0, getResources().getString(R.string.menuPerfil)));
        leftMenu_items.add(new LeftMenu_Item(0, getResources().getString(R.string.menuEstadio)));
        leftMenu_items.add(new LeftMenu_Item(0, getResources().getString(R.string.menuPartidos)));
        leftMenu_items.add(new LeftMenu_Item(0, getResources().getString(R.string.menuJugadas)));
        leftMenu_items.add(new LeftMenu_Item(0, getResources().getString(R.string.menuCuenta)));
        //leftMenu_items.add(new LeftMenu_Item(0, getResources().getString(R.string.menuAyuda)));
        leftMenu_items.add(new LeftMenu_Item(0, getResources().getString(R.string.menuGolazzos)));
        leftMenu_items.add(new LeftMenu_Item(0, getResources().getString(R.string.menuSalir)));
        return leftMenu_items;

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        no_menu = true;
        _menu = menu;
        // Associate searchable configuration with the SearchView
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menuItemBuscar));

        searchView.setSubmitButtonEnabled(false);

        search = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchClose = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchGo = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_go_btn);
        search.setImageResource(R.drawable.magnify_white);
        searchClose.setImageResource(R.drawable.ic_clear_white_24dp);
        searchGo.setImageResource(R.drawable.ic_done_white_24dp);
        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Fragment fragment = null;
                Class fragmentClass = Busqueda_Fragment.class;

                try {
                    textTitle.setText("BÚSQUEDA");
                    fragment = (Fragment) fragmentClass.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("search", query);
                    fragment.setArguments(bundle);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                searchView.setIconified(true);
                searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public static void colorizeToolbar(Toolbar toolbarView, int toolbarIconsColor, Activity activity) {
        final PorterDuffColorFilter colorFilter
                = new PorterDuffColorFilter(toolbarIconsColor, PorterDuff.Mode.MULTIPLY);

        for(int i = 0; i < toolbarView.getChildCount(); i++) {
            final View v = toolbarView.getChildAt(i);

            //Step 1 : Changing the color of back button (or open drawer button).
            if(v instanceof ImageButton) {
                //Action Bar back button
                ((ImageButton)v).getDrawable().setColorFilter(colorFilter);
            }

            if(v instanceof ActionMenuView) {
                for(int j = 0; j < ((ActionMenuView)v).getChildCount(); j++) {

                    //Step 2: Changing the color of any ActionMenuViews - icons that
                    //are not back button, nor text, nor overflow menu icon.
                    final View innerView = ((ActionMenuView)v).getChildAt(j);

                    if(innerView instanceof ActionMenuItemView) {
                        int drawablesCount = ((ActionMenuItemView)innerView).getCompoundDrawables().length;
                        for(int k = 0; k < drawablesCount; k++) {
                            if(((ActionMenuItemView)innerView).getCompoundDrawables()[k] != null) {
                                final int finalK = k;

                                //Important to set the color filter in seperate thread,
                                //by adding it to the message queue
                                //Won't work otherwise.
                                innerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((ActionMenuItemView) innerView).getCompoundDrawables()[finalK].setColorFilter(colorFilter);
                                    }
                                });
                            }
                        }
                    }
                }
            }

            //Step 3: Changing the color of title and subtitle.
            toolbarView.setTitleTextColor(toolbarIconsColor);
            toolbarView.setSubtitleTextColor(toolbarIconsColor);

            //Step 4: Changing the color of the Overflow Menu icon.
            setOverflowButtonColor(activity, colorFilter);
        }
    }
    public static void setOverflowButtonColor(final Activity activity, final PorterDuffColorFilter color) {
        final String overflowDescription = activity.getString(R.string.abc_action_menu_overflow_description);
        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        final ViewTreeObserver viewTreeObserver = decorView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final ArrayList<View> outViews = new ArrayList<>();
                decorView.findViewsWithText(outViews, overflowDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                if (outViews.isEmpty()) {
                    return;
                }
                AppCompatImageView overflow = (AppCompatImageView) outViews.get(0);
                overflow.setColorFilter(color);
                removeOnGlobalLayoutListener(decorView, this);
            }
        });
    }
    private static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }
        else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    private void pintarMenu(int opcion){
        ColorDrawable colorDrawable;
        switch (opcion){
            case 1:
                colorDrawable = new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary));
                getSupportActionBar().setBackgroundDrawable(colorDrawable);
                colorizeToolbar(toolbar , ContextCompat.getColor(this, R.color.white),this);
                textTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
                search.setImageResource(R.drawable.magnify_white);
                toggle.setHomeAsUpIndicator(R.drawable.ic_drawer_w);
                toggle.syncState();
                break;
            case 2:
                colorDrawable = new ColorDrawable(ContextCompat.getColor(this, R.color.green));
                getSupportActionBar().setBackgroundDrawable(colorDrawable);
                colorizeToolbar(toolbar , ContextCompat.getColor(this, R.color.colorAccent),this);
                textTitle.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                search.setImageResource(R.drawable.magnify_blue);
                toggle.setHomeAsUpIndicator(R.drawable.ic_drawer_b);
                toggle.syncState();
                break;
            case 3:
                colorDrawable = new ColorDrawable(ContextCompat.getColor(this, R.color.blueSky100));
                getSupportActionBar().setBackgroundDrawable(colorDrawable);
                colorizeToolbar(toolbar , ContextCompat.getColor(this, R.color.colorAccent),this);
                textTitle.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                search.setImageResource(R.drawable.magnify_blue);
                toggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
                toggle.syncState();
                _menu.findItem(R.id.menuSalir).setVisible(false);
                break;
            case 4:
                colorDrawable = new ColorDrawable(ContextCompat.getColor(this, R.color.red));
                getSupportActionBar().setBackgroundDrawable(colorDrawable);
                colorizeToolbar(toolbar , ContextCompat.getColor(this, R.color.white),this);
                textTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
                search.setImageResource(R.drawable.magnify_white);
                toggle.setHomeAsUpIndicator(R.drawable.ic_drawer_w);
                toggle.syncState();
                _menu.findItem(R.id.menuSalir).setVisible(false);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menuSalir:
                imageFanaticada.setColorFilter(Color.rgb(255, 255, 255));
                imagePartidos.setColorFilter(Color.rgb(223, 253, 0));
                imageEquipos.setColorFilter(Color.rgb(255, 255, 255));
                imageRanking.setColorFilter(Color.rgb(255, 255, 255));
                imageAmigos.setColorFilter(Color.rgb(255, 255, 255));

                textFanaticada.setTextColor(Color.rgb(255, 255, 255));
                textPartidos.setTextColor(Color.rgb(223, 253, 0));
                textEquipos.setTextColor(Color.rgb(255, 255, 255));
                textRanking.setTextColor(Color.rgb(255, 255, 255));
                textAmigos.setTextColor(Color.rgb(255, 255, 255));
/*
                pintarMenu(0);

                textTitle.setText("FANATICADA");
                showFragment(0);
*/
                onClickBottomMenu(findViewById(R.id.menuFanaticada));
                bottomMenu1.setVisibility(View.VISIBLE);
                bottomMenu2.setVisibility(View.GONE);
                _menu.findItem(R.id.menuSalir).setVisible(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void loadProfile() {
        if (gnr.getLoggedUser() != null) {
            Picasso.with(this).load(gnr.getLoggedUser().getProfile_pic_url()).transform(new CircleTransform()).into(imageProfile);

            textName.setText("Hola, " + gnr.getLoggedUser().getName().split(" ")[0]);
            String s = String.format("%,d", Math.round(gnr.getLoggedUser().getPoints()));
            textPuntos.setText(s);
            textPuntosGlobal.setText(s + " pts");

            if(gnr.getLoggedUser().getPaid_subscription()){
                textTypeProfile.setText("Titular");
            }else{
                textTypeProfile.setText("Suplente");
            }

            textNivel.setText("" + gnr.getLoggedUser().getLevel().getOrder());
        }
    }
    
    @Override
    public void onAttachFragment(Fragment f) {
        super.onAttachFragment(f);
        gnr.setLastFragment(f);
    }

    @Override
    protected void onDestroy() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }

    @Override
    public void onResume(){
        super.onResume();
        gnr.getUser(new IGetUser_Listener() {
            @Override
            public void onComplete(Boolean complete, User user) {
                if(complete){
                    loadProfile();
                }
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(gnr.getLastFragment() != null){
            if(gnr.getLastFragment() instanceof Fanaticada_Fragment){
                onClickBottomMenu(findViewById(R.id.menuFanaticada));

            }else if(gnr.getLastFragment() instanceof PartidosPorJugar_Fragment){
                onClickBottomMenu(findViewById(R.id.menuPartidos));
            }else if(gnr.getLastFragment() instanceof Favoritos1_Fragment){
                onClickBottomMenu(findViewById(R.id.menuEquipos));
            }else if(gnr.getLastFragment() instanceof RankingFragment){
                onClickBottomMenu(findViewById(R.id.menuRanking));
            }else if (gnr.getLastFragment() instanceof Amigos_Fragment){
                onClickBottomMenu(findViewById(R.id.menuRanking));
            }else if (gnr.getLastFragment() instanceof Nivel_Fragmet){
                onClickBottomMenu(findViewById(R.id.menuPartidos1));
            }else if(gnr.getLastFragment() instanceof Trofeos_Fragment){
                onClickBottomMenu(findViewById(R.id.menuEquipos1));
            }else if(gnr.getLastFragment() instanceof Aciertos_Fragment){
                onClickBottomMenu(findViewById(R.id.menuRanking2));
            }else if(gnr.getLastFragment() instanceof Fragment_Ganadores){
                onClickBottomMenu(findViewById(R.id.menuAmigos1));
            }else{
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            gnr.clearHistoryFragment();
        }else{
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
