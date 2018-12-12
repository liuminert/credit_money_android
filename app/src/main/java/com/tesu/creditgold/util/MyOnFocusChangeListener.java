package com.tesu.creditgold.util;

import android.view.View;
import android.widget.EditText;

/**
 * Created by Administrator on 2017/4/13 0013.
 */
public class MyOnFocusChangeListener implements View.OnFocusChangeListener {
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EditText editText = (EditText)v;
        String hint = null;
        if (hasFocus) {
            hint = editText.getHint().toString();
            v.setTag(hint);
            editText.setHint("");
        } else {
            hint = editText.getTag().toString();
            editText.setHint(hint);
        }
    }
}
