package site.binghai.core.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.binghai.TestApplication;
import site.binghai.core.core.Conversation;


@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = TestApplication.class
)
public class ConversationServiceTest {
    @Autowired
    private ConversationService conversationService;

    @Test
    public void testGetConversation() {
        Conversation conversation = conversationService.getConversation(1L);
        Assert.assertNotNull(conversation);
    }
}