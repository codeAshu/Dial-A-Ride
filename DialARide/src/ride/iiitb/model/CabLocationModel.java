package ride.iiitb.model;

public class CabLocationModel {
	int cab;
	int location;
	int timestamp=0;
	int occupancy=0;
	int revenue =0;
	int nextDest= -1;
	int finalDest = -1;
	int km = 0;
	int deadKM =0;
	char status = 'O';
	int timer =0;
	/**
	 * @return the timer
	 */
	public int getTimer() {
		return timer;
	}

	/**
	 * @param timer the timer to set
	 */
	public void setTimer(int timer) {
		this.timer = timer;
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
	 * @return the deadKM
	 */
	public int getDeadKM() {
		return deadKM;
	}

	/**
	 * @param deadKM the deadKM to set
	 */
	public void setDeadKM(int deadKM) {
		this.deadKM = deadKM;
	}

	/**
	 * @return the km
	 */
	public int getKm() {
		return km;
	}

	/**
	 * @param km the km to set
	 */
	public void setKm(int km) {
		this.km = km;
	}

	/**
	 * @return the revenue
	 */
	public int getRevenue() {
		return revenue;
	}

	/**
	 * @return the nextDest
	 */
	public int getNextDest() {
		return nextDest;
	}

	/**
	 * @param nextDest the nextDest to set
	 */
	public void setNextDest(int nextDest) {
		this.nextDest = nextDest;
	}

	/**
	 * @return the finalDest
	 */
	public int getFinalDest() {
		return finalDest;
	}

	/**
	 * @param finalDest the finalDest to set
	 */
	public void setFinalDest(int finalDest) {
		this.finalDest = finalDest;
	}

	/**
	 * @param revenue the revenue to set
	 */
	public void setRevenue(int revenue) {
		this.revenue = revenue;
	}

	/**
	 * @return the cabLocationID
	 */
	
	public CabLocationModel(int cab, int location, int timestamp, int occupancy)
	{
		this.cab = cab;
		this.location = location;
		this.timestamp = timestamp;
		this.occupancy = occupancy;
	}

	/**
	 * @return the occupancy
	 */
	public int getOccupancy() {
		return occupancy;
	}

	/**
	 * @param occupancy the occupancy to set
	 */
	public void setOccupancy(int occupancy) {
		this.occupancy = occupancy;
	}

	public CabLocationModel() {}

	/**
	 * @return the cab
	 */
	public int getCab() {
		return cab;
	}

	/**
	 * @param cab the cab to set
	 */
	public void setCab(int cab) {
		this.cab = cab;
	}

	/**
	 * @return the location
	 */
	public int getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(int location) {
		this.location = location;
	}

	/**
	 * @return the timestamp
	 */
	public int getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}


}
