

import javax.xml.soap.Node;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;



public class TramFinder { 
    
    // Assignment: Implement this!
    public static TramArrival[] fastFindRoute(TramNetwork nw, int starttime, TramNetwork.Station from, TramNetwork.Station to){

        Heap<NodeLengthEdgeTriplet> tripHeap = new Heap<NodeLengthEdgeTriplet>();
        NodeLengthEdgeTriplet[] nodes = new NodeLengthEdgeTriplet[nw.stations.length];

        boolean[] visited = new boolean[nw.stations.length];

        //Kickstart the algorithm with all possibilities from the start
        for(TramNetwork.TramConnection tramConnection : from.tramsFrom) {
            tripHeap.add(new NodeLengthEdgeTriplet(from, starttime + tramConnection.tram.waitingTime(starttime,from), tramConnecKtion));
        }

        NodeLengthEdgeTriplet current = null;
        while(!tripHeap.isEmpty())
        {
            current = tripHeap.removeMin();
            if(visited[current.node.id]){continue;}
            for (TramNetwork.TramConnection connection: current.node.tramsFrom)
            {
                NodeLengthEdgeTriplet NLE = new NodeLengthEdgeTriplet(
                    connection.to,
                    current.time + connection.timeTaken + connection.tram.waitingTime(current.time, current.node),
                    connection);

                tripHeap.add(NLE);
                if(nodes[connection.to.id] == null || NLE.compareTo(nodes[connection.to.id]) < 0)
                {
                    nodes[connection.to.id] = NLE;
                }
            }
            visited[current.node.id] = true;
        }


        NodeLengthEdgeTriplet curr = nodes[to.id];
        int i = 0;
        TramArrival[] path = new TramArrival[nodes.length];
        path[i++] = new TramArrival(curr.edge.tram, curr.edge.to, curr.time);
        while(!curr.node.equals(from) && i < nodes.length - 1 ) {
            path[i++] = new TramArrival(curr.edge.tram ,curr.edge.from, curr.time);
            curr = nodes[curr.edge.from.id];
        }

        i--;
        TramArrival[] pathReversed = new TramArrival[i+1];
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

    public static class NodeLengthEdgeTriplet implements Comparable<NodeLengthEdgeTriplet>
    {
        public final TramNetwork.Station node;
        public int time;
        public TramNetwork.TramConnection edge;

        public NodeLengthEdgeTriplet(TramNetwork.Station node, int time, TramNetwork.TramConnection edge) {
            this.node = node;
            this.time = time;
            this.edge = edge;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public void setEdge(TramNetwork.TramConnection edge) {
            this.edge = edge;
        }

        @Override
        public int compareTo(NodeLengthEdgeTriplet nodeLengthEdgeTriplet) {
            return this.time - nodeLengthEdgeTriplet.time;
        }

        public String toString()
        {
            return "Station " + node.name + " : from:  " + edge.from.name + " to " + edge.to.name + " time " + time;
        }
    }
}





