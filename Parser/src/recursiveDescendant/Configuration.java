package recursiveDescendant;

import java.util.Stack;

public class Configuration {

    public String state;
    public int position;
    public Stack<String> workingStack;
    public Stack<String> inputStack;

    public Configuration(String state, int position, Stack<String> workingStack, Stack<String> inputStack) {
        this.state = state;
        this.position = position;
        this.workingStack = workingStack;
        this.inputStack = inputStack;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "state='" + state + '\'' +
                ", pos=" + position +
                ", working=" + workingStack +
                ", input=" + inputStack +
                '}';
    }
}
