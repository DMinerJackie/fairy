package com.jackie.fairy;

import com.jackie.fairy.bean.FairyDao;
import com.jackie.fairy.constant.ParseType;
import com.jackie.fairy.context.FairyApplicationContext;
import org.junit.Test;

/**
 * Created by jackie on 17/11/19.
 */
public class FairyTest {

    @Test
    public void testLoadBean() {
        FairyApplicationContext xmlApplicationContext = new FairyApplicationContext("application-context-getbean.xml", ParseType.XML_PARSER);
        FairyDao xmlFairyDao = (FairyDao) xmlApplicationContext.getBean("fairyDao");
        xmlFairyDao.greet();

        FairyApplicationContext jsonApplicationContext = new FairyApplicationContext("application-context.json", ParseType.JSON_PARSER);
        FairyDao jsonFairyDao = (FairyDao) jsonApplicationContext.getBean("fairyDao");
        jsonFairyDao.greet();
    }
}
