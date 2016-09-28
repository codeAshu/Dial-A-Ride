package ride.iiitb.controller;

import static ride.iiitb.controller.InputHandleController.maxTime;
import static ride.iiitb.controller.InputHandleController.reqList;

import java.util.ArrayList;

import ride.iiitb.model.RequestModel;

public class Main {
	public static void main(String[] args) {
	long start = System.currentTimeMillis();
	InputHandleController in = new InputHandleController();
	// Read input file and load data into data structures
	in.readFile(args[0]);

	ProcessController pc = new ProcessController();
	//calculate shortest path matrix
	pc.calShortestPath();									

	//Calculate length and revenue of the request
	pc.calcRevAndLength(reqList);

	//Mark all the long requests and sort the reqID according to them
	pc.markLongReq(reqList);			

	//get average length of an edge
	pc.getAvgLength();
	
	// Initialize current time
	int currentTime = 1;
	
	while(currentTime <maxTime){
		ArrayList<RequestModel> validRequest = null;
		
		// Get all the valid requests at the time
		validRequest = pc.getValidRequest(reqList,currentTime);			

		// Check suitable cabs for the request
		pc.suitedCab(validRequest, currentTime);

		// Update cab details
		pc.updateCabDetails(currentTime);

		currentTime = currentTime+1;

		validRequest.clear();
	}
	pc.printResults(args[1]);
	long elapsed = System.currentTimeMillis() - start;
	System.out.println(String.format("Total elapsed time: %d ms", elapsed));
	}
}