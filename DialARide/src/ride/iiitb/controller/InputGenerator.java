package ride.iiitb.controller;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
public class InputGenerator {

	public static void main(String[] args) {
		PrintWriter writer;
		int[][] matrix = InputGenerator.RandomArray(1000);
		try {
			writer = new PrintWriter("input_1000_full.txt", "UTF-8");
			for(int i = 0; i < matrix.length; i++)
			{
				for(int j = 0; j < matrix.length; j++) {
					writer.print(matrix[i][j] + " ");
				}
				writer.println();
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	private static int[][] RandomArray(int n) {
	    int[][] randomMatrix = new int [n][n];

	    Random rand = new Random(); 
	    rand.setSeed(20); 
	    for (int i = 0; i < n; i++) {     
	        for (int j = 0; j < n; j++) {
	            Integer r = rand.nextInt()% 100; 
	            randomMatrix[i][j] = Math.abs(r);
	        }
	    }
	    for (int i = 0; i < n; i++) {     
	        for (int j = 0; j < n; j++) {
	        	if(i == j) {
	        		randomMatrix[i][j] = -1;
	        		continue;
	        	}
	            Integer r1 = rand.nextInt()% 1000;
	            Integer r2 = rand.nextInt()% 1000;
	            randomMatrix[Math.abs(r1)][Math.abs(r2)] = -1;
	        }
	    }
	    int counter = 0;
	    for(int i = 0; i < n; i++) {
	    	for(int j = 0; j < n; j++) {
	    		if(randomMatrix[i][j] == -1)
	    		{
	    			counter++;
	    		}
	    	}
	    }
	    System.out.println("Total empty: " + counter);
	    return randomMatrix;
	}
}
