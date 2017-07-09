package com.marcelorbenites.liveticker.exceptions;

import com.marcelorbenites.liveticker.R;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class) public class NetworkErrorToStringResourceConverterTest {

  @Test public void convertToErrorMessage() throws Exception {
    NetworkErrorToStringResourceConverter converter = new NetworkErrorToStringResourceConverter();
    assertEquals(R.string.activity_match_list_network_error, converter.convert(new IOException()));
    assertEquals(R.string.activity_match_list_unknown_error, converter.convert(new SQLException()));
  }
}