package comp261.assig1;

import java.util.*;

/**
 * This is an implementation of a trie, used for the search box.
 */

public class Trie { 
	TrieNode root = new TrieNode(); // the root node of the trie

	public Trie() {
	}

	/**
	 * Adds a given stop to the Trie.
	 */
	public void add(Stop stop) {
		// write the code to add a stop object into the trie
		TrieNode current = root; 
		current.addStop(stop);
		for(int i = 0; i < stop.getName().length(); i++){
			char c = stop.getName().charAt(i); 
			if(!current.children.keySet().contains(c)){
				current.children.put(c, new TrieNode()); 	
			}
			current = current.children.get(c); 
			current.addStop(stop);	
		}
		current.setWordTrue(); 
	}

	/**
	 * Returns all the stops whose names start with a given prefix.
	 */
	public ArrayList<Stop> getAll(String prefix) {
		// write the code to get all the stops whose names match the prefix.
		TrieNode current = root;
		for(int i = 0; i < prefix.length(); i++){
			char c = prefix.charAt(i); 
			if(!current.children.containsKey(c)){return null;}
			current = current.children.get(c); 
		}

		return current.data;
	}



	/**
	 * Represents a single node in the trie. It contains a collection of the
	 * stops whose names are exactly the traversal down to this node.
	 */
	private class TrieNode {
		ArrayList<Stop> data = new ArrayList<>();
		Map<Character, TrieNode> children = new HashMap<>();

		boolean isWord = false; 

		public void setWordTrue(){
			isWord = true; 
		}

		public void addStop(Stop stop){
			data.add(stop);
		}
	}
}