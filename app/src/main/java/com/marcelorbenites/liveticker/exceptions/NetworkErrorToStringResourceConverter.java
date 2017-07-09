package com.marcelorbenites.liveticker.exceptions;

import com.marcelorbenites.liveticker.R;
import java.io.IOException;

public class NetworkErrorToStringResourceConverter implements ThrowableToStringResourceConverter {

  @Override public int convert(Throwable throwable) {
    if (throwable instanceof IOException) {
      return R.string.activity_match_list_network_error;
    }
    return R.string.activity_match_list_unknown_error;
  }
}
