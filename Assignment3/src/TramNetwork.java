import java.util.ArrayList;
import java.util.List;


public class TramNetwork{
    
    private List<Tram> trams = new ArrayList<Tram>();
    private SortedList<Station> stationList;
    
    /** The set of stations in the network */
    public final Station[] stations;
    
    public TramNetwork(String[] stationNames){
        stations = new Station[stationNames.length];
        for(int i = 0; i < stationNames.length; i++)
            stations[i] = new Station(stationNames[i],i);
        stationList = new SortedListImpl<Station>();
        for(Station s : stations) stationList.add(s);
    }
    
    /** Finds the stop with the given name, or the first one after it in alphabetical order, O(log N)*/
    public Station findStation(String stationName){
        Station searchStation = new Station(stationName, 0);
        int index = stationList.firstIndex(searchStation);
        return stationList.get(index % stationList.size());
    }
    
    
    public void addTram(String tramName, Integer[] startTimes, StationTime[] stopTimes){
        this.new Tram(tramName, startTimes, stopTimes);
    }
    
    /** Represents a tram station */
    public class Station implements Comparable<Station>{
        public final String name;
        public final int id;
        
        /** A list of tram lines leaving this station */
        public final List<TramConnection> tramsFrom;

        public Station(String name, int id) {
            this.name = name;
            this.id = id;
            tramsFrom = new ArrayList<TramConnection>();
        }

        private void addConnection(TramConnection tc) {
            tramsFrom.add(tc);
        }

        public boolean equals(Station s){
            return s.id==id;
        }
        
        public int compareTo(Station stop) {
            return name.compareTo(stop.name);
        }

        public String toString(){
            return name;
        }
    }
    
    /** Represents a tram moving from one stop to the next, without stopping.
     * Includes the time (in minutes) it takes for the tram to move.
     * */
    public class TramConnection {
        public final Tram tram;
        public final Station from;
        
        /** The next stop on this tram line after from */
        public final Station to;
        
        /** The time in minutes it takes to move between from and to. */
        public final int timeTaken; 

        private TramConnection(Tram tram, Station from, Station to, int time) {
            this.tram = tram;
            this.from = from;
            this.to = to;
            this.timeTaken = time;

        }
    }
    
    /** Represents a tram line */
    public class Tram{

        public final String name;
        
        private SortedList<Integer> startTimes;
        
        private TramConnection[] edges;
        private int[] stopTimes;
        
        private Tram(String name, Integer[] startTimes, StationTime[] stopTimes) {
            this.startTimes = new SortedListImpl<Integer>();
            this.startTimes.addSortedArray(startTimes);
            
            this.name = name;
            
            this.stopTimes = new int[stations.length];
            for (int i = 0;i<stations.length;i++) this.stopTimes[i] = -1;
            for (int i = 0;i<stopTimes.length;i++) 
                this.stopTimes[stopTimes[i].stopsAt.id] = 
                  stopTimes[i].stopTime;
            
            this.edges = new TramConnection[stopTimes.length-1];
            
            for(int i = 0;i<stopTimes.length-1;i++){
                StationTime from = stopTimes[i];   
                StationTime to = stopTimes[i+1];
                int time = to.stopTime-from.stopTime;
                
                TramConnection tc = new TramConnection(this, from.stopsAt, to.stopsAt, time);
                edges[i] = tc;
                from.stopsAt.addConnection(tc);
            }
            
            trams.add(this);
        }
        
        
        /**
         * Does this tram stop at this station?
         * Complexity: O(1)
         * @param station
         * @return true iff the tram stops at station. 
         */
        public boolean stopsAt(Station station){
            return stopTimes[station.id] >= 0;
        }
        
        /**
         * Complexity: O(1)
         * @param station
         * @return The time in minutes from the start of the run that this tram leaves station
         */
        public int travelTimeTo(Station station){
            if (!stopsAt(station))
                throw new RuntimeException("Tram does not stop here");
            return stopTimes[station.id];
        }
        
        /**
         * The time in minutes you will have to wait at station for this tram to arrive, if you get there on currentTime.
         * @param currentTime Time of day, in minutes since 00:00
         * @param station The station to leave from
         * @return Number of minutes of waiting from currentTime for this tram to leave station
         */
        public int waitingTime(int currentTime, Station station){
            int tt = travelTimeTo(station),
                idealDep = (currentTime - tt) % (60*24);
            
            if (idealDep < 0) idealDep = idealDep + 60*24;

            int next = startTimes.firstIndex(idealDep);
            int actualDep;
            
            if (next == startTimes.size())
                actualDep = 60*24+startTimes.get(0);
            else
                actualDep = startTimes.get(next);
            
            return actualDep - idealDep;
        }
        
        public String toString(){
            return name;
        }
    }
    
    /** Represents the arrival/departure of a tram at/from a station, and the 
     * time in minutes it takes a tram to get to the station from its starting point.
     */
    public class StationTime {
        public final Station stopsAt;
        /** The time in minutes since the tram started to get to this stop*/
        public final int stopTime;
        public StationTime(Station stopsAt, int stopTime) {
            this.stopsAt = stopsAt;
            this.stopTime = stopTime;
        } 
        
        public StationTime(String stopsAt, int stopTime){
            this(findStation(stopsAt), stopTime);
        }
    }
}