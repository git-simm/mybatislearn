package simm.learning.biztest;

import org.junit.Test;
import simm.learning.biztest.util.EncodeUtil;

/**
 * 转换测试
 */
public class ConvertTest {
    @Test
    public void testUnicodeToString() throws Exception {
        String str = "\\u6728\\u+6720\\u+6729";
        String s = EncodeUtil.unicodeToStr(str);
        System.out.println(s);  //木
    }

}
