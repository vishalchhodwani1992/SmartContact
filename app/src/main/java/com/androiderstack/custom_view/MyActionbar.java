package com.androiderstack.custom_view;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androiderstack.smartcontacts.R;

/**
 * Created by vishalchhodwani on 6/3/17.
 */
public class MyActionbar {

    AppCompatActivity activity;
    View.OnClickListener clickListener;

    public MyActionbar(AppCompatActivity activity)
    {
        this.activity = activity;
    }

    public void setActionbar(String titleString, boolean showBack, boolean showMenu )
    {
        ActionBar actionBar = activity.getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.theme_gradiant));
        actionBar.setCustomView(R.layout.actionbar_layout);

        View actionbarView = actionBar.getCustomView();

        Toolbar toolbar = (Toolbar) actionbarView.getParent();
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.setPadding(0, 0, 0, 0);

        StyledTextView title = (StyledTextView) actionbarView.findViewById(R.id.actionbarLayout_title);
        ImageView backIv = (ImageView) actionbarView.findViewById(R.id.actionbarLayout_backButton);
        LinearLayout menuLayout = (LinearLayout) actionbarView.findViewById(R.id.actionbarLayout_menu);

        title.setText(titleString);

//        actionbarView.setPadding(0, 0, 0, 0);

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clickListener != null) {
                    clickListener.onClick(v);
                } else {
                    activity.onBackPressed();
                }
            }
        });

        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.onClick(v);
                else
                    Toast.makeText(activity, activity.getResources().getString(R.string.under_construction_message), Toast.LENGTH_SHORT).show();

            }
        });

        backIv.setVisibility(showBack ? View.VISIBLE : View.GONE);
        menuLayout.setVisibility(showMenu ? View.VISIBLE : View.GONE);
    }

    public View getActionBarView()
    {
        return activity.getSupportActionBar().getCustomView();
    }

    public void setViewVisible(int buttonId, int visibility)
    {
        try
        {
            View actionbarView = activity.getSupportActionBar().getCustomView();
            if (actionbarView != null)
            {
                actionbarView.findViewById(buttonId).setVisibility(visibility);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    public void setClickListener(View.OnClickListener clickListener)
    {
        this.clickListener = clickListener;
    }
}
