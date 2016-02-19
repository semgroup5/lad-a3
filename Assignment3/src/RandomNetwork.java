import java.util.ArrayList;
import java.util.Random;

public class RandomNetwork{

    static Random r = new Random();
    
    public static void main(String[] args){
        
        // first parameter is number of stops, second is number of tram lines
        TramNetwork nw = random(1000,500);
        
        
        System.out.println("Done");
        
        findRand(nw, 0);
        
    }
    
    private static void findRand(TramNetwork nw, int time){
        TramNetwork.Station sfrom = nw.stations[r.nextInt(nw.stations.length)],
                            sto   = nw.stations[r.nextInt(nw.stations.length)];
        System.out.println("Finding route from " + sfrom+ " to " + sto);
        TramFinder.TramArrival[] deps = TramFinder.fastFindRoute(nw, time, sfrom, sto);
        if (deps!=null){
        System.out.println("Route length: " + deps.length);
        for(TramFinder.TramArrival tc:deps) 
            System.out.println(tc);
        } else
            System.out.println("No route found");
        
        System.out.println("Search complete!");
    }
    
    
    
    
    
    static TramNetwork random(int numNodes, int numTrams){
      
        String[] names = new String[numNodes];
      
        for(int i = 0; i<numNodes;i++) names[i] = "N"+i;
        
        TramNetwork nw = new TramNetwork(names);

        for (int i = 0; i < numTrams ; i++)
            randomTram(nw,i);
        
        
      
      
        return nw;
      
    }
    
    
    static void randomTram(TramNetwork nw, int id){
        TramNetwork.Station[] stations = nw.stations;
        
        ArrayList<TramNetwork.Station> l = new ArrayList<TramNetwork.Station>();

        
        for(int i = 0; i < stations.length; i++)
            l.add(stations[i]);
        
        TramNetwork.StationTime[] stopTimes = new TramNetwork.StationTime[r.nextInt(stations.length/2)+stations.length/2];
        
        int time = 0;
        for (int i = 0;i<stopTimes.length;i++){
            time += r.nextInt(25)+1;
            TramNetwork.Station next = l.remove(r.nextInt(l.size()));
            stopTimes[i]=nw.new StationTime(next, time);
        }

        Integer[] startTimes = new Integer[60];
        for (int i = 0;i<60;i++)
            startTimes[i]=24*i+r.nextInt(23);
        

        nw.addTram("T"+id, startTimes, stopTimes);

        
        
    }
    

}


