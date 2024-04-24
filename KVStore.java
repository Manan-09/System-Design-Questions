import java.util.*;

public class KVStore {

    private class Transaction {
        private Map<String, String> prevStore;
        private Map<String, String> currentStore;
        private Set<String> deletedKeys;

        Transaction() {
            this.prevStore = new HashMap<>();
            this.currentStore = new HashMap<>();
            this.deletedKeys = new HashSet<>();
        }

        Transaction(Map<String, String> prev) {
            this.prevStore = new HashMap<>(prev);
            this.currentStore = new HashMap<>();
            this.deletedKeys = new HashSet<>();
        }
    }

    private Stack<Transaction> transactionStack;
    private Map<String, String> globalStore;

    public KVStore() {
        transactionStack = new Stack<>();
        globalStore = new HashMap<>();
    }

    public void begin() {
        if(!transactionStack.isEmpty()) {
            Map<String, String> lastStore = transactionStack.peek().prevStore;
            lastStore.putAll(transactionStack.peek().currentStore);
            lastStore.remove(transactionStack.peek().deletedKeys);
            transactionStack.add(new Transaction(lastStore));
        }
        else
            transactionStack.add(new Transaction(globalStore));
    }

    public void rollback() {
        if (!transactionStack.isEmpty()) transactionStack.pop();
        else {
            System.out.println("No active transaction");
        }
    }

    public void commit() {
        if (!transactionStack.isEmpty()) {
            globalStore.putAll(transactionStack.peek().currentStore);
            globalStore.remove(transactionStack.peek().deletedKeys);
        }
        else {
            System.out.println("No active transaction");
        }
    }

    public void set(String key ,String value) {
        if (!transactionStack.isEmpty()) {
            transactionStack.peek().currentStore.put(key, value);
            transactionStack.peek().deletedKeys.remove(key);
        }
        else {
            globalStore.put(key, value);
        }
    }

    public String get(String key) {
        if(!transactionStack.isEmpty() && transactionStack.peek().deletedKeys.contains(key)) {
            return "";
        }
        if (!transactionStack.isEmpty() && transactionStack.peek().currentStore.containsKey(key)) {
            return transactionStack.peek().currentStore.get(key);
        } else if (!transactionStack.isEmpty() && transactionStack.peek().prevStore.containsKey(key)) {
            return transactionStack.peek().prevStore.get(key);
        } else if(globalStore.containsKey(key)){
            return globalStore.get(key);
        }
        return "";
    }

    public void delete(String key) {
        if (!transactionStack.isEmpty()) {
            transactionStack.peek().currentStore.remove(key);
            transactionStack.peek().deletedKeys.add(key);
        }
        else {
            globalStore.remove(key);
        }
    }
}
