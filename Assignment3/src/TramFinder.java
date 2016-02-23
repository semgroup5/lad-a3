import java.util.LinkedList;
import java.util.List;



public class TramFinder { 
    
    // Assignment: Implement this!
    public static TramArrival[] fastFindRoute(TramNetwork nw, int starttime, TramNetwork.Station from, TramNetwork.Station to){

        Heap<TramArrival> edges = new Heap<TramArrival>();

        //Kickstart the algorithm with all possibilities from the start
        for(TramNetwork.TramConnection tramConnection : from.tramsFrom) {
            edges.add(new TramArrival(tramConnection, starttime));
        }



        System.out.println("Fast finding is not yet implemented...");
        return null;
    }

    public static class TramConnectionEdge implements Comparable<TramConnectionEdge>{
        public final TramNetwork.TramConnection tc;
        public int duration;
        public TramNetwork.TramConnection previous;

        public TramConnectionEdge(TramNetwork.TramConnection tc, int duration, TramNetwork.TramConnection previous)
        {
            this.tc = tc;
            this.duration = duration;
            this.previous = previous;
        }

        public int compareTo(TramConnectionEdge tramArrivalEdge) {
            return this.tc.compareTo(tramArrivalEdge.tc);
        }
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

        public TramArrival(TramNetwork.TramConnection tramConnection, int startTime)
        {
            this.tram = tramConnection.tram;
            this.station = tramConnection.to;

            int waitTime = this.tram.waitingTime(startTime, this.station);

            this.time = startTime + waitTime;
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
}





