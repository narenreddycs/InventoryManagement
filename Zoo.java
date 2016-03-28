import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;

class Zoo{
	int zooId;
	HashMap<Integer,HashSet<Animal>> speciesList;
	HashMap<Integer, Animal> animalSet;
	HashMap<Integer,Integer> feedLevel;
	int wastage = 0;
	
	protected Zoo(int zooId, HashMap<Integer, HashSet<Animal>> speciesList,
			HashMap<Integer, Animal> animalSet,
			HashMap<Integer, Integer> feedLevel, int wastage) {
		super();
		this.zooId = zooId;
		this.speciesList = speciesList;
		this.animalSet = animalSet;
		this.feedLevel = feedLevel;
		this.wastage = wastage;
	}
	protected Zoo(int zooId){
		this.zooId = zooId;
		this.animalSet = new HashMap<Integer, Animal>();
		this.feedLevel = new HashMap<Integer,Integer>();
		this.speciesList = new HashMap<Integer,HashSet<Animal>>();
	}

	
	protected HashMap<Integer, HashSet<Animal>> getSpeciesList() {
		return speciesList;
	}
	protected void setSpeciesList(HashMap<Integer, HashSet<Animal>> speciesList) {
		this.speciesList = speciesList;
	}
	protected int getZooId() {
		return zooId;
	}
	protected void setZooId(int zooId) {
		this.zooId = zooId;
	}
	protected HashMap<Integer, Animal> getAnimalSet() {
		return animalSet;
	}
	protected void setAnimalSet(HashMap<Integer, Animal> animalSet) {
		this.animalSet = animalSet;
	}
	protected HashMap<Integer, Integer> getFeedLevel() {
		return feedLevel;
	}
	protected void setFeedLevel(HashMap<Integer, Integer> feedLevel) {
		this.feedLevel = feedLevel;
	}
	protected int getWastage() {
		return wastage;
	}
	protected void setWastage(int wastage) {
		this.wastage = wastage;
	}
	protected void inventeryUpdate(int feedId, int feedLvl){
		if(feedLevel.containsKey(feedId)){
		int currentLvl = feedLevel.get(feedId);
		feedLevel.put(feedId, currentLvl+feedLvl);
		}
		else{
			feedLevel.put(feedId, feedLvl);
		}
	}
	protected void inventeryValidate(int feedId, int feedLvl){
		if(feedLevel.containsKey(feedId)){
		int currentLvl = feedLevel.get(feedId);
		wastage+=(feedLvl-currentLvl);
		feedLevel.put(feedId, currentLvl);	
		}
		else{
			System.err.println("No Inventory Entry for Current FeedId: "+feedId);
		}
	}
	protected void updateFeed(int animalId, int feedId, int feedQuantity,
			String feedDate) {
		if(validate(feedId,feedQuantity)){
			animalSet.get(animalId).feedAnimal(feedId,feedQuantity,feedDate);
			feedLevel.put(feedId, feedLevel.get(feedId)-feedQuantity);
		}
	}
	private boolean validate(int feedId, int feedQuantity) {
		if(feedLevel.containsKey(feedId)){
			if(feedLevel.get(feedId)>=feedQuantity)
				return true;
			else{
				System.err.println("Insuffitient Feed Quantity for feed "+feedId);
				return false;
			}
		}
		return false;
	}
	protected void getAnimalFeedFreq(int animalId) {
		if(animalSet.containsKey(animalId)){
			System.out.println("Feed Quantity for Zoo: "+zooId+" ; Animal: "+animalId+" is "+animalSet.get(animalId).getAvgQuantity()+" per Day");
		}
		else{
			System.err.println("Invalid Animal Id "+animalId);
		}
	}
	protected double getAnimalFeedQuantity_species(int animalId) {
		if(animalSet.containsKey(animalId)){
			return animalSet.get(animalId).getAvgQuantity();
		}
		else{
			return 0;
		}
	}
	protected double getAnimalFeedFreq_species(int animalId) {
		if(animalSet.containsKey(animalId)){
			return animalSet.get(animalId).getAvgFrequency();
		}
		else{
			return 0;
		}
	}
	protected void getFeedFreq() {
		for(Map.Entry<Integer, Animal> entry:animalSet.entrySet()){
			getAnimalFeedFreq(entry.getValue().getAnimalId());
		}
	}
	protected void getWastagePrint() {
		System.out.println("The wastage at Zoo: "+zooId+" is "+wastage);
	}
	protected void getSpeciesFeed(int speciesId) {
		if(speciesList.containsKey(speciesId)){
			HashSet<Animal> speciesAnimalSet = speciesList.get(speciesId);
			double freq = 0.0;
			for(Animal animal:speciesAnimalSet){
				freq += animal.getAvgFrequency();
			}
			freq = freq/speciesAnimalSet.size();
			System.out.println("Average Feed Frequency for Species: "+speciesId+" in Zoo: "+zooId+" is "+freq);
		}
		else{
			System.err.println("No species "+speciesId+ " found in Zoo "+zooId);
		}
	}
	public void getSpeciesFeed() {
		for(Map.Entry<Integer, HashSet<Animal>> entry: speciesList.entrySet()){
			getSpeciesFeed(entry.getKey());
		}
	}
	protected void addAnimal(int animalId, int speciesId) {
		if(animalSet.containsKey(animalId)){
			System.err.println("Animal Already in System");
		}
		else{
			Animal animal = new Animal(animalId, speciesId);
			animalSet.put(animalId, animal);
			if(speciesList.containsKey(speciesId)){
				speciesList.get(speciesId).add(animal);
			}
			else{
				speciesList.put(speciesId, new HashSet<Animal>());
				speciesList.get(speciesId).add(animal);
			}
		}
	}
	protected void reportFeed() {
		for(Map.Entry<Integer, Integer> entry: feedLevel.entrySet()){
			System.out.println("Zoo "+zooId+" FeedId "+entry.getKey()+" Quantity "+entry.getValue());
		}
	}
	
}