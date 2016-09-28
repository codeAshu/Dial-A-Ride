package ride.iiitb.controller;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/********************************************************************************
 * Parallel and serial implementations of Floyd Warshall's algorithm to compute
 * the shortest path (only one should be used at a time)
 ********************************************************************************/

public class ShortestPath {
	int[][] ans;
	
	public int[][] shortestPath(int[][] adj, int[][] path) {

		int n = adj.length;
		ans = new int[n][n];
		ArrayList<int[]> ar = new ArrayList<>();
		int ansTemp[] = new int [n*n];
		int pathTemp[] = new int [n*n];

		// Copying matrix for computations so the original isn't modified
		copy(ans, adj);
		
		// Parallel Computation for shortest path
		// Uses the class ParallelShortestPath for parallel implementation of Floyd Warshall
		
		ExecutorService exec = Executors.newCachedThreadPool();
		try {
			// Passing in 8 threads as argument = number of cores on my system (value can be changed)
			ParallelShortestPath parallelSP =  new ParallelShortestPath(n, ans, path, exec, 8);
			
			//long start = System.currentTimeMillis();
			ar = parallelSP.solve();
			ansTemp = ar.get(0);
			pathTemp = ar.get(1);
			int k = 0;
			for(int i = 0; i < n; i++) {
				for(int j = 0; j < n; j++) {
					ans[i][j] = ansTemp[k];
					path[i][j] = pathTemp[k];
					k++;
				}
			}
			
			//long elapsed = System.currentTimeMillis() - start;
			//System.out.println("Floyd parallel: " + elapsed);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			exec.shutdown();
		}
		
		
		// Serial implementation of Floyd Warshall's shortest path algorithm
		
//		long start = System.currentTimeMillis();
//		for (int k = 0; k < n; k++) {
//			for (int i=0; i<n; i++) {
//				for (int j=0; j<n;j++) {
//					if (ans[i][k]+ans[k][j] < ans[i][j]) {
//						ans[i][j] = ans[i][k]+ans[k][j];
//						path[i][j] = path[k][j];
//					}
//				}
//			}
//		}		
//		long elapsed = System.currentTimeMillis() - start;
//		System.out.println("Floyd serial: " + elapsed);
			 
		return ans;
	}

	public static void copy(int[][] a, int[][] b) {
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[0].length; j++)
				a[i][j] = b[i][j];
	}

	public static int min(int a, int b) {
		if (a < b) 
			return a;
		else       
			return b;
	}

	public ArrayList<Integer> pathNodes(int start , int end, int[][] path) {
		String myPath = end + "";
		while (path[start][end] != start) {
			myPath = path[start][end] + " " + myPath;
			end = path[start][end];
		}
		myPath = start + " " + myPath;
		Scanner scanner = new Scanner(myPath);
		
		ArrayList<Integer> Nodes = new ArrayList<Integer>();
		while (scanner.hasNextInt()) {
			Nodes.add(scanner.nextInt());
		}
		scanner.close();
		return Nodes;
	}
}