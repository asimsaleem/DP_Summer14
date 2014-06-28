import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class InstrumentedAirportScraper implements AirportScraperInterface {

	/**START**/
	protected AirportScraperInterface airportScraperIntf;
	private  static Map<String, Double> destinationsCacheLookup = new HashMap<String, Double>(); 
   	private  List<String> listOfCommonDestinations = new ArrayList<String>();
   	private  Map<String, Integer> commonDestinationsCacheLookup = new HashMap<String, Integer>();
	
	//Implement the Constructors Here
	 public InstrumentedAirportScraper(AirportScraperInterface airportScraperIntf) {
		 this.airportScraperIntf = airportScraperIntf;
	 }
	/**END***/
	
	
    // sorted by most commonly called destinations first,
    // ties broken alphabetically. ok to consider case, i.e.,
    // treat AUS and aus differently.  see test suite
    // for exact format
    public List<String> mostCommonDestinations() {
    	
    	/** START **/
    	//Get the values from the HashMap
    	Set<Entry<String, Integer>> set = commonDestinationsCacheLookup.entrySet();
        List<Entry<String, Integer>> sortList = new ArrayList<Entry<String, Integer>>(set);
        Collections.sort(sortList, new Comparator<Map.Entry<String, Integer>>(){
            public int compare( Map.Entry<String, Integer> comparer, Map.Entry<String, Integer> comparee){
                return (comparee.getValue()).compareTo(comparer.getValue());
            }
        });
        
        //Iterate through the Sorted list and return the results in a list of String
        for(Map.Entry<String, Integer> entry: sortList){
            System.out.println(entry.getKey()+":"+entry.getValue());
            listOfCommonDestinations.add(entry.getKey()+":"+entry.getValue());
        }
    	return listOfCommonDestinations;
    	/** END **/
    }

	@Override
	public double lookupDistance(String dest) {
		/**START**/
		System.out.println("************Destination being processed is: " + dest + "*******************");
		//System.out.println("Inside lookupDistance of the InstrumentedAirportScraper");
		//System.out.println("Size of the Destinations Cache is: " + destinationsCacheLookup.size());
		//System.out.println("Content of the Cache is: " + destinationsCacheLookup);

		double calculatedDistance = 0; 
		
		//Check if Destination exists in the cache. 
		if(destinationsCacheLookup != null && destinationsCacheLookup.containsKey(dest)){
			//Destination exists in the cache. Don't make a Lookup Distance Call anymore
			calculatedDistance = destinationsCacheLookup.get(dest);
			//System.out.println("Distance Retrieved from the Cache is: " + calculatedDistance);
		}else{
			//Destination does not exist in the cache. Make a Lookup Distance call
			calculatedDistance =  airportScraperIntf.lookupDistance(dest);
			//Add the distance to the Cache
			//System.out.println("Distance Calculated from the Airport Scraper is: " + calculatedDistance);
			destinationsCacheLookup.put(dest, calculatedDistance);
			//System.out.println("lookupDistance returned is: " + calculatedDistance);
		}
		
		//Update the Count in the Hash Table if the specific Destination exists. 
		//If not, then add it fresh and initialize it to a count of one
		if(commonDestinationsCacheLookup != null){
			
			//Check if the destination required exists
			if(!commonDestinationsCacheLookup.isEmpty()){
				System.out.println("Destination Lookup is not EMPTY. It has the content size of " + commonDestinationsCacheLookup.size());
				System.out.println("Current content in the Destination Lookup is: " + commonDestinationsCacheLookup);
				if(commonDestinationsCacheLookup.containsKey(dest)){
					//Indicates that a record already exists. So increment the corresponding Destinations counter by 1
					System.out.println("Destination {" + dest + "} already existed. Incrementing its count");
					commonDestinationsCacheLookup.put(dest, commonDestinationsCacheLookup.get(dest) + 1);
				}else{
					//Indicated that NO record currently exists for the destination. Add a new record with an initial count of 1
					System.out.println("Destination {" + dest + "} did not exist. Setting its count to 1");
					commonDestinationsCacheLookup.put(dest, 1);
				}
			}else{
				//Indicated that NO record currently exists in the Map. Add a new record with an initial count of 1
				//System.out.println("Destination {" + dest + "} did not exist. Setting its count to 1: " );
				commonDestinationsCacheLookup.put(dest, 1);
			}
		}
		
		
		return calculatedDistance;
		/**END***/
	}

}