import Utils.Grammar;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Grammar grammar = Grammar.fromFile("g1.txt");
        System.out.println("Nonterminals: " + grammar.getNonterminals());
        System.out.println("\nTerminals: " + grammar.getTerminals());
        System.out.println("\nProductions:\n" + grammar.getProductions());
        System.out.println("\nProductions for 'S':\n" + grammar.getForProduction("S"));
    }
}
