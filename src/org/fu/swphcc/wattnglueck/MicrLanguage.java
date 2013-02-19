package org.fu.swphcc.wattnglueck;

import com.abbyy.mobile.ocr4.UserRecognitionLanguage;

public enum MicrLanguage implements UserRecognitionLanguage {
	MICR( 1024 );
	
	final int _languageID;
    MicrLanguage( final int languageID ) {
          _languageID = languageID;
    }


	@Override
	public int getLanguageId() {
		return _languageID;
	}

}
