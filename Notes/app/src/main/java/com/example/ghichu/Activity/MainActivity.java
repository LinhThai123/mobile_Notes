package com.example.ghichu.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ghichu.DTO.Notes;
import com.example.ghichu.Fragment.NoteFragment;
import com.example.ghichu.R;
import com.google.android.material.navigation.NavigationView;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.banner.BannerView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int DEFAULT_VIEW = 0x22 ;
    private static final int FRAGMENT_NOTE = 0 ;
    private static final int FRAGMENT_CALENDER = 1 ;
    private static final int FRAGMENT_REMIND = 2 ;
    private static final int FRAGMENT_TRASH = 3 ;
    private static final int FRAGMENT_SETTING = 4 ;

    private int curentFragment = FRAGMENT_NOTE ;

    DrawerLayout drawerLayout ;
    Toolbar tbMain ;
    NavigationView nav ;

    public static int ROW = 1 ;
    public static int SORT = 0 ;
    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            this.requestPermissions(
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                    DEFAULT_VIEW);
        }
        mapping();
        loadAds();
        // support toobal
        setSupportActionBar(tbMain);
        // icon toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,tbMain , R.string.nav_drawer_open,R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // show fargment up to op top
        replaceFragment(new NoteFragment());
        nav.getMenu().findItem(R.id.nav_note).setChecked(true);

        sharedPreferences =  getSharedPreferences("option" , MainActivity.MODE_PRIVATE) ;
    }
    // mapping
    public void mapping(){
        drawerLayout = findViewById(R.id.drawerLayout);
        tbMain       = findViewById(R.id.tbMain) ;
        nav          = findViewById(R.id.nav) ;

        nav.setNavigationItemSelectedListener(this);

    }
    // funtion ads
    private void loadAds(){
        HwAds.init(this);
        BannerView bannerView = findViewById(R.id.hw_banner_view);

        bannerView.setAdId("testw6vs28auh3");
        bannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);

        bannerView.setBannerRefresh(30);
        AdParam adParam = new AdParam.Builder().build() ;
        bannerView.loadAd(adParam);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            // show frafment notes
            case R.id.nav_note:
                setTitle("My Note");
                if(curentFragment != FRAGMENT_NOTE){
                    replaceFragment(new NoteFragment());
                    curentFragment = FRAGMENT_NOTE ;
                }
                break;
            case R.id.nav_Calender:
                break;
            case R.id.nav_remind:
                break;
            case R.id.nav_trash:
                break;
            case R.id.nav_settings:
                break;

        }
        return true ;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
        super.onBackPressed();
        }
    }
    //
    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentFrame,fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu , menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences.Editor editor = sharedPreferences.edit() ;
        switch (item.getItemId()){
            case R.id.mnuGrid:
                ROW = 2 ;
                editor.putInt("ROW" , ROW) ;
                editor.apply();
                if(curentFragment == FRAGMENT_NOTE ){
                    replaceFragment(new NoteFragment());
                }
                break;
            case R.id.mnuList:
                ROW = 1 ;
                editor.putInt("ROW" , ROW) ;
                editor.apply();
                if (curentFragment == FRAGMENT_NOTE) {
                    replaceFragment(new NoteFragment());
                }
            case R.id.mnuUpdateNew:
                SORT = 1 ;
                if(curentFragment == FRAGMENT_NOTE) {
                    replaceFragment(new NoteFragment());
                }
                break;
            case R.id.mnuUpdateOld:
                SORT = 2 ;
                if(curentFragment == FRAGMENT_NOTE) {
                    replaceFragment(new NoteFragment());
                }
                break;
            case R.id.mnuSortAZ:
                SORT = 3;
                editor.putInt("SORT", SORT);
                editor.apply();
                if (curentFragment == FRAGMENT_NOTE) {
                    replaceFragment(new NoteFragment());
                }
                break;
            case R.id.mnuSortZA:
                SORT = 4;
                editor.putInt("SORT", SORT);
                editor.apply();
                if (curentFragment == FRAGMENT_NOTE) {
                    replaceFragment(new NoteFragment());
                }
                break;
            case R.id.mnuSearch:
                Intent intent = new Intent(MainActivity.this , ListNotesActivity.class) ;
                startActivity(intent);
                break;
//            case R.id.mnuSearchDay:
//                startActivity(new Intent(MainActivity.this , FindListDateActivity.class));
//                break;
        }
        return super.onOptionsItemSelected(item);
    }
}