package com.jackie.fairy;

import com.jackie.fairy.bean.FairyDao;
import com.jackie.fairy.bean.FairyService;
import com.jackie.fairy.constant.ParseType;
import com.jackie.fairy.context.FairyApplicationContext;
import org.junit.Test;

/**
 * Created by jackie on 17/11/19.
 */
public class FairyTest {

    @Test
    public void testLoadBean() {
        /**
         * bean加载实例化（xml）
         */
        FairyApplicationContext xmlApplicationContext =
                new FairyApplicationContext("application-context-getbean.xml", ParseType.XML_PARSER);
        FairyDao xmlFairyDao = (FairyDao) xmlApplicationContext.getBean("fairyDao");
        xmlFairyDao.greet();

        /**
         * bean加载实例化（json）
         */
        FairyApplicationContext jsonApplicationContext =
                new FairyApplicationContext("application-context.json", ParseType.JSON_PARSER);
        FairyDao jsonFairyDao = (FairyDao) jsonApplicationContext.getBean("fairyDao");
        jsonFairyDao.greet();

        /**
         * bean依赖注入
         */
        FairyApplicationContext autowiredApplicationContext =
                new FairyApplicationContext("application-context-inject.xml");
        FairyService fairyService = (FairyService) autowiredApplicationContext.getBean("fairyService");
        fairyService.greet();
        fairyService.lighting();

        /**
         * bean依赖注入
         */
        FairyApplicationContext annotationApplicationContext =
                new FairyApplicationContext("application-context-annotation-inject.xml");
        FairyService annotationFairyService = (FairyService) annotationApplicationContext.getBean("fairyService");
        annotationFairyService.greet();
        annotationFairyService.lighting();
    }
}
