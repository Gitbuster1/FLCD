public class SymbolTable {

    private final Node[] hashTable;
    private int size = 1000;

    public SymbolTable() {
        this.hashTable = new Node[this.size];
    }

    public SymbolTable(int size) {
        this.size = size;
        this.hashTable = new Node[this.size];
    }

    public int pos(String token) {
        Pair<Integer, Integer> result = this.find(token);
        if (result.getSecond() == -1) {
            return this.add(token);
        } else {
            return result.getFirst() + result.getSecond() * this.size;
        }
    }

    private int hashing(String token) {
        return token.hashCode() % this.size;
    }

    private Pair<Integer, Integer> find(String token) {
        int index1 = hashing(token);
        if (hashTable[index1] == null) {
            return new Pair<>(index1, -1);
        }
        int index2 = 0;
        Node current = hashTable[index1];
        while (current != null && !current.token.equals(token)) {
            current = current.next;
            index2++;
        }
        if (current == null) {
            return new Pair<>(index1, -1);
        }
        return new Pair<>(index1, index2);
    }

    public int add(String token) {
        int index = hashing(token);
        Node newNode = new Node(null, null, token);
        Node current = hashTable[index];
        int pos = 0;
        if (current == null) {
            hashTable[index] = newNode;
            return index;
        } else {
            while (current.next != null) {
                current = current.next;
                pos++;
            }
            current.next = newNode;
            newNode.previous = current;
            return pos * this.size + index;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hashTable.length; i++) {
            Node current = hashTable[i];
            if (current == null) {
                continue;
            }
            sb.append(i).append(": ");
            while (current != null) {
                sb.append(current.token).append(" ");
                current = current.next;
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private class Node {

        public Node previous;
        public Node next;
        public String token;

        public Node(Node previous, Node next, String token) {
            this.previous = previous;
            this.next = next;
            this.token = token;
        }
    }
}
