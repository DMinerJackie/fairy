package com.jackie.fairy.paser.factory;

import com.google.common.base.Preconditions;
import com.jackie.fairy.constant.ParseType;
import com.jackie.fairy.paser.Parser;
import com.jackie.fairy.paser.impl.JsonParserImpl;
import com.jackie.fairy.paser.impl.XmlParserImpl;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by jackie on 17/11/19.
 */
public class ParserFactory {

    public static Parser getParser(ParseType parseType) {
        Preconditions.checkArgument(StringUtils.isNoneEmpty(parseType.name()), "parseType must be not nulll");

        Parser parser = null;
        switch (parseType) {
            case XML_PARSER:
                return new XmlParserImpl();
            case JSON_PARSER:
                return new JsonParserImpl();
            default:
                return parser;
        }
    }
}
