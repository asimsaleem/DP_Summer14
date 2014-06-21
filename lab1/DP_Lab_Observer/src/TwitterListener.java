import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class TwitterListener implements StatusListener, Subject {

  private Map<Observer, Set<String>> mapObservers = new HashMap<Observer, Set<String>>();
  
  /***** Start ***/
  private Set<String> trackSet = new HashSet<String>();
  private List<String> matchedDataForTest = new ArrayList<String>();
  /***** End *****/

  @Override 
  public void onStatus(Status status) {
    // you need to write some codes here 
	  
	  /****** Start **********/
	  //Get the text from the Status object. This constitutes the unfiltered twitter stream
	  String text = status.getText();
	  System.out.println("onStatus: Twitter has sent the Status: {" + status.getText() + "}");
	  //Invoke the notifyObservers method to notify all the registered users
	  System.out.println("onStatus: Notify Observers that a Message has been received");
	  notifyObservers(text);
	  /****** End ***********/
  }

  @Override
  public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
    System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
  }

  @Override
  public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
    System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
  }

  @Override
  public void onScrubGeo(long userId, long upToStatusId) {
    System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
  }

  @Override
  public void onStallWarning(StallWarning warning) {
    System.out.println("Got stall warning:" + warning);
  }

  @Override
  public void onException(Exception ex) {
    ex.printStackTrace();
  }

  @Override // implementing method defined in Subject interface
  public void notifyObservers(String text) {
    // you need to write some codes here 
	  
	  /****** Start **********/
	  System.out.println("notifyObservers: Number of Observers in the Map is: {" + mapObservers.size() + "}");
	 
	  boolean isDataValidForObserver = false;
	  
	  //Get the list of observers registered in the system from the Map and loop through it
	  for (Entry<Observer, Set<String>> entry : mapObservers.entrySet()){
		  
	      System.out.println("notifyObservers: Entry Key/Value is: {" + entry.getKey() + "/" + entry.getValue() + "}");
	      
	      //TODO: Check the Set<String> entry of the Observer Map to find out what Stream each Observer had registered to.
	      //If the registered string is found then update them else ignore that particular tweet for that observer while continuing to process
	      //the same tweet for other observers
	      
	      
	      //For each observer, get the reference to the observer object
		  if(entry.getKey()  != null){

			  System.out.println("notifyObservers: Registered Observer: " + entry.getValue());
			  Observer observer = (Observer)entry.getKey();
			  
			  //Check how many terms the current Observer subscribed for
			  Set<String> setofUserSubscriptions = entry.getValue();
			  for(String userSubscription: setofUserSubscriptions){
				  
				  //Check for individual terms
				  String lowerCaseText = text.toLowerCase();
				  String lowerCaseUserSubscription = userSubscription.toLowerCase();
				  if(lowerCaseText.contains(lowerCaseUserSubscription)){
					  //One of the Search terms is available in the text
					  //Set the Data valid Flag to TRUE
					  //Note that the Data valid flag will be set to TRUE everytime a match is found
					  isDataValidForObserver = true;
				  }
			  }//End of For loop which browses through the User Subscriptions
			  
			  System.out.println("noifyObservers: Is the Data Valid to be sent to the Observer?");
			  System.out.println("******************************Answer is: " + isDataValidForObserver + "*****************************");
			  //Update the observer with the updated text
			  if(isDataValidForObserver){				  
				  System.out.println("notifyObservers: Update the Observer with the Text");
				  observer.update(text);
				  System.out.println("notifyObservers: After updating the observer with the text");
			  }else{
				  System.out.println("notifyObservers: Data received does not contain all the terms Subscribed to by the Observer.Ignoring it...");
			  }
		  }
	  }//End of For loop which browses through the Map Of Observers Entry Set
	  /****** End ************/
  }

  @Override // implementing method defined in Subject interface
  public boolean removeObserver(Observer observer) {
	  
    boolean result = false;
    
    // you need to write some codes here 

    /************ Start ***********/
    //Get the desired observer from the Map
    System.out.println("removeObserver: Checking if the specified observer is in the Map");
    if(mapObservers.get(observer) != null){
    	System.out.println("removeObserver: Observer that has to be removed has been found");
    	mapObservers.remove(observer);
    	//Setting the resul to True to indicate that the record to be removed was removed successfully
    	result = true;
    }
    /************ End ************/
    
    return result;
  }

  @Override  // implementing method defined in Subject interface
  public boolean registerObserver(Observer observer, String track) {
 
	boolean result = false;
    
    // you need to write some codes here 
	/******* Start *********/
	System.out.println("registerObserver: Track: {" + track + "}");
	System.out.println("registerObserver: Observer: {" + observer + "}");

	//First create a Hashset of Strings	
	//Add the track to the newly created Hashset
	if(!trackSet.contains(track)){
		System.out.println("registerObserver: Add the track to trackset");
	    trackSet.add(track);
	}else{
		System.out.println("registerObserver: Track being requested is already present in the list. Skipping...");
	}
    
    //Put the Observer and the Trackset into the Map of Observers
    System.out.println("registerObserver: Put the trackset for the specific user in the map for Observers");
    if(!mapObservers.containsKey(observer)){
    	System.out.println("registerObserver: Adding the Observer and the corresponding trackset into the map of observers");
    	mapObservers.put(observer, trackSet);
    	System.out.println("registerObserver: Set the result to true since the new object was added freshly into the Map");
    	result = true; 
    }
    /******* End ***********/
    
    return result;
  }
}