package chap5;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BusinessRuleEngineTest {

//    @Test
//    public void shouldHaveNoRulesInitially() {
//        final BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine();
//
//        assertEquals(0, businessRuleEngine.count());
//    }
//
//    @Test
//    public void shouldAddTwoActions() {
//        final BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine();
//
//        businessRuleEngine.addAction(() -> {});
//        businessRuleEngine.addAction(() -> {});
//
//        assertEquals(2, businessRuleEngine.count());
//    }
//
//    @Test
//    public void shouldExecuteOneAction() {
//        final BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine();
//        final Action mockAction = mock(Action.class);
//
//        businessRuleEngine.addAction(mockAction);
//        businessRuleEngine.run();
//
//        verify(mockAction).perform();
//    }

    @Test
    public void shouldPerformAnActionWithFacts() {
        final Action mockAction = mock(Action.class);
        final Facts mockFacts = mock(Facts.class);
        final BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine(mockFacts);

        businessRuleEngine.addAction(mockAction);
        businessRuleEngine.run();

        verify(mockAction).perform(mockFacts);
    }
}