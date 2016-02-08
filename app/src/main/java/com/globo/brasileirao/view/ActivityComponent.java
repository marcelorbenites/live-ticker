package com.globo.brasileirao.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;

import com.globo.brasileirao.ApplicationComponent;
import com.globo.brasileirao.view.image.ImageLoader;
import com.globo.brasileirao.view.scopes.PerActivity;
import com.squareup.sqlbrite.BriteDatabase;

import java.text.DateFormat;

import javax.inject.Named;

import dagger.Component;
import retrofit2.Retrofit;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    @Named("application") Context applicationContext();

    Retrofit retrofit();

    BriteDatabase briteDatabase();

    LayoutInflater layoutInflater();

    LinearLayoutManager linearLayoutManager();

    ImageLoader imageLoader();

    DateFormat dateFormat();

}