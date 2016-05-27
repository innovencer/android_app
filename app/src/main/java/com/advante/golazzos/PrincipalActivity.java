package com.advante.golazzos;

import android.app.Activity;
import android.content.Context;
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
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
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
import com.advante.golazzos.Fragments.Fragment_Ganadores;
import com.advante.golazzos.Fragments.Nivel_Fragmet;
import com.advante.golazzos.Fragments.PartidosEnVivo_Fragment;
import com.advante.golazzos.Fragments.PartidosFinalizado_Fragment;
import com.advante.golazzos.Fragments.Perfil_Fragment;
import com.advante.golazzos.Fragments.RankingFragment;
import com.advante.golazzos.Fragments.Trofeos_Fragment;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Model.LeftMenu_Item;
import com.advante.golazzos.Fragments.Fanaticada_Fragment;
import com.advante.golazzos.Fragments.Favoritos1_Fragment;
import com.advante.golazzos.Fragments.PartidosPorJugar_Fragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import bolts.AppLinks;
import io.npay.activity.NPay;

/**
 * Created by Ruben Flores on 4/19/2016.
 */
public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    ListView LeftMenu;
    FragmentManager fragmentManager;
    ImageView imageFanaticada,imagePartidos,imageEquipos,imageRanking,imageAmigos,imageProfile;
    ImageView imageFanaticada1,imagePartidos1,imageEquipos1,imageRanking1,imageAmigos1;
    TextView textFanaticada,textPartidos,textEquipos,textRanking,textAmigos,textName,textPuntos,textNivel;
    TextView textFanaticada1,textPartidos1,textEquipos1,textRanking1,textAmigos1,textPuntosGlobal;
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
                Intent intent = new Intent(PrincipalActivity.this, ServiceDetailActivity.class);
                startActivity(intent);
            }
        });
        gnr = new General(this);

        setTitle("");
        showFragment(1);

        leftMenuAdapter = new LeftMenuAdapter(this, getMenu());
        LeftMenu = (ListView)findViewById(R.id.list_leftmenu_items);
        LeftMenu.setAdapter(leftMenuAdapter);
        LeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        showFragment(7);
                        pintarMenu(3);
                        textTitle.setText("PERFIL");
                        bottomMenu1.setVisibility(View.GONE);
                        bottomMenu2.setVisibility(View.GONE);
                        break;
                    case 1:
                        showFragment(0);
                        pintarMenu(1);
                        textTitle.setText("FANATICADA");
                        bottomMenu1.setVisibility(View.VISIBLE);
                        bottomMenu2.setVisibility(View.GONE);
                        break;
                    case 2:
                        showFragment(1);
                        pintarMenu(1);
                        textTitle.setText("PARTIDOS");
                        bottomMenu1.setVisibility(View.VISIBLE);
                        bottomMenu2.setVisibility(View.GONE);
                        break;
                    case 4:
                        showFragment(8);
                        pintarMenu(3);
                        textTitle.setText("PERFIL");
                        bottomMenu1.setVisibility(View.GONE);
                        bottomMenu2.setVisibility(View.GONE);
                        break;
                    case 7:
                        finish();
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
        leftMenu_items.add(new LeftMenu_Item(0, getResources().getString(R.string.menuAyuda)));
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

                pintarMenu(1);

                textTitle.setText("PARTIDOS");
                showFragment(1);

                bottomMenu1.setVisibility(View.VISIBLE);
                bottomMenu2.setVisibility(View.GONE);
                _menu.findItem(R.id.menuSalir).setVisible(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Bitmap bm = bitmap;
            GraphicsUtil graphicUtil = new GraphicsUtil();
            imageProfile.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
            FileOutputStream stream = null;
            File file;
            try {
                file = new File(General.local_dir_images + "profile/");
                if(!file.exists()){
                    file.mkdir();
                }
                stream = new FileOutputStream(General.local_dir_images + "profile/"+pic_name+".png");
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
                stream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {}

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {}
    };

    private void loadProfile(){
        File file = new File(General.local_dir_images + "profile/no_profile.png");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        GraphicsUtil graphicUtil = new GraphicsUtil();
        imageProfile.setImageBitmap(graphicUtil.getCircleBitmap(
                bm, 16));

        textName.setText("Hola, " + gnr.getLoggedUser().getName().split(" ")[0]);
        textPuntos.setText(""+gnr.getLoggedUser().getPoints());
        String s = String.format("%,d", Math.round(gnr.getLoggedUser().getPoints()));
        textPuntosGlobal.setText(s +" pts");
        textNivel.setText(""+gnr.getLoggedUser().getLevel().getOrder());
        if(gnr.getLoggedUser().getProfile_pic_url().contains("facebook.com")) {
            pic_name = "" + gnr.getLoggedUser().getId();

            file = new File(General.local_dir_images + "profile/" + pic_name + ".png");
            if (file.exists()) {
                options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                graphicUtil = new GraphicsUtil();
                imageProfile.setImageBitmap(graphicUtil.getCircleBitmap(
                        bm, 16));
            } else {
                Picasso.with(this)
                        .load(gnr.getLoggedUser().getProfile_pic_url())
                        .into(target);
            }

        }else{
            pic_name = gnr.getLoggedUser().getProfile_pic_url().substring(
                    gnr.getLoggedUser().getProfile_pic_url().lastIndexOf("/"),
                    gnr.getLoggedUser().getProfile_pic_url().lastIndexOf("."));
            if (file.exists()) {
                options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                graphicUtil = new GraphicsUtil();
                imageProfile.setImageBitmap(graphicUtil.getCircleBitmap(
                        bm, 16));
            } else {
                Picasso.with(this)
                        .load(gnr.getLoggedUser().getProfile_pic_url())
                        .into(target);
            }
        }
    }

    @Override
    protected void onDestroy() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
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
}
