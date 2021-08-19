package chap5;

@FunctionalInterface
public interface Condition {
    boolean evaluate(Facts facts);
}
