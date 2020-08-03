package cn.ganzhiqiang.ares.common.serivce;

import cn.ganzhiqiang.ares.Ares;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Ares.class)
public class WeChatServiceTest {

    @Resource
    private WeChatService weChatServiceUnderTest;

    @Test
    public void testMsgSecCheck() {
        // Setup

        // Run the test
        final Integer result = weChatServiceUnderTest.msgSecCheck("test");

        // Verify the results
        assertNotNull(result);
    }
}
