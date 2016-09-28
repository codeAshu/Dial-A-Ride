package ride.iiitb.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ParallelShortestPath {

	private ExecutorService exec;
	private int numThreads;
	private int[] current;
	private int[] next;
	private int[] path;
	private int numNodes; 
	private boolean solved;

	private int getIndex(int i, int j){
		return i*numNodes+j; 
	}

	private int getI(int index){
		return index / numNodes; 
	}

	private int getJ(int index){ 
		return index % numNodes; 
	}
	
	public ParallelShortestPath(int numNodes, int[][] distances, int[][] path, ExecutorService exec, int numThreads){
		this.exec = exec; 
		this.numThreads = numThreads; 
		this.numNodes = numNodes;
		this.current = new int[numNodes*numNodes]; 
		this.next = new int[numNodes*numNodes];
		this.path = new int[numNodes*numNodes];
		
		for(int i = 0; i < numNodes; i++){
			for(int j = 0; j < numNodes; j++){
				current[getIndex(i,j)] = distances[i][j];
				this.path[getIndex(i,j)] = path[i][j];
			}
		}
		this.solved = false;
	}

    public ArrayList<int[]> solve(){
    	ArrayList<int[]> resultReturn = new ArrayList<>();
		if(solved){ 
			throw new RuntimeException("Already solved");
		} 
		for(int k = 0; k < numNodes; k++){
			List<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();
			if(current.length < numThreads){ 
				for(int i = 0; i < current.length; i++){ 
					tasks.add(new FloydWarshall(i, i+1, k)); 
				} 
			} else{ 
				for(int t = 0; t < numThreads; t++){ 
					int start = (t * current.length) / numThreads; 
					int end = ((t+1) * current.length) / numThreads; 
					tasks.add(new FloydWarshall(start, end, k));
				} 
			} 
			
			try {
				List<Future<Boolean>> results = exec.invokeAll(tasks);
				for(Future<Boolean> result : results){ 
					if(result.get().equals(false)){ 
						throw new RuntimeException(); 
					} 
				} 
			} catch (InterruptedException e) { 
				throw new RuntimeException(e); 
			} catch (ExecutionException e) { 
				throw new RuntimeException(e); 
			} 
			int[] temp = current; 
			current = next; 
			next = temp;
		}
		next = null; 
		solved = true;
		resultReturn.add(current);
		resultReturn.add(path);
		return resultReturn;
	}

    private class FloydWarshall implements Callable<Boolean>{

		private final int start;
		private final int end;
		private final int k;

		public FloydWarshall(int start, int end, int k){ 
			this.start = start; 
			this.end = end;
			this.k = k;
		}

		@Override public Boolean call() throws Exception { 
			for(int index = start; index < end; index++){
				int i = getI(index); 
				int j = getJ(index); 
				int temp = current[getIndex(i,k)] + current[getIndex(k,j)];
				if(temp < current[index]){
					next[index] = temp;
					path[index] = path[getIndex(k,j)];
				}
				else{
					next[index] = current[index];
				}
			}
		return true;
		}
	}
}