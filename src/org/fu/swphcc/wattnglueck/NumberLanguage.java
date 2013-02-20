package org.fu.swphcc.wattnglueck;

import com.abbyy.mobile.ocr4.UserRecognitionLanguage;

public enum NumberLanguage implements UserRecognitionLanguage {
    Numbers( 1024 );
    final int _languageID;
    NumberLanguage( final int languageID ) {
          _languageID = languageID;
    }
	@Override
	public int getLanguageId() {
		return _languageID;
	}
}

