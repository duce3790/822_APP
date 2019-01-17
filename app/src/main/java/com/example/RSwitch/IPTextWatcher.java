package com.example.RSwitch;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class IPTextWatcher implements TextWatcher
{
    private static String LOG_TAG = "IPTextWatcher";

    private EditText editText;

    public IPTextWatcher( EditText editText )
    {
        this.editText = editText;
    }

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    @Override
    public void afterTextChanged( Editable s )
    {
        Log.v( LOG_TAG, s.toString() );

        if( !s.toString().matches( IPADDRESS_PATTERN ) )
        {
            String ip = format( s.toString() );

            editText.removeTextChangedListener( this );
            editText.setText( ip );

            editText.setTextKeepState( ip );
            Selection.setSelection( editText.getText(), ip.length() );

            editText.addTextChangedListener( this );


        }
    }

    public static String format( String value )
    {
        String userInput = "" + value.replaceAll( "[^\\d\\.]", "" );
        StringBuilder ipBuilder = new StringBuilder();

        String[] address = userInput.split("\\.");

        String glue = null;
        for( String part : address )
        {
            if( glue != null ) ipBuilder.append( glue );

            int p = Integer.valueOf( part );

            if( p >= 256 )
            {
                int i = 1;

                do
                {
                    p = Integer.valueOf( part.substring( 0, part.length() -i ) );
                    i++;
                }
                while( p >= 256 );
            }

            ipBuilder.append( p );

            glue = ".";
        }

        if( userInput.charAt( userInput.length()-1 ) == '.' )
            ipBuilder.append( "." );

        return ipBuilder.toString();
    }

    @Override
    public void onTextChanged( CharSequence s, int start, int before, int count )
    {
    }

    @Override
    public void beforeTextChanged( CharSequence s, int start, int count, int after )
    {
    }
}