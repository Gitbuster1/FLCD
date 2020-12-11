package Utils;

import java.util.*;

public class ParserOutput {

    Grammar grammar;
    Map<String, Integer> nrChildren;

    List<String> values;
    List<Integer> father;
    List<Integer> leftChild;
    List<Integer> rightSibling;

    public ParserOutput(Grammar grammar) {
        this.grammar = grammar;
        values = new ArrayList<>();
        father = new ArrayList<>();
        leftChild = new ArrayList<>();
        rightSibling = new ArrayList<>();

        nrChildren = new HashMap<>();
        for (String nonterminal : grammar.nonterminals) {
            List<List<String>> productions = grammar.productions.get(nonterminal);
            for (int i = 0; i < productions.size(); i++) {
                nrChildren.put(nonterminal + "#" + (i + 1), productions.get(i).size());
            }
        }
        for (String terminal : grammar.terminals) {
            nrChildren.put(terminal, 0);
        }
        nrChildren.put("epsilon", 1);
    }

    public void addProductionString(List<String> productionString) {
        Stack<Integer> stack = new Stack<>();
        stack.push(-1);
        for (String p : productionString) {
            int father = stack.pop();
            int idx = add(p, father);
            for (int i = 0; i < nrChildren.get(p); i++) {
                stack.push(idx);
            }
        }
    }

    public int add(String node, int parent) {
        values.add(node);
        int index = values.size() - 1;
        rightSibling.add(-1);
        leftChild.add(-1);
        if (parent == -1) {
            return index;
        }

        if (leftChild.get(parent) == -1) {
            leftChild.set(parent, index);
        } else {
            int current = leftChild.get(parent);
            int prev = -1;
            while (current != -1) {
                prev = current;
                current = rightSibling.get(current);
            }
            rightSibling.set(prev, index);
        }
        father.add(parent);
        return index;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Parser output:\n\n");
        sb.append("Values: ");
        sb.append(this.values).append("\n");
        sb.append("Father: ");
        sb.append(this.father).append("\n");
        sb.append("Left child: ");
        sb.append(this.leftChild).append("\n");
        sb.append("Right sibling: ");
        sb.append(this.rightSibling).append("\n\n");
        if(this.values.size() == 0){
            return sb.toString();
        }
        sb.append(subtree(0));
        return sb.toString();
    }

    private String subtree(int node) {
        StringBuilder sb = new StringBuilder();
        String value = values.get(node);
        if(value.contains("#")){
            value = value.split("#")[0];
        }
        sb.append(value).append("\n");
        List<String> subtrees = new ArrayList<>();
        int child = leftChild.get(node);
        while (child != -1) {
            String subtreeString = subtree(child);
            String shiftedSubtree = Arrays.stream(subtreeString.split("\n")).map(l -> "\t" + l).reduce("", (s, t) -> s + t + "\n");
            String linkedSubtree = " -- " + shiftedSubtree.substring(1);
            subtrees.add(linkedSubtree);
            child = rightSibling.get(child);
        }
        List<String> B = new ArrayList<>();
        if (subtrees.size() > 0) {
            for (String a : subtrees.subList(0, subtrees.size() - 1)) {
                String b = Arrays.stream(a.split("\n")).map(l -> " \t|" + l.substring(1)).reduce("", (s, t) -> s + t + "\n");
                B.add(b);
            }
            B.add(" \t\\" + subtrees.get(subtrees.size() - 1).substring(1));
        }
        B.forEach(sb::append);
        return sb.toString();
    }
}
