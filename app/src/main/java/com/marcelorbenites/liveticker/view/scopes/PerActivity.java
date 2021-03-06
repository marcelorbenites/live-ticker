package com.marcelorbenites.liveticker.view.scopes;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Scope;

@Scope @Retention(RetentionPolicy.RUNTIME) @Documented public @interface PerActivity {
}