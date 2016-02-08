package com.globo.brasileirao.exceptions;

import com.globo.brasileirao.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class MatchListThrowableToStringResourceConverterTest {

    @Test public void convertToErrorMessage() throws Exception {
        MatchListThrowableToStringResourceConverter converter = new MatchListThrowableToStringResourceConverter();
        assertEquals(R.string.activity_match_list_network_error, converter.convert(new IOException()));
        assertEquals(R.string.activity_match_list_unknown_error, converter.convert(new SQLException()));
    }
}