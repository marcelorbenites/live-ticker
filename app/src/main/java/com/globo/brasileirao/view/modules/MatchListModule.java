package com.globo.brasileirao.view.modules;

import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;

import com.globo.brasileirao.data.DataModule;
import com.globo.brasileirao.exceptions.NetworkErrorToStringResourceConverter;
import com.globo.brasileirao.exceptions.ThrowableToStringResourceConverter;
import com.globo.brasileirao.view.adapter.MatchListAdapter;
import com.globo.brasileirao.view.image.ImageLoader;
import com.globo.brasileirao.view.image.ImageModule;
import com.globo.brasileirao.view.utils.UnitConverter;

import java.text.DateFormat;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ActivityModule.class, DataModule.class, ImageModule.class})
public class MatchListModule {

    private final int teamIconWidth;
    private final int teamIconHeight;
    private final int teamIconPlaceholder;

    public MatchListModule(int teamIconWidth, int teamIconHeight, @DrawableRes int teamIconPlaceholder) {
        this.teamIconWidth = teamIconWidth;
        this.teamIconHeight = teamIconHeight;
        this.teamIconPlaceholder = teamIconPlaceholder;
    }

    @Provides MatchListAdapter provideMatchListAdapter(LayoutInflater layoutInflater, ImageLoader imageLoader, UnitConverter unitConverter, DateFormat matchDateFormat) {
        return new MatchListAdapter(layoutInflater, imageLoader, unitConverter.dpToPixels(teamIconWidth), unitConverter.dpToPixels(teamIconHeight), teamIconPlaceholder, matchDateFormat);
    }
}