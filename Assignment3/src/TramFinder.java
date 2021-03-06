

import java.util.LinkedList;
import java.util.List;



public class TramFinder { 
    
    // Assignment: Implement this!
    public static TramArrival[] fastFindRoute(TramNetwork nw, int starttime, TramNetwork.Station from, TramNetwork.Station to){
        Heap<StationTimeConnection> tripHeap = new Heap<StationTimeConnection>();
        StationTimeConnection[] nodes = new StationTimeConnection[nw.stations.length];
        boolean[] visited = new boolean[nw.stations.length];

        //Kickstart the algorithm with all possibilities from the start
        for(TramNetwork.TramConnection tramConnection : from.tramsFrom) {
            tripHeap.add(new StationTimeConnection(from, starttime + tramConnection.tram.waitingTime(starttime, from), tramConnection));
        }

        StationTimeConnection current = null;
        while(!tripHeap.isEmpty())
        {
            current = tripHeap.removeMin();
            if(visited[current.station.id]){continue;}
            //For all connections from this station...
            for (TramNetwork.TramConnection connection: current.station.tramsFrom)
            {
                //...add them to the heap
                tripHeap.add(new StationTimeConnection(
                    connection.to,
                    current.time + connection.timeTaken + connection.tram.waitingTime(current.time, connection.from),
                    connection));
            }

            //If the path we took to the current node is an improvement...
            if(nodes[current.station.id] == null || current.compareTo(nodes[current.station.id]) < 0) {
                //...replace it
                nodes[current.station.id] = current;
            }
            //Set this node as visited
            visited[current.station.id] = true;
        }

        //Create path array (will never be longer than all the nodes)
        TramArrival[] path = new TramArrival[nodes.length];

        //Starting from destination
        StationTimeConnection curr = nodes[to.id];
        int i = 0;
        for(;i < nodes.length;i++) {
            //Add the current arrival
            path[i] = new TramArrival(curr.connection.tram, curr.connection.to, curr.time);
            //Get the next arrival in the path
            curr = nodes[curr.connection.from.id];
            if(curr == nodes[from.id]){
                //Add the from node
                path[++i] = new TramArrival(curr.connection.tram, from, curr.time);
                //Quit the loop
                break;
            }
        }

        //If no path found
        if(curr != nodes[from.id]){return new TramArrival[0];}

        //Reverse the path
        TramArrival[] pathReversed = new TramArrival[i+1]; // i is leftover from the for-loop
        for (int j=i; j >= 0; j--){
            pathReversed[i - j] = path[j];
        }
        return pathReversed;
    }



    ////////////////////////////////
    //   Below is the provided    //
    //  code from the assignment  //
    ////////////////////////////////

    // This works but is not fast enough. 
    public static TramArrival[] findRoute(TramNetwork nw, int starttime, TramNetwork.Station from, TramNetwork.Station to){
       Finder f = new Finder(nw); 
       f.findRoute(starttime, from, to);
       return f.bestPath;
    }
    
    private static class Finder {
        private boolean[] visited;
        private LinkedList<TramArrival> currentPath = new LinkedList<TramArrival>();
        
        private int bestTime = -1;
        TramArrival[] bestPath = null;
        
        public Finder(TramNetwork nw) {
            visited = new boolean[nw.stations.length];
        }

        
        public void findRoute(int currentTime, TramNetwork.Station from, TramNetwork.Station to){
            
            if (visited[from.id])
                return;
            if (from.equals(to)){
                if (bestTime == -1 || currentTime < bestTime){
                    bestTime = currentTime;
                    currentPath.addLast(new TramArrival(currentPath.getLast().tram, to, currentTime));
                    bestPath = currentPath.toArray(new TramArrival[0]);
                    currentPath.removeLast();
                }
                return;
            }
            
            
            visited[from.id]=true;
            
            List<TramNetwork.TramConnection> fromHere = from.tramsFrom;
            for(TramNetwork.TramConnection conn : fromHere ){
                
                int waitTime = conn.tram.waitingTime(currentTime,from);
                
                TramArrival dep = new TramArrival(conn.tram, from, currentTime+waitTime);
                
                currentPath.addLast(dep);
                findRoute(dep.time+conn.timeTaken, conn.to, to);
                currentPath.removeLast();
            }
            
            visited[from.id]=false;
            
        }
    
    }
    
    /** Represents a single arrival/departure of a tram at/from a station*/
    public static class TramArrival implements Comparable<TramArrival>{
        public final TramNetwork.Tram tram;
        public final TramNetwork.Station station;
        
        /** Absolute time, in minutes from 00:00, possibly exceeds 24*60 */
        public final int time;
        
        public TramArrival(TramNetwork.Tram tram, TramNetwork.Station station, int time) {
            this.tram = tram;
            this.station = station;
            this.time = time;
        }

        public String toString(){
            int timeofday = time % (60*24),
                hour = timeofday/60,
                minute = timeofday%60;
            
            return (hour < 10 ? "0"+hour: hour)+":"+(minute < 10 ? "0"+minute: minute)+", Tram "+tram+" at " + station;
            
        }

        public int compareTo(TramArrival t) {
            return time - t.time;
        }
    }

    public static class StationTimeConnection implements Comparable<StationTimeConnection>
    {
        public final TramNetwork.Station station;
        public int time;
        public TramNetwork.TramConnection connection;

        public StationTimeConnection(TramNetwork.Station station, int time, TramNetwork.TramConnection connection) {
            this.station = station;
            this.time = time;
            this.connection = connection;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public void setConnection(TramNetwork.TramConnection connection) {
            this.connection = connection;
        }

        @Override
        public int compareTo(StationTimeConnection stationTimeConnection) {
            return this.time - stationTimeConnection.time;
        }

        public String toString()
        {
            return "Station " + station.name + " : from:  " + connection.from.name + " to " + connection.to.name + " time " + time;
        }
    }
}





