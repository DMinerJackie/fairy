package com.jackie.fairy;

import com.jackie.fairy.bean.FairyBean;
import com.jackie.fairy.constant.ParseType;
import com.jackie.fairy.context.FairyApplicationContext;
import org.junit.Test;

/**
 * Created by jackie on 17/11/19.
 */
public class FairyTest {

    @Test
    public void testLoadBean() {
        FairyApplicationContext xmlApplicationContext = new FairyApplicationContext("application-context.xml", ParseType.XML_PARSER);
        FairyBean xmlFairyBean = (FairyBean) xmlApplicationContext.getBean("fairyBean");
        xmlFairyBean.greet();

        FairyApplicationContext jsonApplicationContext = new FairyApplicationContext("application-context.json", ParseType.JSON_PARSER);
        FairyBean jsonFairyBean = (FairyBean) jsonApplicationContext.getBean("fairyBean");
        jsonFairyBean.greet();
    }
}
