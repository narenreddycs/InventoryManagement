import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

class InventoryManagement{


	static HashMap<Integer, Zoo> zooList = new HashMap<Integer, Zoo>();
	static Set<Integer> feedList = new HashSet<Integer>();
	static HashMap<Integer, HashMap<Integer, HashSet<Integer>>> speciesList = new HashMap<Integer, HashMap<Integer, HashSet<Integer>>>();

	public static void main(String[] args){

		String inputString="";
		do{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try {
				inputString = br.readLine();
				processInput(inputString);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}while(!inputString.equals("exit"));
	}

	private static void processInput(String inputString) {
		String[] tokens = inputString.split(" ");
		if(tokens.length>0){
			switch(tokens[0]){
			case "inv_ins": inventoryInsert(tokens);
			break;

			case "inv_update": inventoryUpdate(tokens);
			break;

			case "feed_update":feedInsert(tokens);
			break;

			case "report_animalFeed": reportAnimalFeed(tokens);
			break;

			case "report_speciesFeed": reportSpeciesFees(tokens);
			break;

			case "report_wastage": getWastage(tokens);
			break;

			case "report_speciesStats": generateVal(tokens);
			break;

			case "input_animal":inputAnimal(tokens);
			break;

			case "report_feed": reportFeed(tokens);
								System.out.println(speciesList);
			break;

			case"exit": System.out.println("Exiting the Inventory Management System");
			break;

			default: System.err.println("Invalid Input"); 

			}
		}
		else{
			System.err.println("No Input Found");
		}
	}

	private static void reportFeed(String[] tokens) {
		if(tokens.length==2){
			if(validate("Zoo",tokens[1])){
				int zooId = Integer.parseInt(tokens[1]);
				if(zooList.containsKey(zooId)){
					zooList.get(zooId).reportFeed();
				}
				else{
					System.err.println("ZooId not found "+zooId);
				}
			}
			else if(tokens.length==1){
				for(Map.Entry<Integer, Zoo> entry: zooList.entrySet()){
					entry.getValue().reportFeed();
				}
			}
			else{
				System.err.println("Invalid number of Arguments for report_feed");
			}
		}
	}

	private static void inputAnimal(String[] tokens) {
		if(tokens.length==4){
			int zooId = Integer.parseInt(tokens[1]);
			int animalId = Integer.parseInt(tokens[2]);
			int speciesId = Integer.parseInt(tokens[3]);
			putAnimal(zooId, speciesId, animalId);
			putAnimalObject(zooId, animalId, speciesId);
			System.out.println("Animal "+animalId+" entered into the Zoo "+zooId+" system");
		}
		else{
			System.err.println("Insuffitient Number of Parameters");
		}
	}

	private static void putAnimalObject(int zooId, int animalId, int speciesId) {
		if(zooList.containsKey(zooId)){
			zooList.get(zooId).addAnimal(animalId,speciesId);
		}
		else{
			Zoo zoo = new Zoo(zooId);
			zooList.put(zooId, zoo);
			zoo.addAnimal(animalId, speciesId);
		}
	}

	private static void putAnimal(int zooId, int speciesId, int animalId) {
		if(speciesList.containsKey(speciesId)){
			HashMap<Integer, HashSet<Integer>> zooTemp = speciesList.get(speciesId);
			if(zooTemp.containsKey(zooId)){
				if(zooTemp.get(zooId).contains(animalId)){
					System.err.println("Animal Already In System");
				}
				else{
					zooTemp.get(zooId).add(animalId);
				}
			}
			else{
				HashSet<Integer> animalTemp = new HashSet<Integer>();
				animalTemp.add(animalId);
				speciesList.get(speciesId).put(zooId, animalTemp);
			}
		}
		else{
			HashMap<Integer, HashSet<Integer>> zooTemp = new HashMap<Integer, HashSet<Integer>>();
			HashSet<Integer> animalTemp = new HashSet<Integer>();
			animalTemp.add(animalId);
			zooTemp.put(zooId, animalTemp);
			speciesList.put(speciesId, zooTemp);
		}
	}

	private static void generateVal(String[] tokens) {
		if(tokens.length<=3){
			HashMap<Integer, Double> feedFreq = new HashMap<Integer, Double>();
			HashMap<Integer, Double> feedQuantity = new HashMap<Integer, Double>();
			generatestats(feedFreq, feedQuantity);
			try{
				double value = Double.parseDouble(tokens[2]);
				if(tokens[1].equals("above")){
					printStats(value,1, feedFreq, feedQuantity);
				}
				else if(tokens[1].equals("below")){
					printStats(value,0, feedFreq, feedQuantity);
				}
				else{
					System.err.println("The only acceptable values are \"above\" and \"below\"");
				}
			}
			catch(NumberFormatException E){
				System.err.println("Invalid Percentage Value");
			}
		}
		else{
			System.err.println("Insuffitient number of parameters");
		}
	}

	private static void printStats(
			double value,
			int i,
			HashMap<Integer, Double> feedFreq,
			HashMap<Integer, Double> feedQuantity) {

		for(Map.Entry<Integer, HashMap<Integer, HashSet<Integer>>> entry: speciesList.entrySet()){
			int speciesId = entry.getKey();
			HashMap<Integer, HashSet<Integer>> speciesZooList = entry.getValue();
			for(Map.Entry<Integer, HashSet<Integer>> entry2: speciesZooList.entrySet()){
				int zooId = entry2.getKey();
				HashSet<Integer> animalIdList = entry2.getValue();
				for(int temp:animalIdList){
					double frequency=zooList.get(zooId).getAnimalFeedFreq_species(temp);
					double quantity=zooList.get(zooId).getAnimalFeedQuantity_species(temp);
					double freqPercentage =getpercentage(frequency,feedFreq.get(speciesId));
					double quanPercentage =getpercentage(quantity, feedQuantity.get(speciesId));
					if(i==1){
						if(freqPercentage>value||quanPercentage>value){
							System.out.println("Animal "+temp+" at Zoo "+zooId+" has a feeding frequency of "+freqPercentage+" daily compared to average and is fed "+quanPercentage+" times on average qunatity consumed by the same species");
						}
					}
					else{
						if(freqPercentage<value||quanPercentage<value){
							System.out.println("Animal "+temp+" at Zoo "+zooId+" has a feeding frequency of "+freqPercentage+" daily compared to average and is fed "+quanPercentage+" times on average qunatity consumed by the same species");
						}
					}
				}
			}
		}
	}

	private static void generatestats(
			HashMap<Integer, Double> feedFreq,
			HashMap<Integer, Double> feedQuantity) {
		for(Map.Entry<Integer, HashMap<Integer, HashSet<Integer>>> entry: speciesList.entrySet()){
			int speciesId = entry.getKey();
			double frequency = 0.0;
			double quantity = 0.0;
			int count=0;
			HashMap<Integer, HashSet<Integer>> speciesZooList = entry.getValue();
			for(Map.Entry<Integer, HashSet<Integer>> entry2: speciesZooList.entrySet()){
				int zooId = entry2.getKey();
				HashSet<Integer> animalIdList = entry2.getValue();
				for(int temp:animalIdList){
					frequency+=zooList.get(zooId).getAnimalFeedFreq_species(temp);
					quantity+=zooList.get(zooId).getAnimalFeedQuantity_species(temp);
					count++;
				}
			}
			frequency = frequency/count;
			quantity = quantity/count;
			feedFreq.put(speciesId, frequency);
			feedQuantity.put(speciesId, quantity);
		}
	}


	private static Double getpercentage(double val1, double val_baseline) {
		double temp = (val1/val_baseline);
		return temp;
	}

	private static void reportSpeciesFees(String[] tokens) {
		if(tokens.length==3){
			if(tokens[1].equals("z")||tokens[1].equals("Z")){
				if(validate("Zoo", tokens[2])){
					int zooId = Integer.parseInt(tokens[2]);
					zooList.get(zooId).getSpeciesFeed();
				}
			}
			else if(tokens[1].equals("s")||tokens[1].equals("S")){
				if(validate("Species",tokens[2])){
					int speciesId = Integer.parseInt(tokens[2]);
					for(Map.Entry<Integer, Zoo> entry:zooList.entrySet()){
						if(entry.getValue().getSpeciesList().containsKey(speciesId)){
							entry.getValue().getSpeciesFeed(speciesId);
						}
					}
				}
			}
			else{
				if(validate("Zoo",tokens[1])&&validate("Species",tokens[2])){
					int zooId = Integer.parseInt(tokens[1]);
					int speciesId = Integer.parseInt(tokens[2]);
					zooList.get(zooId).getSpeciesFeed(speciesId);
				}
			}

		}
		else{
			System.err.println("Invalid Number of Parameters");
		}
	}

	private static void getWastage(String[] tokens) {
		if(tokens.length==1){
			gettotalWastage();
		}
		else{
			getottalWastage(tokens[1]);
		}
	}

	private static void gettotalWastage() {
		for(Map.Entry<Integer, Zoo> entry:zooList.entrySet()){
			entry.getValue().getWastagePrint();
		}
	}

	private static void getottalWastage(String string) {
		if(validate("Zoo",string)){
			int zooId = Integer.parseInt(string);
			zooList.get(zooId).getWastagePrint();
		}
	}

	private static void reportAnimalFeed(String[] tokens) {

		if(tokens.length==1){
			getAnimalFeedAgg();
		}
		else if(tokens.length==2){
			try{
				int zooId = Integer.parseInt(tokens[1]);
				getAnimalFeedZoo(zooId);
			}
			catch(NumberFormatException E){
				System.err.println("Invalid Number");
			}
		}
		else if(tokens.length==3){
			try{
				int zooId = Integer.parseInt(tokens[1]);
				int AnimalId = Integer.parseInt(tokens[2]);
				getAnimalFeedAnimal(zooId, AnimalId);
			}
			catch(Exception E){
				System.err.println("Invalid Number");
			}
		}
	}

	private static void getAnimalFeedAgg() {
		for(Map.Entry<Integer, Zoo> entry:zooList.entrySet()){
			entry.getValue().getFeedFreq();
		}
	}

	private static void getAnimalFeedZoo(int zooId) {
		if(validate("Zoo",zooId+"")){
			zooList.get(zooId).getFeedFreq();
		}
	}

	private static void getAnimalFeedAnimal(int zooId, int animalId) {
		if(validate("Zoo", zooId+"")){
			zooList.get(zooId).getAnimalFeedFreq(animalId);
		}
	}

	private static void feedInsert(String[] tokens) {
		if(tokens.length==6){
			if(validate("Zoo",tokens[1])&&validate("Feed",tokens[3])){
				try{
					int zooId = Integer.parseInt(tokens[1]);
					int animalId = Integer.parseInt(tokens[2]);
					int feedId = Integer.parseInt(tokens[3]);
					int feedQuantity = Integer.parseInt(tokens[4]);
					DateTime date = new DateTime(tokens[5]);
					DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/YYYY");
					String feedDate = date.toString(dtf);
					if(validateAnimal(animalId, zooList.get(zooId).getAnimalSet())){
						zooList.get(zooId).updateFeed(animalId, feedId, feedQuantity, feedDate);
					}

				}
				catch(NumberFormatException E){
					System.err.println("Invalid Number");
				}
				catch(IllegalArgumentException E){
					System.err.println("Wrong Date Format");
				}
			}
		}
		else{
			System.err.println("Invalid Format ");
		}
	}

	private static boolean validateAnimal(int animalId,
			HashMap<Integer, Animal> animalSet) {
		if(animalSet.containsKey(animalId))
			return true;
		else{
			System.err.println("No Animal found in current Zoo with id "+animalId);
			return false;
		}
	}

	private static void inventoryUpdate(String[] tokens) {
		if(tokens.length==4){
			if(validate("Zoo",tokens[1])&&validate("Feed",tokens[2])){
				int zooId = Integer.parseInt(tokens[1]);
				int feedId = Integer.parseInt(tokens[2]);
				try{
					int feedlvl = Integer.parseInt(tokens[3]);
					zooList.get(zooId).inventeryValidate(feedId, feedlvl);
				}
				catch(NumberFormatException E){
					System.err.println("Invalid FeedLvl Number "+ tokens[3]);
				}
			}
		}
		else{
			System.err.println("Invalid Format ");
		}

	}

	private static void inventoryInsert(String[] tokens) {
		if(tokens.length==4){
			if(validate("Zoo",tokens[1])){
				int zooId = Integer.parseInt(tokens[1]);
				try{
					int feedId = Integer.parseInt(tokens[2]);
					if(!feedList.contains(feedId))
						feedList.add(feedId);
					int feedlvl = Integer.parseInt(tokens[3]);
					zooList.get(zooId).inventeryUpdate(feedId, feedlvl);
				}
				catch(NumberFormatException E){
					System.err.println("Invalid FeedLvl Number "+ tokens[3]);
				}
			}
		}
		else{
			System.err.println("Invalid Format ");
		}
	}

	private static boolean validate(String string, String string2) {
		try{
			int count = Integer.parseInt(string2);
			switch(string){
			case "Zoo": if(zooList.containsKey(count))
				return true;
			else{
				System.err.println("Invalid ZooId "+count);
				return false;
			}

			case "Feed": if(feedList.contains(count))
				return true;
			else{
				System.err.println("Invalid FeedId "+count);
				return false;

			}
			case "Species":if(speciesList.containsKey(count))
				return true;
			else{
				System.err.println("Invalid speciesId "+count);
				return false;

			}

			}
		}
		catch(NumberFormatException E){
			System.err.println("Invalid Number "+string2 );
			return false;
		}
		return false;
	}
}