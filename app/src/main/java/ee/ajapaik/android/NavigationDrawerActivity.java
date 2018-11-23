package ee.ajapaik.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import ee.ajapaik.android.util.WebActivity;

public abstract class NavigationDrawerActivity extends WebActivity {
    private static final String TAG = "NavDrawerActivity";
    private DrawerLayout mDrawer;

    protected abstract void setContentView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        configureNavigationDrawer();
        configureToolbar();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START, true);
        } else {
            super.onBackPressed();
        }
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,  mDrawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        mDrawer.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
    }

    private void configureNavigationDrawer() {
        NavigationView navView = findViewById(R.id.nvView);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Log.d(TAG, "onNavigationItemSelected");

                Context context = NavigationDrawerActivity.this;
                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.nearest:
                        if ( context instanceof NearestActivity
                            && !(context instanceof FinnaActivity)
                            && !(context instanceof Finna1918Activity)
                                ) break;
                        intent = new Intent(context, NearestActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        finish();
                        break;
                    case R.id.finna:
                        if (context instanceof FinnaActivity) break;
                        intent = new Intent(context, FinnaActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        finish();
                        break;
                    case R.id.finna1918:
                        if (context instanceof Finna1918Activity) break;
                        intent = new Intent(context, Finna1918Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        finish();
                        break;
                    case R.id.albums:
                        if (context instanceof AlbumsActivity) break;
                        AlbumsActivity.start(context);
                        finish();
                        break;
                    case R.id.profile:
                        if (context instanceof ProfileActivity) break;
                        ProfileActivity.start(context);
                        break;
                    case R.id.rephoto_drafts:
                        if (context instanceof RephotoDraftsActivity) break;
                        RephotoDraftsActivity.start(context);
                        finish();
                        break;
                    case R.id.favorites:
                        if (context instanceof FavoritesActivity) break;
                        FavoritesActivity.start(context);
                        finish();
                        break;
                    case R.id.my_rephotos:
                        if (context instanceof RephotosActivity) break;
                        RephotosActivity.start(context);
                        finish();
                        break;
                    case R.id.about:
                        if (context instanceof AboutActivity) break;
                        AboutActivity.start(context);
                        if (context instanceof ProfileActivity) finish();
                        break;
                    case R.id.settings:
                        if (context instanceof SettingsActivity) break;
                        SettingsActivity.start(context);
                        if (context instanceof ProfileActivity) finish();
                        break;
                }
                mDrawer.closeDrawer(GravityCompat.START, true);
                return false;
            }
        });
    }
}
