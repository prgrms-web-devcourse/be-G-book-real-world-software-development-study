package chap5;

import java.util.ArrayList;
import java.util.List;

public class BusinessRuleEngine {

    private final List<Rule> rules;
    private final Facts facts;

    public BusinessRuleEngine(final Facts facts) {
        this.rules = new ArrayList<>();
        this.facts = facts;
    }

    public void addRule(final Rule rule) {
        this.rules.add(rule);
    }

    public void run() {
        this.rules.forEach(action -> action.perform(facts));
    }
}
