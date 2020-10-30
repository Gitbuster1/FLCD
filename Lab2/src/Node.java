public class Node<K, P> {
    K key;
    P position;
    Node<K, P> next;
    
    public Node(K key, P position)
    {
        this.key = key;
        this.position = position;
    }
}