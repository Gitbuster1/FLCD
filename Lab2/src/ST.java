import java.util.ArrayList;

public class ST<K, V> {
    // hash is used to store array of chains
    private ArrayList<Node<K, V>> hash;

    // Current capacity of array list
    private int capacity;

    // Current size of array list
    private int size;

    // Constructor (Initializes capacity, size and
    // empty chains.
    public ST(int capacity) {
        hash = new ArrayList<>();
        this.capacity = capacity;
        size = 0;

        // Create empty chains
        for (int i = 0; i < capacity; i++)
            hash.add(null);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    // This implements hash function to find index
    // for a key
    private int hashing(K key) {
        int sum = 0;
        for (char c : key.toString().toCharArray()){
            sum += c;
        }
        return sum % capacity;
    }


    // Returns true if a key exists
    public boolean exists(K key) {
        // Find head of chain for given key
        int index = hashing(key);
        Node<K, V> head = hash.get(index);

        // Search key in chain
        while (head != null) {
            if (head.key.equals(key))
                return true;
            head = head.next;
        }

        // If key not found
        return false;
    }

    // Adds a key value pair to hash
    public void add(K key, V value) {
        // Check if key is already present
        if (exists(key)) {
            return;
        }

        // Find head of chain for given key
        int index = hashing(key);
        Node<K, V> head = hash.get(index);

        // Insert key in chain
        size++;
        head = hash.get(index);
        Node<K, V> newNode = new Node<K, V>(key, value);
        newNode.next = head;
        hash.set(index, newNode);

        // If load factor goes beyond threshold, then
        // double the hashtable size
        if ((1.0 * size) / capacity >= 0.7) {
            ArrayList<Node<K, V>> temp = hash;
            hash = new ArrayList<>();
            capacity = 2 * capacity;
            size = 0;
            for (int i = 0; i < capacity; i++)
                hash.add(null);

            for (Node<K, V> headNode : temp) {
                while (headNode != null) {
                    add(headNode.key, headNode.position);
                    headNode = headNode.next;
                }
            }
        }
    }
}
