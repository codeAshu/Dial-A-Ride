package ride.iiitb.controller;

import static ride.iiitb.controller.InputHandelController.maxTime;
import static ride.iiitb.controller.InputHandelController.reqList;

import java.util.ArrayList;

import ride.iiitb.model.RequestModel;

public class Main {

	/********************************************************************************
	 * Main()
	 ********************************************************************************/
	public static void main(String[] args) {

		InputHandelController in = new InputHandelController();
		in.readFile(args[0]);											//read and load into request master

		ProcessController pc = new ProcessController();

		//calculate shortest path matrix
		pc.calShortestPath();									

		//Calculate length and revenue of the request
		pc.calcRevAndLength(reqList);

		//Mark all the long requests and sort the reqID according to it
		pc.markLongReq(reqList);			

		//get avg length of an edge
		pc.getAvgLength();
		/*
		for (RequestModel rq : reqList) {
			System.out.println(rq.getReqID() +" "+rq.getOrigin()+" "+rq.getDest()+" "+rq.getTofReq()+" "+rq.getTofExp());
		}
		 */
		int currentTime = 1;
		//start the time clock
		while(currentTime <maxTime){

			ArrayList<RequestModel> validRequest=null;
			//	System.out.println("\n\n\ncurrent Time :"+currentTime);

			//get all the valid request of this time
			validRequest = pc.getValidRequest(reqList,currentTime);			

			/*
			for (RequestModel requestModel : validRequest) {
							System.out.println("req :"+ requestModel.getReqID()+" "+ requestModel.getOrigin() +" "+ requestModel.getDest()+ " exp: "+ requestModel.getTofExp()+" rev :"+requestModel.getRev()+" status :"+requestModel.getStatus());
			}
			//	System.out.println();
			 */
			//check for which cab these requests will suit
			pc.suitedCab(validRequest,currentTime);

			//update each cab details
			pc.updateCabDetails(currentTime);

			currentTime = currentTime+1;				//update current time

			validRequest.clear();
		}

		pc.printResults(args[1]);
	}

}
