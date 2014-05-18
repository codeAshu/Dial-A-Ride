package ride.iiitb.controller;

import java.util.ArrayList;
import java.util.Scanner;


/********************************************************************************
 * This is Floyd Warshall's algorithm, It will return Shortest path Matrix
 ********************************************************************************/
public class floyd {
	int[][] ans;
	
	public int[][] shortestpath(int[][] adj, int[][] path) {

		int n = adj.length;
		ans = new int[n][n];

		// Implement algorithm on a copy matrix so that the adjacency isn't
		// destroyed.
		copy(ans, adj);

		// Compute successively better paths through vertex k.
		for (int k=0; k<n;k++) {

			// Do so between each possible pair of points.
			for (int i=0; i<n; i++) {
				for (int j=0; j<n;j++) {


					if (ans[i][k]+ans[k][j] < ans[i][j]) {
						ans[i][j] = ans[i][k]+ans[k][j];
						path[i][j] = path[k][j];
					}
				}
			}
		}

		// Return the shortest path matrix.
		return ans;
	}


	/********************************************************************************
	 *  Copies the contents of array b into array a. Assumes that both a and
	 * b are 2D arrays of identical dimensions.
	 ********************************************************************************/
	public static void copy(int[][] a, int[][] b) {

		for (int i=0;i<a.length;i++)
			for (int j=0;j<a[0].length;j++)
				a[i][j] = b[i][j];
	}

	
	/********************************************************************************
	 *  Returns the smaller of a and b.
	 ********************************************************************************/
	public static int min(int a, int b) {
		if (a < b) 
			return a;
		else       
			return b;
	}


	/*********************************************************************************************
	 *This method will print all the Nodes from Start to End in shortest path calculation
	 * This is a utility Function , can be modified as per needs.
	 ********************************************************************************************/
	public ArrayList<Integer> pathNodes(int start , int end, int[][] path) {

		//System.out.println("From where to where do you want to find the shortest path?(0 to 4)");

		// The path will always end at end.
		String myPath = end + "";

		// Loop through each previous vertex until you get back to start.
		while (path[start][end] != start) {
			myPath = path[start][end] + " " + myPath;
			end = path[start][end];
		}

		// Just add start to our string and print.
		myPath = start + " " + myPath;
		

		//System.out.println("Here's the path "+myPath);
		Scanner scanner = new Scanner(myPath);
		
		ArrayList<Integer> Nodes = new ArrayList<Integer>();

		while (scanner.hasNextInt()) {
			Nodes.add(scanner.nextInt());

		}
		return Nodes;

	}
}