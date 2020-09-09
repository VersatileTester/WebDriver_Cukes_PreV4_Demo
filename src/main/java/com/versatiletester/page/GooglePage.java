package com.versatiletester.page;

import com.versatiletester.cukes.TestBase;

public abstract class GooglePage extends PageBase{
    protected String pageUrl;

    public GooglePage(){
        super();
        pageUrl = TestBase.getProperty(TestBase.GOOGLE_URL_PROPERTY_NAME);
    }
}
