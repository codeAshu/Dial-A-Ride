package ride.iiitb.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import ride.iiitb.model.CabLocationModel;
import ride.iiitb.model.RequestModel;

public class InputHandelController {

	public static int n , N , c, R, minTime = Integer.MAX_VALUE, maxTime =0;
	/* n = number of locations
	 * N = number of vehicle
	 * c = capacity of the cabs
	 * R = no of request to process  */

	public static ArrayList<ArrayList<Integer>> adjList = new ArrayList<ArrayList<Integer>>() ;	//Adjacency Matrix

	public static ArrayList<Integer> cabLoc = new ArrayList<Integer>();							//location of cabs

	CabLocationModel locModel =null;
	public static ArrayList<CabLocationModel> cabLocList = new ArrayList<CabLocationModel>();	//cabLocation model List

	RequestModel req = null;
	public static ArrayList<RequestModel> reqList = new ArrayList<RequestModel>();				//request model List


	/***********************************************************************************************
	 * This method will read the input file pass the adjacency list to the process controller
	 * It will also store cab location at midnight in 'cablocList' ArrayList
	 ************************************************************************************************/
	public void readFile(String input)
	{
		try {


			BufferedReader br = new BufferedReader(new FileReader(input));

			String line = br.readLine();  			//first line has been read, <n N c R>

			//fetch the initial values
			String[] values = line.split(" ");

			n = Integer.parseInt(values[0]);
			N = Integer.parseInt(values[1]);
			c = Integer.parseInt(values[2]);
			R = Integer.parseInt(values[3]);


			//Now fetch n*n adjacency matrix
			for(int i = 0 ; i<n; i++){
				line = br.readLine();

				Scanner scanner = new Scanner(line);
				ArrayList<Integer> row = new ArrayList<Integer>();

				while (scanner.hasNextInt()) {
					row.add(scanner.nextInt());
				}	

				adjList.add(row);
			}

			/*	
			for (int i = 0; i < adjList.size(); i++) {
				System.out.println(i+" th line  :"+adjList.get(i));
			}
			 */
			//read taxi locations
			int i =0;
			line = br.readLine();
			Scanner scanner = new Scanner(line);
			while (scanner.hasNextInt()) {

				cabLoc.add(scanner.nextInt());

				locModel = new CabLocationModel(i, (cabLoc.get(i)-1), 0,0);
				cabLocList.add(locModel);
				i++;

			}

			/*
			for ( i = 0; i < cabLocList.size(); i++)
				System.out.println(i+" th cab  :"+cabLocList.get(i).getCab() +" and location is "+cabLocList.get(i).getLocation());
			 */
			//fetch requests and load into reqList

			line = br.readLine();
			while (line != null) {
				scanner = new Scanner(line);

				ArrayList<Integer> row = new ArrayList<Integer>();

				while (scanner.hasNextInt()) {
					row.add(scanner.nextInt());
				}		


				if(row.size()==4)
				{
					getMinMaxTime(row.get(3));
					req = new RequestModel(row.get(0)-1,row.get(1)-1,row.get(2),row.get(3));			//taking locations from 0 to 99, but input is as 0 to 100
					reqList.add(req);
				}
				line = br.readLine();
			}

		} catch(IOException e){
			e.printStackTrace();

		}

	}


	public static void getMinMaxTime(int expTime) {

		if(expTime<= minTime)
			minTime = expTime;

		if(expTime>= maxTime)
			maxTime = expTime;


	}
}
