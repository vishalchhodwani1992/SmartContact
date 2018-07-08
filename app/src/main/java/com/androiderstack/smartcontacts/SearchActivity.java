package com.androiderstack.smartcontacts;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androiderstack.adapter.AllContactsRecyclerAdapter;
import com.androiderstack.item.AllContacts;
import com.androiderstack.utility.LogUtils;
import com.androiderstack.utility.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishalchhodwani on 3/6/17.
 */
public class SearchActivity extends AppCompatActivity implements View.OnTouchListener, AllContactsRecyclerAdapter.OnItemClickListener{

    private final String TAG = "SearchActivity";

    Context context;

    RelativeLayout rootLayout;
    List<AllContacts> feedList;
    RecyclerView feedRecyclerView;
    AllContactsRecyclerAdapter feedListRecyclerAdapter;

    MediaPlayer mediaPlayer;
    private Visualizer mVisualizer;
    boolean isAudioOn = false;
    boolean isAudioPause = false;

    String searchKeyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setMyActionbar();

        initializeViews();

        AppController.getInstance().getFirebaseAnalytics().setCurrentScreen(this, "SearchActivity", "SearchActivity");

    }

    public void initializeViews() {

        context = SearchActivity.this;

        feedList = new ArrayList<>();
        feedRecyclerView = (RecyclerView) findViewById(R.id.activityMain_listView);
        feedRecyclerView.getItemAnimator().setChangeDuration(0);
        feedRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        feedRecyclerView.setLayoutManager(layoutManager);
        feedRecyclerView.setItemAnimator(new DefaultItemAnimator());

        feedListRecyclerAdapter = new AllContactsRecyclerAdapter(context, feedList);
        feedListRecyclerAdapter.setOnItemClickListener(this);
        feedRecyclerView.setAdapter(feedListRecyclerAdapter);
    }


    //-----------------Search Actionbar-----------------//
    private void setMyActionbar() {
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue_color)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        setSearchView(menu);

        return true;
    }

    private void setSearchView(Menu menu) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                try {
                    SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

                    MenuItem menuItem = menu.findItem(R.id.search);

                    final SearchView searchView = (SearchView) menuItem.getActionView();

                    AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
                    try
                    {
                        Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
                        mCursorDrawableRes.setAccessible(true);
                        mCursorDrawableRes.set(searchTextView, R.drawable.cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
                        @Override
                        public boolean onMenuItemActionExpand(MenuItem item) {
                            return true;
                        }

                        @Override
                        public boolean onMenuItemActionCollapse(MenuItem item) {
                            onBackPressed();
                            return true;
                        }
                    });

                    searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                        @Override
                        public boolean onQueryTextSubmit(String query)
                        {
                            LogUtils.e(TAG, "onQueryTextSubmit() : query==" + query);

                            searchKeyword = query;

                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String query) {

                            LogUtils.e(TAG, "onQueryTextChange() : query==" + query);
                            return false;
                        }
                    });
                    if (Utils.hasIceCreamSandwich())
                        menuItem.expandActionView();
                    else
                        MenuItemCompat.expandActionView(menuItem);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //-----------------Search Actionbar-----------------//


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        Utils.setupUI(this, v);

        return true;
    }

    @Override
    public void onItemClick(View view, int position) {

        try {

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
