package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		TrieNode root = new TrieNode(null,null,null);
		Indexes i = new Indexes(0,(short)0,(short)(allWords[0].length()-1));
		TrieNode firstKid = new TrieNode(i,null,null);
		root.firstChild = firstKid;
		for(int j = 1; j<allWords.length;j++)
		{
			i = new Indexes(j,(short)0,(short)(allWords[j].length()-1));
			TrieNode current = new TrieNode(i,null,null);
			recursiveBuild(firstKid,current,allWords);
		}
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		return root;
	}
	private static void recursiveBuild(TrieNode parent, TrieNode nodeToAdd, String[] allWords)
	{
		TrieNode ptr = parent;
		int x = indexMatch(ptr.substr,nodeToAdd.substr,allWords);
		if(x==ptr.substr.startIndex)
		{
			if(ptr.sibling==null)
			{
				ptr.sibling = nodeToAdd;
			}
			else
			{
				recursiveBuild(ptr.sibling,nodeToAdd,allWords);
			}
		}
		else
		{
			if(ptr.firstChild == null)
			{
				Indexes i = null;
				if((x-1)>ptr.substr.startIndex)
				{
					i = new Indexes(ptr.substr.wordIndex,(short)(x),ptr.substr.endIndex);
					nodeToAdd.substr.startIndex = (short)(x);
					ptr.substr.endIndex = (short)(x-1);
				}
				else
				{
					i = new Indexes(ptr.substr.wordIndex,(short)x,ptr.substr.endIndex);
					nodeToAdd.substr.startIndex = (short)x;
					ptr.substr.endIndex = (short)(x-1);
				}
				TrieNode newKid = new TrieNode(i,null,null);
				switchKids(ptr,newKid);
				recursiveBuild(ptr.firstChild,nodeToAdd,allWords);	
			}
			else
			{	
				if(x<=ptr.substr.endIndex) {
				Indexes i = new Indexes(ptr.substr.wordIndex,(short)x,(short)x);
				ptr.substr.endIndex = (short)(x-1);
				TrieNode newKid = new TrieNode(i,null,null);
				TrieNode temp = ptr.firstChild;
				ptr.firstChild = newKid;
				newKid.firstChild = temp;
				nodeToAdd.substr.startIndex=(short)(x);
				recursiveBuild(ptr.firstChild,nodeToAdd,allWords);
				}
				else
				{
					nodeToAdd.substr.startIndex = (short)x;
					recursiveBuild(ptr.firstChild,nodeToAdd,allWords);
				}
				
			}
		}
		return;
	}
	
	private static int indexMatch(Indexes a, Indexes b, String[] allWords)
	{
		String str = allWords[a.wordIndex];
		String str2 = allWords[b.wordIndex];
		int end = a.startIndex;
		int termCon = a.endIndex;
		if(str.length()>str2.length())
		{
			termCon = b.endIndex;
		}
		for(int i = end; i<=termCon; i++)
		{
			if(str.charAt(i)==str2.charAt(i))
			{
				end++;
			}
			else
			{
			break;			
			}
		}
		return end;	

	}
	private static int indexMatch(Indexes a, String b, String[] allWords)
	{	String str = allWords[a.wordIndex];
		str = str.substring(a.startIndex, a.endIndex+1);
	String str2 = b;
	if(str2.equals(""))
	{
		return 0;
	}
	int end = 0;
	int termCon = str.length()-1;
	if(str.length()>str2.length())
	{
		termCon = b.length()-1;
	}
	for(int i = end; i<=termCon; i++)
	{
		if(str.charAt(i)==str2.charAt(i))
		{
			end++;
		}
		else
		{
		break;			
		}
	}
		
		return end;
	}

	private static void switchKids(TrieNode parent, TrieNode newKid)
	{
		TrieNode temp = parent.firstChild;
		parent.firstChild = newKid;
		parent.firstChild.firstChild = temp;
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		ArrayList<TrieNode> ret = new ArrayList();
		TrieNode ptr = root.firstChild;
		ret = completionList(ptr,prefix,allWords,ret);
		return ret;
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
	}
	private static ArrayList<TrieNode> completionList(TrieNode ptr, String prefix, String[] allWords, ArrayList<TrieNode> ret)
	{
		ArrayList<TrieNode> wordList = ret;
		TrieNode pointer = ptr;
		String target = prefix;
		int x = indexMatch(ptr.substr,target,allWords);
		if(target.equals(""))
		{
			TrieNode horizontal = pointer;
			while(horizontal!=null) {
			if(horizontal.firstChild==null)
			{
				wordList.add(horizontal);
			}
			else
			{
				completionList(horizontal.firstChild,target,allWords,wordList);
			}
			horizontal = horizontal.sibling;
			}
		}
		else if(x>0)
		{
			target = target.substring(x, target.length());
			if(pointer.firstChild!=null) {
			completionList(pointer.firstChild,target,allWords,wordList);
			}
			else
			{
				wordList.add(pointer);
			}
		}
		else
		{
			if(pointer.sibling!=null) {
			completionList(pointer.sibling,target,allWords,wordList);}
		}
		if(wordList.isEmpty())
		{
			return null;
		}
		return wordList;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
