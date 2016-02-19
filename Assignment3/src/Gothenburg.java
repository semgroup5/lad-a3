import java.util.LinkedList;



public class Gothenburg {

    public static final TramNetwork GOTHENBURG;

    public static void main(String[] args){
        findGBG(0,"Centralstationen","Chalmers");
        
        findGBG(0,"Lilla Bommen","Chalmers");
        
        findGBG(23*60+44,"Lilla","Vasa V");
        
        findGBG(23*60+50,"Hjal","Chal");
        
        findGBG(23*60+50,"Hjal","Kor");
        
        findGBG(23*60+50,"Kor","Chal");
        
    }
    
    private static void findGBG(int time, String from, String to){
        TramNetwork.Station sfrom = GOTHENBURG.findStation(from),
                            sto   = GOTHENBURG.findStation(to);
        System.out.println("Finding route from " + sfrom+ " to " + sto);
        TramFinder.TramArrival[] deps = TramFinder.findRoute(GOTHENBURG, time, sfrom, sto);
        System.out.println("Route length: " + deps.length);
        for(TramFinder.TramArrival tc:deps) 
            System.out.println(tc);
        System.out.println("***");
        
        
        deps = TramFinder.fastFindRoute(GOTHENBURG, time, sfrom, sto);
        if (deps!=null){
            System.out.println("Route length: " + deps.length);
        
            for(TramFinder.TramArrival tc:deps) 
                System.out.println(tc);
        }
        System.out.println();
    }
    
    
    
    private enum GBG
       { Brunnsparken
       , Centralstationen
       , Nordstan
       , Kungsportsplatsen
       , Valand
       , Vasaplatsen
       , Gronsakstorget("Grönsakstorget")
       , Kapellplatsen
       , Chalmers
       , Domkyrkan
       , LillaBommen("Lilla Bommen")
       , Frihamnen
       , HjalmarBrantingsplatsen("Hjalmar Brantingsplatsen")
       , Berzeligatan
       , Korsvagen("Korsvägen")
       , VasaViktoriagatan("Vasa Viktoriagatan")
       ;
       
        //Line 10 (A)
        public static TimeToReach[] l10At = 
                {t(HjalmarBrantingsplatsen, 0)
                ,t(Frihamnen,               1)
                ,t(LillaBommen,             5)
                ,t(Brunnsparken,            6)
                ,t(Kungsportsplatsen,       7)
                ,t(Valand,                  9)
                ,t(Vasaplatsen,             10)
                ,t(Kapellplatsen,           12)
                ,t(Chalmers,                13)
                };

        
       
       // Line 5 (A)
       public static TimeToReach[] l5At  = 
                {t(HjalmarBrantingsplatsen, 0)
                ,t(Frihamnen,               1)
                ,t(LillaBommen,             5)
                ,t(Brunnsparken,            6)
                ,t(Kungsportsplatsen,       7)
                ,t(Valand,                  9)
                ,t(Berzeligatan,            11)
                ,t(Korsvagen,               12)
                };
       
       //Line 2 (A)
       public static TimeToReach[] L2At = 
                {t(Centralstationen,        0)
                ,t(Brunnsparken,            2)
                ,t(Domkyrkan,               5)
                ,t(Gronsakstorget,          6)
                ,t(Vasaplatsen,             7)
                ,t(VasaViktoriagatan,       8)
                };


       private String name;
       
       private GBG(String s){
           this.name = s;
       }
       
       private GBG(){
           name = this.name(); 
       }
       
       public String toString(){return name;}
    }
    
    private static class TimeToReach{
        public final GBG stop;
        public final int time;
        public TimeToReach(GBG stop, int time) {
            this.stop = stop;
            this.time = time;
        }
    }
    static TimeToReach t(GBG g,int t){
        return new TimeToReach(g,t);
    }
    
    static {
        int numStops=GBG.values().length;
        
        String[] names = new String[numStops];
        
        for(GBG x : GBG.values()) names[x.ordinal()] = x.toString();

        GOTHENBURG = new TramNetwork(names);
        
        
        // These are tram start times. Could be made more realistic e.g. with fewer trams at night.
        LinkedList<Integer> start10 = new LinkedList<Integer>();
        for (Integer i = 10; i < 60*24; i+=20)
             start10.add(i);
        
        LinkedList<Integer> start2 = new LinkedList<Integer>();
        for (Integer i = 4; i < 60*24; i+=11)
             start2.add(i);
      
        LinkedList<Integer> start5 = new LinkedList<Integer>();
        for (Integer i = 7; i < 60*24; i+=12)
             start5.add(i);
        
        addGBGTram(GOTHENBURG, "10", start10.toArray(new Integer[0]), GBG.l10At);
        
        addGBGTram(GOTHENBURG, "2", start2.toArray(new Integer[0]), GBG.L2At);
        
        addGBGTram(GOTHENBURG, "5", start5.toArray(new Integer[0]), GBG.l5At);
    }

    static void addGBGTram(TramNetwork nw, String num, Integer[] startTimes, TimeToReach[] ttr){
        
        String name = num + " towards " + ttr[ttr.length-1].stop;
        
        TramNetwork.StationTime[] stopTimes = new TramNetwork.StationTime[ttr.length];
        for (int i = 0; i < ttr.length; i++){
            stopTimes[i] = nw.new StationTime(ttr[i].stop.toString(), ttr[i].time);
        }
        
        nw.addTram(name, startTimes, stopTimes);
        
        // Add tram in reverse direction as well
        stopTimes = new TramNetwork.StationTime[ttr.length];
        int tmax = ttr[ttr.length-1].time;
        for (int i = 0; i < ttr.length; i++)
            stopTimes[i] = nw.new StationTime(ttr[ttr.length-i-1].stop.toString(), tmax-ttr[ttr.length-i-1].time);
        
        name = num + " towards " + ttr[0].stop;
        
        nw.addTram(name, startTimes, stopTimes);
    }

}


