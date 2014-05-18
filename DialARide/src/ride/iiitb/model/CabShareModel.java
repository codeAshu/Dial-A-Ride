package ride.iiitb.model;

public class CabShareModel{

	RequestModel rm;
	CabLocationModel cl;
	int occup =0;
	char status = 'R';	//R = running, F = Finish the request
	
	
	public CabShareModel(RequestModel rm, CabLocationModel cl)
	{
	
		this.rm = rm;
		this.cl = cl;
	}


	/**
	 * @return the status
	 */
	public char getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(char status) {
		this.status = status;
	}


	
	/**
	 * @return the rm
	 */
	public RequestModel getRm() {
		return rm;
	}


	/**
	 * @param rm the rm to set
	 */
	public void setRm(RequestModel rm) {
		this.rm = rm;
	}


	/**
	 * @return the cl
	 */
	public CabLocationModel getcl() {
		return cl;
	}


	/**
	 * @param cl the cl to set
	 */
	public void setRd(CabLocationModel cl) {
		this.cl = cl;
	}
	
		/**
	 * @return the occup
	 */
	public int getOccup() {
		return occup;
	}


	/**
	 * @param occup the occup to set
	 */
	public void setOccup(int occup) {
		this.occup = occup;
	}


}

