package Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class Grammar {

    public List<String> nonterminals;
    public List<String> terminals;
    public Map<String, List<List<String>>> productions;
    public String start;

    public Grammar() {
        nonterminals = new ArrayList<>();
        terminals = new ArrayList<>();
        productions = new HashMap<>();
        start = "";
    }

    public static Grammar fromFile(String fileName) throws Exception {
        Grammar grammar = new Grammar();
        Scanner sc = new Scanner(new BufferedReader(new FileReader(fileName)));
        String nonterminalsLine = sc.nextLine();
        grammar.nonterminals = Arrays.asList(nonterminalsLine.split(" "));
        String terminalsLine = sc.nextLine();
        grammar.terminals = new ArrayList<>(Arrays.asList(terminalsLine.split(" ")));
        grammar.start = sc.nextLine();
        String line;
        for (String nonterminal : grammar.nonterminals) {
            grammar.productions.put(nonterminal, new ArrayList<>());
        }
        while (sc.hasNext()) {
            line = sc.nextLine();
            String[] parts = line.split("->");
            String[] productions = parts[1].split("\\|");
            for (String production : productions) {
                String[] symbols = production.split("#");
                grammar.productions.get(parts[0]).add(Arrays.asList(symbols));
            }

        }
        sc.close();
        if(!grammar.validate()){
            throw new Exception("Invalid grammar");
        }
        return grammar;
    }

    public boolean validate() {
        if (!nonterminals.contains(start)) {
            return false;
        }
        for (String key : productions.keySet()) {
            if (!nonterminals.contains(key)) {
                return false;
            }
        }
        for (List<List<String>> rhs: productions.values()){
            for (List<String> values: rhs){
                for (String value: values){
                    if(!(nonterminals.contains(value) || terminals.contains(value))){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public List<String> getNonterminals() {
        return this.nonterminals;
    }

    public List<String> getTerminals() {
        return this.terminals;
    }

    public String getProductions() {
        return productions.keySet().stream().map(this::getForProduction).collect(Collectors.joining("\n"));
    }

    public String getForProduction(String nonterminal) {
        List<List<String>> productions = this.productions.get(nonterminal);
        StringBuilder sb = new StringBuilder();
        sb.append(nonterminal).append("->");
        List<String> aux = new ArrayList<>();
        productions.forEach(p -> aux.add(String.join(" ", p)));
        sb.append(String.join("|", aux));
        return sb.toString();
    }
}
