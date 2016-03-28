import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;

class Animal{
	int animalId;
	int animalSpecies;
	HashMap<String, Integer> feedFrequency;
	HashMap<String, Integer> feedQuantity;
	
	protected Animal(int animalId, int speciesId){
		this.animalId = animalId;
		this.animalSpecies = speciesId;
		this.feedFrequency = new HashMap<String, Integer>();
		this.feedQuantity = new HashMap<String, Integer>();
	}
	
	protected HashMap<String, Integer> getFeedFrequency() {
		return feedFrequency;
	}
	protected void setFeedFrequency(HashMap<String, Integer> feedFrequency) {
		this.feedFrequency = feedFrequency;
	}
	protected HashMap<String, Integer> getFeedQuantity() {
		return feedQuantity;
	}
	protected void setFeedQuantity(HashMap<String, Integer> feedQuantity) {
		this.feedQuantity = feedQuantity;
	}
	protected Animal(int animalId, int animalSpecies,
			HashMap<String, Integer> feedFrequency,
			HashMap<String, Integer> feedQuantity) {
		super();
		this.animalId = animalId;
		this.animalSpecies = animalSpecies;
		this.feedFrequency = feedFrequency;
		this.feedQuantity = feedQuantity;
	}
	protected int getAnimalId() {
		return animalId;
	}
	protected void setAnimalId(int animalId) {
		this.animalId = animalId;
	}
	protected int getAnimalSpecies() {
		return animalSpecies;
	}
	protected void setAnimalSpecies(int animalSpecies) {
		this.animalSpecies = animalSpecies;
	}
	protected void feedAnimal(int feedId, int feedQuantity2, String feedDate) {
		if(feedFrequency.containsKey(feedDate)){
			int currentFeedFreq = feedFrequency.get(feedDate);
			feedFrequency.put(feedDate, currentFeedFreq+1);
		}
		else{
			feedFrequency.put(feedDate, 1);
		}
		if(feedQuantity.containsKey(feedDate)){
			int currentFeedQuantity = feedQuantity.get(feedDate);
			feedQuantity.put(feedDate, currentFeedQuantity+feedQuantity2);
		}
		else{
			feedQuantity.put(feedDate, feedQuantity2);
		}
	}
	protected double getAvgQuantity() {
		double freq =0.0;
		for(Map.Entry<String, Integer> entry: feedQuantity.entrySet()){
			freq += entry.getValue();
		}
		freq = freq/feedQuantity.size();
		return freq;
	}
	protected double getAvgFrequency() {
		double freq =0.0;
		for(Map.Entry<String, Integer> entry: feedFrequency.entrySet()){
			freq += entry.getValue();
		}
		freq = freq/feedFrequency.size();
		return freq;
	}
	
	
}