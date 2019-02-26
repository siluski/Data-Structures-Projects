package friends;

import structures.Queue;
import structures.Stack;

import java.util.*;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		ArrayList<ArrayList<Integer>> friends = makeFriendLists(g);
		int p1Num = g.map.get(p1);
		int p2Num = g.map.get(p2);
		int[] listOfPaths = BFSshortestPath(g,p1Num, friends);
		Stack paths = new Stack();
		int current = p2Num;
		while(listOfPaths[current]!=-1) {

			paths.push(current);
			current = listOfPaths[current];
		}
		if(paths.isEmpty()==false) {
		paths.push(current);}
		ArrayList<String> returnList = new ArrayList<String>();
		while(!paths.isEmpty())
		{
			int currentNum = (int)paths.pop();
			String currentName = g.members[currentNum].name;
			returnList.add(currentName);
			
		}
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return returnList;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		/** COMPLETE THIS METHOD **/
		boolean[] visited = new boolean[g.members.length];
		ArrayList<ArrayList<String>> allCliques = new ArrayList<ArrayList<String>>();
		for(int i = 0; i<g.members.length; i++)
		{
			if(visited[i]==false)
			{
				ArrayList<String> clique = new ArrayList<String>();
				clique = dfsCliques(g,g.members[i],clique,school,visited);
				visited[i] = true;
				if(clique.isEmpty()==false) {
				allCliques.add(clique);}
			}
		}
		/*for(int i = 0; i<allCliques.size();i++) {
			ArrayList<String>clique = allCliques.get(i);
			for(int j = 0; j<clique.size(); j++)
			{
				String name = clique.get(j);
				for(int k = j+1; k<clique.size();k++)
				{
					if(clique.get(k).equals(name))
					{
						clique.remove(k);
						k--;
					}
				}
			}
			
		}*/
		for(int i = 0; i<allCliques.size();i++)
		{
			ArrayList<String>clique = allCliques.get(i);
			ArrayList<String>cliqueWithoutDoubles = new ArrayList<String>();
			for(int j = 0; j<clique.size(); j++)
			{
				if(!cliqueWithoutDoubles.contains(clique.get(j)))
				{
					cliqueWithoutDoubles.add(clique.get(j));
				}
			}
			allCliques.remove(i);
			allCliques.add(i, cliqueWithoutDoubles);
		}
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return allCliques;
		
	}
	private static int[] BFSshortestPath(Graph g, int root,ArrayList<ArrayList<Integer>> friends)
	{
		Queue shortestSoFar = new Queue();
		boolean[] visited = new boolean[g.members.length];
		int[] previous = new int[g.members.length];
		for(int i = 0; i<previous.length; i++)
		{
			previous[i]=-1;
		}
		visited[root] = true;
		shortestSoFar.enqueue(root);
		while(!shortestSoFar.isEmpty())
		{
			int currentWeight = (int)shortestSoFar.dequeue();
			for(int x : friends.get(currentWeight))
			{
				if(visited[x]==false)
				{
					visited[x]=true;
					previous[x]=currentWeight;
					shortestSoFar.enqueue(x);
				}
			}
		}
		return previous;
	}
	private static ArrayList<ArrayList<Integer>> makeFriendLists(Graph g)
	{
		ArrayList<ArrayList<Integer>> friendLists = new ArrayList<ArrayList<Integer>>();
		for(Person p : g.members)
		{
			ArrayList<Integer> currentFriends = new ArrayList<Integer>();
			Friend currentFriend = p.first;
			while(currentFriend!=null){
				currentFriends.add(currentFriend.fnum);
				currentFriend = currentFriend.next;
			}
			friendLists.add(currentFriends);
		}
		return friendLists;
	}
	private static ArrayList<String> dfsCliques(Graph g,Person p, ArrayList<String> clique, String school,boolean[]visited)
	{
		int index = g.map.get(p.name);
		school = school.toLowerCase();
		if(visited[index]==false)
		{
			if(p.school!=null) {
				if(p.school.equalsIgnoreCase(school)) {
				clique.add(p.name);
				}
				visited[index]=true;
				Friend neighbor = p.first;
				while(neighbor!=null)
				{
					Person o = g.members[neighbor.fnum];
					if(o.school==null||o.school.equalsIgnoreCase(school)==false)
					{
						neighbor = neighbor.next;
						continue;
					}

					if(visited[neighbor.fnum]==false)
					{
						clique.addAll(dfsCliques(g, o, clique, school,visited));
					}
					neighbor = neighbor.next;
				}
				
			
			}
		}
		return clique;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		/** COMPLETE THIS METHOD **/
		
		ArrayList<ArrayList<Integer>> friendLists = makeFriendLists(g);
		boolean[] visited = new boolean[g.members.length];
		int dfsNum = 0;
		int[] dfsNums = new int[g.members.length];
		int[] backNums = new int[g.members.length];
		ArrayList<ArrayList<String>> returnList = new ArrayList<ArrayList<String>>();
		for(int i = 0; i<g.members.length; i++)
		{
			if(visited[i]==false) {
			ArrayList<String> temp = new ArrayList<String>();
			dfsNum = dfsNum+1;
			int backNum = dfsNum;
			dfsNums[i] = dfsNum;
			backNums[i] = backNum;
			temp = dfsConnect(g,i,friendLists,dfsNums,backNums,visited, dfsNum);
			returnList.add(temp);}
		}
		ArrayList<String> ret = new ArrayList<String>();
		for(int i = 0; i<returnList.size();i++)
		{
			ArrayList<String> temp = returnList.get(i);
			for(int j = 0; j<temp.size(); j++)
			{
				if(!ret.contains(temp.get(j)))
				{
					ret.add(temp.get(j));
				}
			}
		}
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return ret;
		
	}
	private static ArrayList<String> dfsConnect(Graph g,int root, ArrayList<ArrayList<Integer>>friends,int[]dfsNums,int[]backNums, boolean[]visited, int dfsNum)
	{
		visited[root] = true;
		ArrayList<String> ret = new ArrayList<String>();
		ArrayList<Integer> neighbors = friends.get(root);
		for(int i = 0; i< neighbors.size();i++)
		{
			if(visited[neighbors.get(i)])
			{
				backNums[root] = Math.min(backNums[root], dfsNums[neighbors.get(i)]);
			}
			else
			{
				dfsNum = dfsNum+1;
				int backNum = dfsNum;
				dfsNums[neighbors.get(i)] = dfsNum;
				backNums[neighbors.get(i)] = backNum;
				ret.addAll(dfsConnect(g,neighbors.get(i),friends, dfsNums, backNums,visited, dfsNum));
				if(dfsNums[root]>backNums[neighbors.get(i)])
				{
					backNums[root] = Math.min(backNums[root], backNums[neighbors.get(i)]);
				}
				if(dfsNums[root]<=backNums[neighbors.get(i)])
				{
					if(dfsNums[root]==1)
					{
						int x = dfsNums[neighbors.get(i)];
						int y = backNums[neighbors.get(i)];
						if(x-y>1)
						{
							ret.add(g.members[root].name);
						}
					}
					else {
						for(int j = 0; j<neighbors.size();j++)
						{
							ArrayList<Integer> neighborsFriends = friends.get(neighbors.get(j));
							if(neighborsFriends.size()==1)
							{
								if(neighborsFriends.get(0)==root)
								{
									ret.add(g.members[root].name);
								}
							}
						}
					}
				}
			}
		}
		return ret;
		
	}
}

