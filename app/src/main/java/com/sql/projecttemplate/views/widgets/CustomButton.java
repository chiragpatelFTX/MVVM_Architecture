package com.sql.projecttemplate.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.sql.projecttemplate.R;
import com.sql.projecttemplate.utils.CommonUtils;


/**
 * Name : CustomButton
 * <br> Purpose :
 * This class is used to set as custom textview, here we have check for multiple types of attrributes for font name.
 * with respect to font name we have load that particular font.
 */
public class CustomButton extends android.support.v7.widget.AppCompatButton {
    private static final String TAG = "TextView";

    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
        setIncludeFontPadding(false);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
        setIncludeFontPadding(false);
    }


    /**
     * Name : CustomButton setCustomFont
     * <br> Purpose : This method is used to set type face tp that textview.
     *
     * @param ctx   : context object of fragment/activity.
     * @param attrs : attribute set of widgets.
     */
    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomFontView);
        String customFont = a.getString(R.styleable.CustomFontView_customFont);
        if (customFont != null)
            setTypeface(CommonUtils.getTypeFace(ctx, customFont));
        a.recycle();
    }
}
