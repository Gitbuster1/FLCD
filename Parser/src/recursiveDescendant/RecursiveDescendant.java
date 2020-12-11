package recursiveDescendant;

import Utils.Grammar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class RecursiveDescendant {

    public Configuration configuration;
    public Grammar grammar;

    public RecursiveDescendant(Grammar grammar) {
        this.grammar = grammar;
        configuration = new Configuration("q", 1, new Stack<>(), new Stack<>());
        configuration.workingStack.push("epsilon");
        configuration.inputStack.push("epsilon");
        configuration.inputStack.push(grammar.start);
    }
    
    // Print comments could be deleted 
    public List<String> run(List<String> w) throws Exception {
        while (!configuration.state.equals("f") && !configuration.state.equals("e")) {
            //System.out.println(configuration);
            String inputTop = configuration.inputStack.peek();
            String workingTop = configuration.workingStack.peek();
            if (configuration.position == w.size() + 1 && configuration.inputStack.peek().equals("epsilon")) {
                //System.out.println("success");
                success();
            }
            if (configuration.state.equals("q")) {
                if (grammar.nonterminals.contains(inputTop)) {
                    //System.out.println("expand");
                    expand();
                } else if (w.size() >= configuration.position && w.get(configuration.position - 1).equals(inputTop)) {
                    //System.out.println("advance");
                    advance();
                } else if ((w.size() < configuration.position || !w.get(configuration.position - 1).equals(inputTop))) {
                    //System.out.println("momentary insuccess");
                    momentaryInsuccess();
                }
            } else if (configuration.state.equals("b")) {
                if (!grammar.terminals.contains(workingTop)) {
                    //System.out.println("another try");
                    anotherTry();
                } else {
                    //System.out.println("back");
                    back();
                }
            }
        }
        if (configuration.state.equals("f")) {
            System.out.println("Sequence accepted\n");
            return configuration.workingStack;
        } else {
            throw new Exception("Syntax Error");
        }
    }

    public void expand() {
        String nonterminal = configuration.inputStack.pop();
        List<String> result = new ArrayList<>(grammar.productions.get(nonterminal).get(0));
        Collections.reverse(result);
        result.forEach(s -> configuration.inputStack.push(s));
        configuration.workingStack.add(nonterminal + "#" + "1");
    }

    public void advance() {
        configuration.position++;
        String terminal = configuration.inputStack.pop();
        configuration.workingStack.push(terminal);
    }

    public void momentaryInsuccess() {
        configuration.state = "b";
    }

    public void back() {
        configuration.position--;
        configuration.inputStack.push(configuration.workingStack.pop());
    }

    public void anotherTry() {
        String topWorking = configuration.workingStack.peek();
        List<String> gamma = grammar.productions.get(topWorking.split("#")[0]).get(Integer.parseInt(topWorking.split("#")[1]) - 1);
        if (Integer.parseInt(topWorking.split("#")[1]) != grammar.productions.get(topWorking.split("#")[0]).size()) {
            configuration.state = "q";
            gamma.forEach(s -> configuration.inputStack.pop());
            configuration.workingStack.pop();
            configuration.workingStack.push(topWorking.split("#")[0] + "#" + (Integer.parseInt(topWorking.split("#")[1]) + 1));
            List<String> gamma2 = new ArrayList<>(grammar.productions.get(topWorking.split("#")[0]).get(Integer.parseInt(topWorking.split("#")[1])));
            Collections.reverse(gamma2);
            gamma2.forEach(s -> configuration.inputStack.push(s));

        } else if (configuration.position == 1 && topWorking.split("#")[0].equals(grammar.start)) {
            configuration.state = "e";
        } else {
            configuration.workingStack.pop();
            gamma.forEach(s -> configuration.inputStack.pop());
            configuration.inputStack.push(topWorking.split("#")[0]);
        }
    }


    public void success() {
        configuration.state = "f";
    }
}
