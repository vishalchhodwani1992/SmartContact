package com.androiderstack.custom_view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.androiderstack.smartcontacts.R;

/**
 * Created by vishalchhodwani on 16/3/17.
 */
public class StyledEditText extends EditText {

    private final String TAG = "StyledEdiText";
    private final String DEFAULT_FONT = "roboto_regular.ttf";

    public StyledEditText(Context context) {
        super(context);
    }

    public StyledEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        setCustomFont(context, attrs);
    }

    public StyledEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setCustomFont(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StyledEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setCustomFont(context, attrs);
    }


    public void setCustomFont(Context context, AttributeSet attrs)
    {
        try
        {
            String customFont = getCustomFont(context, attrs);
            Log.e(TAG, "customFont=="+customFont);

            if(customFont != null)
            {
                Typeface face=Typeface.createFromAsset(context.getAssets(), customFont);
                this.setTypeface(face);
            }
            else
            {
                Typeface face=Typeface.createFromAsset(context.getAssets(), DEFAULT_FONT);
                this.setTypeface(face);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private String getCustomFont(Context context, AttributeSet attrs)
    {
        TypedArray ta = null;

        try {
            ta = context.obtainStyledAttributes(attrs, R.styleable.TextElement, 0, 0);
            String fontName = ta.getString(R.styleable.TextElement_myfont);
            return fontName;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }finally {
            ta.recycle();
        }
        return null;
    }
}
