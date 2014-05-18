package ride.iiitb.controller;
import static ride.iiitb.controller.InputHandelController.*;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ride.iiitb.model.CabLocationModel;
import ride.iiitb.model.CabShareModel;

import ride.iiitb.model.RequestModel;

public class ProcessController {

	public static int[][] shortpath;
	public static int[][] adjMat;
	public static int HighInt = 100000000;
	public static int[][] path;
	public static int avg = 0;

	public static ArrayList<CabShareModel> shareList = new ArrayList<CabShareModel>(); 
	static floyd fd = new floyd();

	public static int cabCapacity = c;


	/********************************************************************************************
	 * This method will assign a known high value to the node which are not directly connected
	 ********************************************************************************************/
	public void replaceToInfi(int[][] array )
	{
		for(int i=0; i<array.length; i++){
			for(int j=0; j<array.length; j++){

				if(array[i][j] == -1 )
					if (i == j)
						array[i][j] =0;
					else
						array[i][j] = HighInt;
			}
		}
	}

	/********************************************************************
	 * Method to display path Matrix								    *
	 ********************************************************************/
	public  static void displayPath(int[][] path)
	{
		for(int i=0;i<path.length;i++){
			for(int j=0;j<path.length;j++){
				System.out.print(path[i][j] + " ");
			}
			System.out.println("");    
		}
	}

	/********************************************************************
	 * Method will return the shortest distance between two nodes								    
	 ********************************************************************/
	public static int distance(int origin, int dest)
	{
		return shortpath[origin][dest];
	}


	/*******************************************************************************************
	 * This method will call Floyed Algorithm to calculate Shortest path Matrix 'shortpath'    *
	 *******************************************************************************************/
	public void calShortestPath(){

		n= adjList.size();
		adjMat = list2Matrix(adjList); 
		//	ArrayList<Integer> p;

		path = new int[n][n];
		replaceToInfi(adjMat);

		// Initialise with the previous vertex for each edge. -1 indicates
		// no such vertex.
		for (int i=0; i<adjMat.length; i++)
			for (int j=0; j<adjMat.length; j++)
				if (adjMat[i][j] == HighInt)
					path[i][j] = -1;
				else
					path[i][j] = i;

		// This means that we don't have to go anywhere to go from i to i.
		for (int i=0; i<adjMat.length; i++)
			path[i][i] = i;

		floyd fd = new floyd();
		shortpath = fd.shortestpath(adjMat, path);

		//display Shortest path Metric
		//	dispShortestMat();

		//p =fd.pathNodes(25, 25, path);
		//	System.out.println("path from 25 to 25 :"+p.get(0));

	}


	/***********************************************************************
	 * Utility method to convert ArrayList to 2d array Matrix			   *	
	 ***********************************************************************/
	public int[][] list2Matrix(ArrayList<ArrayList<Integer>> adjList) {

		int[][] resultMat = new int[n][n];

		ArrayList<Integer> tempList = new ArrayList<Integer>();

		for(int i = 0 ; i<n; i++){
			tempList.addAll(adjList.get(i));

			for(int j = 0 ; j<n; j++){
				resultMat[i][j] = tempList.get(j);
			}
			tempList.clear();
		}
		return resultMat;
	}


	/********************************************************************************
	 * This method will display Shortest path Matrix Calculated by Floyd Algorithm *
	 ********************************************************************************/
	public void dispShortestMat(){	

		// Prints out shortest distances.
		System.out.println("-----Shortest path Matrix-------");
		System.out.print("   |");
		for (int i=0; i<shortpath.length;i++)
			System.out.print(i +"| ");

		System.out.println("");
		for (int i=0; i<shortpath.length;i++) {
			System.out.print("["+i +"] ");
			for (int j=0; j<shortpath.length;j++)
				System.out.print(shortpath[i][j]+"  ");
			System.out.println();
		}
		System.out.println("");
	}



	/*********************************************************************************************************
	 * This function will update the location, TimeStamp of each cab and update there status and occupancy 
	 * @param CabLocationModel
	 *********************************************************************************************************/
	public void updateCabDetails(int currentTime)
	{
		char waitFlag = 'N';
		for(CabLocationModel clm : cabLocList) {

			if(currentTime >= clm.getTimestamp()){

				if(clm.getOccupancy() == 0){
				//	System.out.println("cab "+clm.getCab() +"  Timer is "+clm.getTimer() + "  next dst is  "+clm.getNextDest());
					clm.setTimer(clm.getTimer()+1);
				}


				for (CabShareModel shr :shareList) {										 //get details of each cab against each shareList entry

					if(clm.getCab() == shr.getcl().getCab() &&  shr.getStatus() =='R')	//It will give all the req which cab is running at the current time
					{

						//check if its final destination of one of the request of the cab or not
						if(clm.getLocation() == shr.getRm().getDest() )
						{
							shr.getRm().setDrop(clm.getTimestamp());				//set drop timings

							clm.setOccupancy(clm.getOccupancy()-1);

							shr.setStatus('C');										//close the request
						}

						//if any request is waiting for more than 1 hour and it is not been shared still it will move on
						if( ( (clm.getLocation() == shr.getRm().getOrigin()) && ( (clm.getTimestamp() - shr.getRm().getPic()) >60) )
								|| (clm.getLocation() == shr.getRm().getDest()) && ( (clm.getTimestamp() - shr.getRm().getDrop() >60) ) )
						{
							waitFlag ='Y';
						}
					}
					
				}
				//If a cab  is sitting idea for 1 hour than send it some other good place 
				if(clm.getTimer() >60)
				{
					//get a good place to send this cab
					//System.out.println("get a good place cab "+clm.getCab());
					whereToMove(clm);
					if(clm.getNextDest() !=-1)
						waitFlag = 'Y';

				}

				if( (clm.getNextDest() !=-1 && clm.getOccupancy() >(c/2)+1 ) || (waitFlag =='Y' )) 			//move the cab only when you have almost full cab
				{

					//distance from next destination
					int d= distance(clm.getLocation(),clm.getNextDest() );

					//send the cab to next destination
					clm.setLocation(clm.getNextDest());

					int timestamp = clm.getTimestamp() + 2*d;
					//update Timestamp
					clm.setTimestamp(timestamp);

					//update KM
					clm.setKm(clm.getKm()+d);

					//update next destination
					clm.setNextDest( fd.pathNodes(clm.getNextDest(), clm.getFinalDest(), path).get(1));
					//System.out.println("cab 's next dest :"+clm.getNextDest());
					waitFlag = 'N';
					clm.setTimer(0);

				}
				//now we have to set a new next destination and Final destination for this cab
				setNewDestination(clm);

			}

		}
	}
	/*************************************************************************************
	 * This function will set the next destination of the cab when it finish one request
	 * @param CabLocationModel
	 *************************************************************************************/
	private void whereToMove(CabLocationModel clm) {
		ArrayList<RequestModel> list=null;
		ArrayList<RequestModel> clist = new ArrayList<RequestModel>();
		ArrayList<Integer> p=null;

		//get valid request where cab can reach within 30 min 
		list = getValidRequest(reqList, (clm.getTimestamp()+30) );
		int d =0;
		int tmin = 100;
		for (RequestModel req : list) {
			tmin = 100;
			d= distance(clm.getLocation(), req.getOrigin());
			int t=2*d;

			if( t<= (req.getTofExp()-clm.getTimestamp()) )							//check if cab can reach there in time to serve the request
			{
				if(tmin >t)
				{																	//select the cab which is closest to the request
					tmin = t; 														//update min and create an object for this cab and its detail
					clist.add(req);
				}

			}

		}

		if(clist.size() == 0){
			//System.out.println("no request within 100 min of range");
			clm.setTimestamp(clm.getTimestamp()+2);
		}
		else
		{
			//now I have requests which can be served by the cab within 30 min

			//sort them based on revenue
			Collections.sort(clist, new Comparator<RequestModel>() {
				@Override public int compare(RequestModel r1, RequestModel r2) {
					return r1.getRev() - r2.getRev();
				}});


			//now I have closest request to full fill now cab will go in that direction and try to get some space in cab
			p=fd.pathNodes(clm.getLocation(),clist.get(0).getOrigin(), path);

			//update Next Destination
			int NextDest = p.get(1);
			cabLocList.get(clm.getCab()).setNextDest(NextDest);

			//update final Destination
			cabLocList.get(clm.getCab()).setFinalDest(p.get(p.size()-1));

			clist.clear();
			list.clear();
			//System.out.println("moving "+clm.getCab()+" to " +clm.getNextDest());
		}

	}


	/*************************************************************************************
	 * This function will set the next destination of the cab when it finish one request
	 * @param CabLocationModel
	 *************************************************************************************/
	public static void setNewDestination(CabLocationModel clm) {

		ArrayList<CabShareModel> list= new ArrayList<CabShareModel>();
		int d,req = 0;
		int min = Integer.MAX_VALUE;
		ArrayList<Integer> p=null;

		//get all the open request which are getting served in this cab
		for (CabShareModel shr : shareList) {
			if(clm.getCab() == shr.getcl().getCab() && shr.getStatus() =='R')
				list.add(shr);
		}
		//select the request whose destination is closest to the current location
		for (CabShareModel sh : list) {
			d= distance(clm.getLocation(), sh.getRm().getDest());

			if(d<min){
				min=d;
				req=sh.getRm().getReqID();
			}

		}
		if(list.size() !=0)
		{
			//now I have closest request to full fill now cab will go in that direction and try to get some space in cab
			p=fd.pathNodes(clm.getLocation(),reqList.get(req).getDest(), path);

			//update Next Destination
			int NextDest = p.get(1);
			cabLocList.get(clm.getCab()).setNextDest(NextDest);

			//update final Destination
			cabLocList.get(clm.getCab()).setFinalDest(p.get(p.size()-1));
			list.clear();
		}
	}
	/*******************************************************************************
	 * check which cab will be able to serve the request
	 * @param currentTime 
	 *************************************************************************************/
	public void suitedCab(ArrayList<RequestModel> CurrentReqList, int currentTime) 
	{
		int d,t = 0;
		int tmin = 60;
		ArrayList<CabLocationModel> list = new ArrayList<CabLocationModel>();

		/*
		//sort current List based on time of rev
		Collections.sort(CurrentReqList, new Comparator<RequestModel>() {
			@Override public int compare(RequestModel r1, RequestModel r2) {
				return r1.getRev() - r2.getRev();

			}});
		 */

		for (RequestModel rm : CurrentReqList) {
			//	CabLocationModel cl = null;
			tmin = 60;
			list.clear();
			for (CabLocationModel cab: cabLocList) {

				if(reqList.get(rm.getReqID()).getStatus() != 'C')
				{
					if(cab.getTimestamp() <= currentTime && cab.getOccupancy() < c){					//check if cab is valid at this time and cab has space

						d = distance(cab.getLocation(),rm.getOrigin()); 								//for every request check for every cab 
						t = 2*d;

						if( t<= (rm.getTofExp()-cab.getTimestamp()) )							//check if cab can reach there in time to serve the request
						{
							/*	if(tmin >t)
						{																	//select the cab which is closest to the request
							tmin = t; 														//update min and create an object for this cab and its detail
							cl = cab;
						}
							 */
							if(t<tmin){
								//		System.out.println(" value of t :"+t);
								cab.setTimer(t);
								list.add(cab);											//add all the cab which can be reached to the request in 60 min

							}
						}

					}

				}
			}


			for (CabLocationModel cl : list) {

				if(reqList.get(rm.getReqID()).getStatus() != 'C') //don't process if request is closed by previous cab
				{

					//now got the optimum cab for this request
					if(cl !=null && rm !=null){

						CabShareModel cshare = null;

						cshare = checkPath(rm,cl,cl.getTimer());

						if (  (cshare !=null) && !(shareList.contains(cshare.getRm().getReqID())) ){


							//	System.out.println(" cab "+cshare.getcl().getCab()+" is serving req :"+cshare.getRm().getReqID()+" and earn :"+cshare.getRm().getRev()+"time to reach: "+t+" cab occp is "+cshare.getcl().getOccupancy()+ " and till now cab has earned "+ cshare.getcl().getRevenue());
							cshare.setOccup(cshare.getcl().getOccupancy());
							shareList.add(cshare);															//add share detail to shareList

						}

					}
				}
			}
		}
	}




	/********************************************************************************
	 * Check the path of the cab and decide the request to serve
	 ********************************************************************************/
	public static CabShareModel checkPath(RequestModel rm, CabLocationModel cl, int t) {

		CabShareModel cshare = null;
		ArrayList<Integer> Nodes =null;
		ArrayList<Integer> cabPath =null;


		//here I have a cab detail and request detail 
		Nodes = fd.pathNodes(rm.getOrigin(), rm.getDest(), path);

		int nextDest = Nodes.get(1);
		int finalDest; 
		int rev = rm.getRev();

		if(cl.getFinalDest() == -1)
			cl.setFinalDest(rm.getDest());
		//path of the cab
		cabPath = fd.pathNodes(cl.getLocation(), cl.getFinalDest(), path);

		//now check if cab is empty just add the customer to the cab

		if(cl.getOccupancy() == 0 || cl.getOccupancy() > (c/2) +1) {		//if cab is empty just add it into the cabShare List or Occupancy is greater than certain value

			if( (rm.getStatus() =='L' && t<20 ) ||
					(rm.getStatus() =='L'|| t==0) && (cl.getOccupancy() == 0) ) 	//wait for long request and if empty always start with long request
			{
				finalDest= rm.getDest();									//final destination of cab is destination of request for empty cab

				updateCabLocList(rm,cl,t,rev,nextDest,finalDest);			//update cab Information

				cshare = new CabShareModel(rm,cl);							//created a cab share model where rm has request detail, cl has details of which cab has served it
				cshare.setStatus('R');										//set the status of this  req and cab as running 'R'
				cshare.getRm().setPic(cl.getTimestamp());
				return cshare;
			}
		}

		//what if cab is not empty get the sharing detail

		else// if(cl.getOccupancy() >0 || cl.getOccupancy() <(c/2 +1))														
		{

			//if cab->destination  lies in the shortest path of new request than also pick the request

			if(Nodes.contains(cl.getFinalDest()))
			{
				finalDest= rm.getDest();

				updateCabLocList(rm,cl,t,rev,nextDest,finalDest);			//update cab Information

				cshare = new CabShareModel(rm,cl);
				cshare.setStatus('R');
				cshare.getRm().setPic(cl.getTimestamp());					//update pic up time
				return cshare;

			}
			//if new req->destination lies the shortest path of cab than pickup the req
			else if( cabPath.contains( rm.getDest()) )
			{
				//final destination of cab will not change
				finalDest = cl.getFinalDest();

				updateCabLocList(rm,cl,t,rev,nextDest,finalDest);			//update cab Information

				cshare = new CabShareModel(rm,cl);					    	// a new req and cab and time entry will log
				cshare.setStatus('R');
				cshare.getRm().setPic(cl.getTimestamp());				   //set pick up time
				return cshare;

			}
			//if cab is marked 'L' means a large request with large revenue
			if(rm.getStatus() == 'L')
			{
				finalDest= rm.getDest();
				updateCabLocList(rm,cl,t,rev,nextDest,finalDest);			//update cab Information

				cshare = new CabShareModel(rm,cl);
				cshare.setStatus('R');
				cshare.getRm().setPic(cl.getTimestamp());				   //set pickup timeStamp
				return cshare;
			}

		}

		return cshare;
	}
	/*************************************************************************************
	 * update the information of cab location list 
	 *************************************************************************************/
	public static void updateCabLocList(RequestModel rm, CabLocationModel cl, int t,
			int rev, int nextDest, int finalDest) {

		//update cab TimeStamp with the request and update cab location to request source location
		int cabT = cl.getTimestamp()+t;  

		//if request was valid at cabT time than we can just pick up the request at cabT time, coz cab is there at this time
		if(rm.getTofReq() <cabT )
			cabLocList.get(cl.getCab()).setTimestamp(cabT);	
		else
			cabLocList.get(cl.getCab()).setTimestamp(rm.getTofReq());

		cabLocList.get(cl.getCab()).setLocation(rm.getOrigin());

		//update dead KM cab has to run
		cabLocList.get(cl.getCab()).setDeadKM(cl.getDeadKM()+t/2);

		//now cab has reached at the source of request source
		cabLocList.get(cl.getCab()).setOccupancy(cl.getOccupancy()+1);				//update occupancy
		cabLocList.get(cl.getCab()).setRevenue(cl.getRevenue()+rev);				//update revenue

		//update next and final destination
		cabLocList.get(cl.getCab()).setNextDest(nextDest);
		cabLocList.get(cl.getCab()).setFinalDest(finalDest);						//cab is empty so final dest of cab is right now the dest of this req

		reqList.get(rm.getReqID()).setStatus('C');									//close the request
		//	System.out.println("closed the request :"+rm.getReqID());


	}

	/*************************************************************************************
	 * Return valid request in the current time which are open
	 * @param list 
	 *************************************************************************************/
	public ArrayList<RequestModel> getValidRequest(ArrayList<RequestModel> list, int currentTime) {

		ArrayList<RequestModel> validReq = new ArrayList<RequestModel>();


		for (RequestModel req: reqList)
		{
			if( (req.getTofReq() < currentTime) && (req.getTofExp()> currentTime) && (req.getStatus() != 'C')	 )  
				validReq.add(req);

		}
		return validReq;
	}


	/*************************************************************************************
	 * This function will mark  50% all the heavy requests in the path
	 * @param ArrayList<RequestModel> 
	 *************************************************************************************/
	public void calcRevAndLength(ArrayList<RequestModel> list) {
		int rev=0;
		ArrayList<Integer> Nodes;
		for (RequestModel req : list) {
			rev = distance(req.getOrigin(), req.getDest());
			req.setRev(rev);
		}		
		//calculate no of nodes in each request and update
		for (RequestModel req : list) {
			Nodes = fd.pathNodes(req.getOrigin(), req.getDest(), path);
			req.setLength(Nodes.size());
		}

	}
	/************************************************************************************
	 * This function will mark all the Long requests in the path
	 * @param ArrayList<RequestModel> 
	 *************************************************************************************/
	public void markLongReq(ArrayList<RequestModel> list) {

		int len=1;
		len= list.size();

		//all the valid requests are sorted on the length 
		Collections.sort(list, new Comparator<RequestModel>() {
			@Override public int compare(RequestModel r1, RequestModel r2) {
				return r2.getLength() - r1.getLength();
			}});
		//3/4 of the valid requests are marked Long (status  = 'L')
		for (int i = 0; i < (3*len)/4; i++) {
			list.get(i).setStatus('L');
		}
		//mark Last 100 requests as small and ignore the requests 
		/*
		for(int i = len-1; i>len-200; i--)
			list.get(i).setStatus('S');
		 */
		for (int i = 0; i < reqList.size(); i++) {
			reqList.get(i).setReqID(i);
		}

	}


	/*************************************************************************************
	 * This function will Print the Results revenue and schedule of each cab 
	 * @param 
	 *************************************************************************************/
	public void printResults(String output)
	{
		Writer wrt = null;

		try {
			wrt = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(output), "utf-8"));

			wrt.write("\t\t\t\t\t Schedule of Cabs by Ashutosh Trivedi(MT2013030)\n\n");

			int trev=0;
			for (CabLocationModel clm : cabLocList) {

				ArrayList<CabShareModel> cabPrint = new ArrayList<CabShareModel>();
				int rev=0;


				wrt.write("\n\nCab :"+clm.getCab() );
				wrt.write("\n");

				for (CabShareModel cs : shareList) {
					if(clm.getCab() == cs.getcl().getCab())
						cabPrint.add(cs);
				}

				Collections.sort(cabPrint, new Comparator<CabShareModel>() {
					@Override public int compare(CabShareModel r1, CabShareModel r2) {
						return r1.getRm().getPic() - r2.getRm().getPic();
					}});

				for(int time =1; time<maxTime; time++)
				{
					for (CabShareModel cp : cabPrint) {

						if(time == cp.getRm().getPic())
							wrt.write("(L:"+cp.getRm().getOrigin()+",T:"+ cp.getRm().getPic()+",Pick)  ");

						if(time == cp.getRm().getDrop())
							wrt.write("(L:"+cp.getRm().getDest()+",T:"+ cp.getRm().getDrop()+",Drop)  ");

						rev=rev+cp.getRm().getRev();
					}
				}

				wrt.write("\n");	
				wrt.write("\nRevenue of Cab  is :"+clm.getRevenue());
				wrt.write("\n----------------------------------------------------------------------------------------------------------------------------------");
				//System.out.println("\n\nKm run by cab is "+clm.getKm());
				//System.out.println("Dead Km are      "+clm.getDeadKM());

				trev = trev+clm.getRevenue();
			}

			wrt.write("\n\nTotal Revenue is :"+trev);

			int i=0;		
			for (RequestModel requestModel : reqList) {

				if(requestModel.getStatus() == 'C'){
					i++;

				}
				//System.out.println("reqest :"+requestModel.getReqID()+" status "+requestModel.getStatus());
			}
			wrt.write("\nTotal Request Served is :"+i);
		} catch (IOException ex) {
			// report
		} finally {
			try {wrt.close();} catch (Exception ex) {}
		}
	}




	/*************************************************************************************
	 * This function will mark  50% all the heavy requests in the path
	 * @param ArrayList<RequestModel> 
	 *************************************************************************************/
	public void getAvgLength() {

		int a=0;
		for (RequestModel rq : reqList) {
			ArrayList<Integer> node = fd.pathNodes(rq.getOrigin(), rq.getDest(), path);		
			a = a + reqList.get(0).getRev() / (node.size()-1);
		}
		avg = a/reqList.size() ;
		//System.out.println("avg length :"+ avg);


	}


	/*******************************************************************************************************
	 * This method will calculate extra cost to bear by allowing another request to share a particular cab
	 *******************************************************************************************************/
	public float getExtraCost(int riOrgn,int riDest,int rjOrgn,int rjDest)
	{

		int riDistance = distance(riOrgn, riDest);
		float ed = distance(rjOrgn, riOrgn) + distance(rjDest, riDest);

		return ed/riDistance;

	}
}
