package com.globo.brasileirao.view.utils;

import android.content.Context;
import android.util.TypedValue;

import javax.inject.Inject;
import javax.inject.Named;

public class UnitConverter {

    private final Context context;

    @Inject public UnitConverter(@Named("application") Context context) {
        this.context = context;
    }

    public int dpToPixels(int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }
}