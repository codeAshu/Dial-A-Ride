package ride.iiitb.model;

public class RequestModel {

	int reqID;
	int origin;
	int dest;
	int tofReq;
	int tofExp;
	int length=2;
	int rev = 0;
	char status='O';
	int pic =-1;				//the time at which request is picked up at source
	int drop =-1;			//the time at which it is dropped at destination
	
	
	public int getPic() {
		return pic;
	}

	public void setPic(int pic) {
		this.pic = pic;
	}

	public int getDrop() {
		return drop;
	}

	public void setDrop(int drop) {
		this.drop = drop;
	}

	
	
	public RequestModel(int origin, int dest, int tmReq, int tmExp)
	{
	
		this.origin = origin;
		this.dest = dest;
		this.tofReq =  tmReq;
		this.tofExp = tmExp;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}
	/**
	 * @return the reqID
	 */
	public int getReqID() {
		return reqID;
	}

	/**
	 * @param reqID the reqID to set
	 */
	public void setReqID(int reqID) {
		this.reqID = reqID;
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
	 * @return the origin
	 */
	public int getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(int origin) {
		this.origin = origin;
	}

	/**
	 * @return the dest
	 */
	public int getDest() {
		return dest;
	}

	/**
	 * @param dest the dest to set
	 */
	public void setDest(int dest) {
		this.dest = dest;
	}

	/**
	 * @return the tofReq
	 */
	public int getTofReq() {
		return tofReq;
	}

	/**
	 * @param tofReq the tofReq to set
	 */
	public void setTofReq(int tofReq) {
		this.tofReq = tofReq;
	}

	/**
	 * @return the tofExp
	 */
	public int getTofExp() {
		return tofExp;
	}

	/**
	 * @param tofExp the tofExp to set
	 */
	public void setTofExp(int tofExp) {
		this.tofExp = tofExp;
	}
	/**
	 * @return the rev
	 */
	public int getRev() {
		return rev;
	}

	/**
	 * @param rev the rev to set
	 */
	public void setRev(int rev) {
		this.rev = rev;
	}

		
}
	