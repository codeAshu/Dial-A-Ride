//as soon as the cab is matched to a request 

t <-- Time for a cab to reach at the request location

//add t to Timestamp of the cab
Currentcabtime = t+CabTimestamp

//if request was valid at CurrentCabTime  than we can just pick up the request at CurrentCabTime because cab is there at this time

update cab timestamp as CurrentCabTime

//If request was not valid at CurrentCabTime It means cab has reached the request location before the request has occure

update CurrentCabTime as the requestTime
	
//Cab has waited at that location untill the request has been occure
	
update cab location

update cab occupancy

update next and final destination

update revenue of the cab

close the request

Add to sharing model