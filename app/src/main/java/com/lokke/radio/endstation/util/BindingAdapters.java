package com.lokke.radio.endstation.util;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lokke.radio.endstation.R;

import jp.wasabeef.glide.transformations.BlurTransformation;


public class BindingAdapters {


    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, String imageUri) {
        try {
            Glide
                    .with(view.getContext())
                    .load(imageUri)
                    .placeholder(R.drawable.app_icon)
                    .into(view);
        } catch (Exception ignored) {
        }
    }

    @BindingAdapter("error")
    public static void setError(TextInputLayout editText, Object strOrResId) {
        if (strOrResId instanceof Integer) {
            editText.setError(editText.getContext().getString((Integer) strOrResId));
        } else {
            editText.setError((String) strOrResId);
        }
    }

    @BindingAdapter("onFocus")
    public static void bindFocusChange(TextInputEditText textInputLayout, View.OnFocusChangeListener onFocusChangeListener) {
        if (textInputLayout.getOnFocusChangeListener() == null) {
            textInputLayout.setOnFocusChangeListener(onFocusChangeListener);
        }
    }

    @BindingAdapter("blurSrc")
    public static void setBlurImageUri(ImageView view, String imageUri) {
        try {
            Glide
                    .with(view.getContext())
                    .load(imageUri)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                    .placeholder(R.drawable.app_icon)
                    .into(view);
        } catch (Exception ignored) {
        }
    }
}
