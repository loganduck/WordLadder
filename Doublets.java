import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.stream.Collectors;

/* The above imports are available for use but not required for solving the Doublets interface. */

/**
 * Provides an implementation of the WordLadderGame interface. The lexicon
 * is stored as a TreeSet of Strings.
 *
 * @author Logan Duck (lmd0036@auburn.edu)
 * @author Dean Hendrix (dh@auburn.edu)
 * @version 2017-11-06
 */
public class Doublets implements WordLadderGame {

    // The word list used to validate words.
    // Must be instantiated and populated in the constructor.
    private TreeSet<String> lexicon;

    /**
     * Instantiates a new instance of Doublets with the lexicon populated with
     * the strings in the provided InputStream. The InputStream can be formatted
     * in different ways as long as the first string on each line is a word to be
     * stored in the lexicon.
     */
    public Doublets(InputStream in) {
        try {
            lexicon = new TreeSet<String>();
            Scanner s =
                new Scanner(new BufferedReader(new InputStreamReader(in)));
            while (s.hasNext()) {
                String str = s.next();
                lexicon.add(str.toLowerCase());
                s.nextLine();
            }
            in.close();
        }
        catch (java.io.IOException e) {
            System.err.println("Error reading from InputStream.");
            System.exit(1);
        }
    }

    /**
    * Returns the Hamming distance between two strings, str1 and str2. The
    * Hamming distance between two strings of equal length is defined as the
    * number of positions at which the corresponding symbols are different. The
    * Hamming distance is undefined if the strings have different length, and
    * this method returns -1 in that case. See the following link for
    * reference: https://en.wikipedia.org/wiki/Hamming_distance
    *
    * @param  str1 the first string
    * @param  str2 the second string
    * @return      the Hamming distance between str1 and str2 if they are the
    *                  same length, -1 otherwise
    */
    public int getHammingDistance(String str1, String str2) {
    	int distance = 0;
    	if (str1.length() != str2.length()) {
    		return -1;
    	}
    	for (int i = 0; i < str1.length(); i++) {
    		if (str1.charAt(i) != str2.charAt(i)) {
    			distance++;
    		}
    	}
    	return distance;
    }

    /**
    * Returns a word ladder from start to end. If multiple word ladders exist,
    * no guarantee is made regarding which one is returned. If no word ladder exists,
    * this method returns an empty list.
    *
    * Depth-first search with backtracking must be used in all implementing classes.
    *
    * @param  start  the starting word
    * @param  end    the ending word
    * @return        a word ladder from start to end
    */
    public List<String> getLadder(String start, String end) {
        List<String> ladder = new ArrayList<String>();
        List<String> empty = new ArrayList<String>(); // empty ladder
        if (start.length() != end.length() || isWord(start) == false || 
            isWord(end) == false) {
                return empty;
        }
        if (start == end) {
            ladder.add(start);
            return ladder;
        }
          
        TreeSet<String> tree = new TreeSet<>();
        Deque<String> deq = new ArrayDeque<>();
        deq.addLast(start);
        tree.add(start);
        while (!deq.isEmpty()) {
            String peekLast = deq.peekLast();
            if (peekLast.equals(end)) {
                break;
            }
            List<String> peekNeighbors = getNeighbors(peekLast);
            List<String> neighbors = new ArrayList<String>(); 
            for (String word : peekNeighbors) {
                if (!tree.contains(word)) {
                    neighbors.add(word);
                }
            }
            if (!neighbors.isEmpty()) {
                deq.addLast(neighbors.get(0));
                tree.add(neighbors.get(0));
            }
            else {
                deq.removeLast();
            }
        }
        ladder.addAll(deq);
        return ladder;
    }

    /**
    * Returns a minimum-length word ladder from start to end. If multiple
    * minimum-length word ladders exist, no guarantee is made regarding which
    * one is returned. If no word ladder exists, this method returns an empty
    * list.
    *
    * Breadth-first search must be used in all implementing classes.
    *
    * @param  start  the starting word
    * @param  end    the ending word
    * @return        a minimum length word ladder from start to end
    */
	public List<String> getMinLadder(String start, String end) {
        List<String> empty = new ArrayList<String>(); // empty ladder
        List<String> ladder = new ArrayList<String>();
        if (start.length() != end.length() || isWord(start) == false || 
            isWord(end) == false) {
                return empty;
        }
        if (start == end) {
            ladder.add(start);
            return ladder;
        }
        
        Deque<Node> deq = new ArrayDeque<>();
        TreeSet<String> tree = new TreeSet<>();
        tree.add(start);
        deq.addLast(new Node(start, null));
        while (!deq.isEmpty()) {
        Node n = deq.removeFirst();
        String position = n.position;
        for (String posNeighbors : getNeighbors(position)) {
            if (!tree.contains(posNeighbors)) {
                tree.add(posNeighbors);
                deq.addLast(new Node(posNeighbors, n));
            }
            if (posNeighbors.equals(end)) {
                Node m = deq.removeLast();
                while (m != null) {
                    ladder.add(0, m.position);
                    m = m.predecessor;
                }
                return ladder;
            }
         }
      }
      return empty;
  }

    /**
    * Returns all the words that have a Hamming distance of one relative to the
    * given word.
    *
    * @param  word the given word
    * @return      the neighbors of the given word
    */	
	public List<String> getNeighbors(String word) {
        List<String> neighbors = new ArrayList<String>();
        List<String> empty = new ArrayList<String>();
        if (word == null) {
            return empty;
        }
        for (String value : lexicon) {
            if (getHammingDistance(word, value) == 1) {
                neighbors.add(value);
            }
        }
        return neighbors;
    }

    /**
    * Returns the total number of words in the current lexicon.
    *
    * @return number of words in the lexicon
    */
	public int getWordCount() {
		return lexicon.size();
	}

    /**
    * Checks to see if the given string is a word.
    *
    * @param  str the string to check
    * @return     true if str is a word, false otherwise
    */
	public boolean isWord(String str) {
		if (lexicon.contains(str)) {
			return true;
		} else {
			return false;
		}
	}

    /**
    * Checks to see if the given sequence of strings is a valid word ladder.
    *
    * @param  sequence the given sequence of strings
    * @return          true if the given sequence is a valid word ladder,
    *                       false otherwise
    */
	public boolean isWordLadder(List<String> sequence) {
        if (sequence == null || sequence.isEmpty()) {
            return false;
        }
        if (sequence.size() == 1) {
            return true;
        }
        int count = 0;
        for (int i = 0; i < sequence.size() - 1; i++) {
            if (isWord(sequence.get(i)) != true || isWord(sequence.get(i + 1)) != true) {
                return false;
            }
            if (sequence.get(i) == sequence.get(i + 1)) {
                return false;
            }
            if (getHammingDistance(sequence.get(i), sequence.get(i + 1)) > 1) {
                count = getHammingDistance(sequence.get(i), sequence.get(i + 1));
            }
            if (count > 1) {
                return false;
            }
        }
        return true;
    }

    private class Node {
      String position;
      Node predecessor;
   
      public Node(String p, Node pred) {
         position = p;
         predecessor = pred;
      }
   }
}