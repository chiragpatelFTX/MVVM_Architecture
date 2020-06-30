package com.ftx.mvvm_template.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.utils.CommonUtils;


/**
 * Name : CustomEditText
 * <br> Purpose :
 * This class is used to set as custom textview, here we have check for multiple types of attrributes for font name.
 * with respect to font name we have load that particular font.
 */
public class CustomEditText extends android.support.v7.widget.AppCompatEditText {
    private static final String TAG = "TextView";

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
        setIncludeFontPadding(false);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
        setIncludeFontPadding(false);
    }


    /**
     * Name : CustomEditText setCustomFont
     * <br> Purpose :  This method is used to set type face tp that textview.
     *
     * @param ctx   : context object of activity/fragment
     * @param attrs : attributes of the widget.
     */
    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomFontView);
        String customFont = a.getString(R.styleable.CustomFontView_customFont);
        if (customFont != null)
            setTypeface(CommonUtils.getTypeFace(ctx, customFont));
        a.recycle();
    }
}
