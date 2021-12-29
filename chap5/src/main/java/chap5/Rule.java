package chap5;

public class Rule {

    private final Condition condition;
    private final Action action;

    public Rule(final Condition condition, final Action action) {
        this.condition = condition;
        this.action = action;
    }

    public void perform(Facts facts) {
        if(condition.evaluate(facts)){
            action.perform(facts);
        }
    }
}