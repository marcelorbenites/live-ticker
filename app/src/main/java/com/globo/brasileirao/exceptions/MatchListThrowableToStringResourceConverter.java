package com.globo.brasileirao.exceptions;

import com.globo.brasileirao.R;

import java.io.IOException;

public class MatchListThrowableToStringResourceConverter implements ThrowableToStringResourceConverter {

    @Override public int convert(Throwable throwable) {
        if (throwable instanceof IOException) {
            return R.string.activity_match_list_network_error;
        }
        return R.string.activity_match_list_unknown_error;
    }
}
