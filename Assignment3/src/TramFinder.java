import javax.xml.soap.Node;
import java.util.LinkedList;
import java.util.List;



public class TramFinder { 
    
    // Assignment: Implement this!
    public static TramArrival[] fastFindRoute(TramNetwork nw, int starttime, TramNetwork.Station from, TramNetwork.Station to){

        Heap<NodeLengthEdgeTriplet> tripHeap = new Heap<NodeLengthEdgeTriplet>();
        NodeLengthEdgeTriplet[] nodes = new NodeLengthEdgeTriplet[nw.stations.length];
        //Kickstart the algorithm with all possibilities from the start
        for(TramNetwork.TramConnection tramConnection : from.tramsFrom) {
            tripHeap.add(new NodeLengthEdgeTriplet(tramConnection.to, tramConnection.tram.waitingTime(starttime,tramConnection.to), tramConnection));
        }

        boolean[] visited = new boolean[nw.stations.length];
        NodeLengthEdgeTriplet current = null;
        while(!tripHeap.isEmpty())
        {
            current = tripHeap.removeMin();
            if(current.node == to) {break;} //TODO;
            if(visited[current.node.id]){continue;}
            for (TramNetwork.TramConnection connection: current.node.tramsFrom)
            {

                NodeLengthEdgeTriplet nle = new NodeLengthEdgeTriplet(
                    connection.to,
                    current.time + connection.timeTaken + connection.tram.waitingTime(current.time, current.node),
                    connection);

                tripHeap.add(nle);

                if(nle.compareTo(nodes[current.node.id]) < 0)
                {
                    nodes[current.node.id] = nle;
                }
            }
            visited[current.node.id] = true;
        }

        if(current != null) {
            System.out.println(current);
        }
        System.out.println("Fast finding is not yet implemented...");
        return null;
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

    /** Represents a single arrival/departure of a tram at/from a station*/
    /*public static class TramTrip implements Comparable<TramTrip>{
        public final TramNetwork.TramConnection previous;
        public final TramNetwork.Tram tram;
        public final TramNetwork.Station from;
        public final TramNetwork.Station to;

        / Absolute time, in minutes from 00:00, possibly exceeds 24*60
        public final int time;

        public TramTrip(TramNetwork.Tram tram, TramNetwork.Station from, TramNetwork.Station to, int time) {
            this.tram = tram;
            this.from = from;
            this.to = to;
            this.time = time;
        }

        public TramTrip(TramNetwork.TramConnection tramConnection, TramNetwork.TramConnection previous, int startTime)
        {
            this.previous = previous;
            this.tram = tramConnection.tram;
            this.from = tramConnection.from;
            this.to = tramConnection.to;

            int waitTime = this.tram.waitingTime(startTime, this.from);

            this.time = startTime + waitTime;
        }


        public String toString(){
            int timeofday = time % (60*24),
                    hour = timeofday/60,
                    minute = timeofday%60;

            return (hour < 10 ? "0"+hour: hour)+":"+(minute < 10 ? "0"+minute: minute)+", Tram "+tram+" at " + to;

        }

        public int compareTo(TramTrip t) {
            return time - t.time;
        }
    }*/

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
            return Integer.compare(this.time, nodeLengthEdgeTriplet.time);
        }
    }
}





