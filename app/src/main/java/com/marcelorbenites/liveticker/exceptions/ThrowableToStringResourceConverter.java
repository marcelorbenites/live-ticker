package com.marcelorbenites.liveticker.exceptions;

import android.support.annotation.StringRes;

public interface ThrowableToStringResourceConverter {

  @StringRes int convert(Throwable throwable);
}
