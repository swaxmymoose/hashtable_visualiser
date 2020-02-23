/*  07/02/2020
    HashTable.java
    COSC326
*/

import java.util.ArrayList;

/**
 * HashTable is a implementation of a hash table data structure
 */
public class HashTable {

    private int CAPACITY;
    private String[] items;

    /**
     * Contructor for hash table
     * @param cap capacity of hash table
     */
    public HashTable(int cap) {
        this.CAPACITY = cap;
        items = new String[CAPACITY];
    }

    /**
     * getNumberOfKeys() iterates through items array and calculates the number of keys inserted
     * @return the number of keys in the hash table
     */
    public int getNumberOfKeys() {
        int keys = 0;
        for (int i = 0; i < getCapacity(); i++) {
            if (items[i] != null) {
                keys++;
            }
        }
        return keys;
    }

    /**
     * toString() returns a string representation of the hashtable used for debugging purposes.
     */
    public String toString() {
        String str = new String();
        for (int i = 0; i < getCapacity(); i ++) {
            str = str + i + " : " + items[i] + "\n";
        }
        return str;
    }

    /**
     *  Converts string into hash
     * 
     *  Algorithm from COSC242 booklet
     * 
     * @param str the string to hash
     * @return hashed value of str
     */
    public int hashString(String str) {
        long result = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);   
            result += ((int)c) + 31 * result;
        }    
        return((int) result);   
    }

    /**
     * hashInt() hashes its input used a simple mod by 10 operation
     * @param str a string representation of an integer value to hash
     * @return the hash of string
     */
    public int hashInt(String str) {
        return (Integer.parseInt(str) % 10) % getCapacity();
    }

    /**
     * Searches for a item in hash table
     * 
     * @param v                the string to search for
     * @param hashingAlgorithm the hashing algorithm selected
     * @param colRes the collision resolution strategy selected
     * @return array list of index's where the first index 0 through length - 2 are indexes where collisions have occured,
     *         and index at length -1 is either -1 if it could not be found or the index where it was found.
     */
    public ArrayList<Integer> search(String v, String hashingAlgorithm, String colRes) {
        int k;
        int hash;
        int collCounter = 0;
        ArrayList<Integer> returnList = new ArrayList<Integer>();
        if (hashingAlgorithm == "simple integer mod hash") {
            hash = hashInt(v);
        } else {
            hash = Math.abs(hashString(v) % this.CAPACITY);
        }

        k = hash;
        
        while (collCounter < this.CAPACITY) {
            if (collCounter > 0) {
                if (colRes == "linear probing") {
                    k += 1;
                    k = k % this.CAPACITY;
                } else if (colRes == "quadratic probing") {
                    Double d = Math.pow((double) collCounter, 2);
                    k = hash + d.intValue();
                    k = k % this.CAPACITY;
                }
            }
            if (items[k] != null && items[k].equals(v)) { 
                returnList.add(k);
                return returnList;
            } else {
                returnList.add(k);
            }
            collCounter++;
        }
        returnList.add(-1);
        return returnList;
    }

    /**
     * Deletes item from hash table
     * 
     * @param v the string to delete
     * @return index of string v in hash table or -1 if absent
     */
    public int delete(String v, String hashingAlgorithm, String colRes) {
        ArrayList<Integer> l = new ArrayList<Integer>();
        l = search(v, hashingAlgorithm, colRes);
        int indx = l.get(l.size() - 1);
        if (indx != -1) {
            this.items[indx] = null;
            return indx;
        }
        return -1;
    }

    /**
     * Inserts a item in hash table
     * 
     * @param v                the string to insert
     * @param hashingAlgorithm the hashing algorithm selected
     * @param colRes           the collision resolution strategy selected
     * @return array list of index's where the first index 0 through length - 2 are
     *         indexes where collisions have occured, and index at length -1 is
     *         either -1 if it could not be found or the index where it was successfully inserted.
     */
    public ArrayList<Integer> insert(String v, String hashingAlgorithm, String colRes) {
        int k;
        int hash;
        int collCounter = 0;
        ArrayList<Integer> returnList = new ArrayList<Integer>();
        if (hashingAlgorithm == "simple integer mod hash") {
            hash = hashInt(v);
        } else {
            hash = Math.abs(hashString(v) % this.CAPACITY);
        }

        k = hash;

        while (collCounter < this.CAPACITY) {
            if(collCounter > 0) {
                if (colRes == "linear probing") {
                    k += 1;
                    k = k % this.CAPACITY;
                } else if (colRes == "quadratic probing") {
                    Double d = Math.pow((double) collCounter, 2);
                    k = hash + d.intValue();
                    k = k % this.CAPACITY;
                }
            }
            if (items[k] == null) {
                items[k] = v;
                returnList.add(k);
                return returnList;
            } else {
                returnList.add(k);
            }
            collCounter++;
        }
        returnList.add(-1);
        return returnList;
    }


    /**
     * Setter for CAPACITY data field
     * 
     * @param cap the capacity
     */
    public void setCapacity(int cap) {
        this.CAPACITY = cap;
    }

    /**
     * Getter for CAPACITY data field
     * 
     * @return capacity of hash table
     */
    public int getCapacity() {
        return this.CAPACITY;
    }

}