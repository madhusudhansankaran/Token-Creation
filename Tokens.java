import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class Tokens 
{
	private static BufferedReader br;
	
	private static Hashtable<String, Integer> listOfWords = new Hashtable<String, Integer>();
	private static Set<String> tag_names = new HashSet<String>();
	private static ArrayList<Map.Entry<String, Integer>> list;
	
	private static int no_of_docs = 0;
	private static int add_value = 0;
	
	/*
	 * This main method takes in the location of the directory from the user and executes the program.
	 */
	public static void main(String args[]) throws IOException
	{
		float start_time = System.nanoTime(); // The starting time of the program.
		
		//Takes the folder location as an argument from the user.
		final File folder = new File(args[0]);
		
		listFilesForFolder(folder);
		
		list = Sort();
		//printListOfWords(list);
		
		int temp = getNumberOfTokens(listOfWords);
		int tokens = temp;
		System.out.println("1. Number of tokens in the text collection: " + temp);
		
		temp = getUniqueWordCount(listOfWords);
		System.out.println("2. Number of unique words in the text collection: " + temp);
		
		temp = getOnlyOnce(listOfWords);
		System.out.println("3. Number of words that occur only once in the text collection: " + temp);
		
		ThirtyMostFrequent(list);
		temp = AvgTokensPerDoc(tokens);
		
		System.out.println("5. The average number of word tokens per document: " + temp);
		
		float end_time = System.nanoTime(); // The ending time of the program.
		System.out.println("\nIt took the program "+ (end_time - start_time)/1000000000  + " s to run."); 
	}
	
	/*
	 * This method lists all the files in the particular folder. It was a preliminary test to check whether all the files in the Cranfield folder were listed.
	 */
	public static void listFilesForFolder(final File folder) throws IOException 
	{
		int count = 0;
    	
		//For every file in the particular folder.
    	for (final File file : folder.listFiles()) 
	    {
    		//If the particular file in the folder is a folder in itself.
	        if (file.isDirectory()) 
	        {
	            listFilesForFolder(file); //Recursive function which finds the list of all the files in the directory.
	        } 
	        else 
	        {
	        	no_of_docs++; //Gets the number of documents in the folder.
	        	br = new BufferedReader(new FileReader(file)); //Initialize BufferedReader for that file.
	        }
	        count = tagOperations(file, tag_names);
	        WordList(file, tag_names);
	    }
	    //System.out.println("Number of tags found in the file: " + count); //Prints the number of tags in the file.
	    //printTagNames(tag_names); // Prints the tag names found in the file.
	}	
	
	/*
	 * This method takes the file as the argument and returns the number of tags in the particular file.
	 */
	public static int tagOperations(File file, Set<String> tag_names) throws IOException
	{
		String line;
		int tag_count = 0;
		
		br = new BufferedReader(new FileReader(file));
		
		while((line = br.readLine()) != null)
        {
			/*
			 * If the line contains a '<', it is considered a tag and tag_count is incremented.
			 */
        	if(line.contains("<"))
        	{
        		tag_count++;
        		
        		String b = line.replaceAll("[<>/]", "");
        		tag_names.add(b);
        	}
        	
        }
        tag_count/=2; //Since each tag represent the beginning and the end, we divide it by two to get the actual count.
        return tag_count;
	}
	
	/*
	 * This method prints the tag names found in the file.
	 */
	public static void printTagNames(Set<String>tag_names)
	{
		System.out.println("All the tags found in the file: " + tag_names);
	}
	
	/*
	 * Creates a hashtable with the tokens and their frequencies.
	 */
	public static void WordList(File file, Set<String> tag_names) throws IOException
	{
		String line;
		String words[];
		
		br = new BufferedReader(new FileReader(file));
        
        while((line = br.readLine()) != null)
        {
        	String alphaOnly = line.replaceAll("[^a-zA-Z]+"," "); //Replace everything that is not an alphabet with a blank space.
      
        	words = alphaOnly.split(" ");
        	
            int countWord = 0;
            
            for(String word : words)
            {
            	if(!tag_names.contains(word) && !word.equals(""))
            	{
            		word = word.toLowerCase(); // Converts all words to lower case.
            		
            		//add word if it isn't added already
            		if(!listOfWords.containsKey(word))
            		{                             
            			//first occurance of this word
            			listOfWords.put(word, 1); 
            		}
            		else
            		{
            			countWord = listOfWords.get(word) + 1; //Get current count and increment
            			//listOfWords.remove(word); //First remove it (can't have duplicate keys)
            			listOfWords.put(word, countWord); //Now put it back with new value
            		}
            	}
            }
        }
    }

	/*
	 * Returns the number of unique words in all the files.
	 */
	public static int getUniqueWordCount(Hashtable<String, Integer> list)
	{
       return list.size();
	}

	/*
	 * Prints the sorted hashtable with the words and their respective frequencies.
	 */
	public static void printListOfWords(ArrayList<Map.Entry<String, Integer>> l)
	{
		System.out.println("The hashtable created with the <token : frequency> information:");
		System.out.println(l + "\n");
	}
	
	/*
	 * Gets the number of tokens in the files.
	 */
	public static int getNumberOfTokens(Hashtable<String, Integer>listOfWords)
	{
		for(Integer val: listOfWords.values())
		{
			add_value = add_value + val;
		}
		return add_value;
	}
	
	/*
	 * Sorts the Hashmap in descending order and returns it as an ArrayList.
	 */
	public static ArrayList<Map.Entry<String, Integer>> Sort()
	{
		ArrayList<Map.Entry<String, Integer>> l = new ArrayList<Map.Entry<String, Integer>>(listOfWords.entrySet());
		Collections.sort(l, new Comparator<Map.Entry<String, Integer>>(){
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
	            return o2.getValue().compareTo(o1.getValue());
	        }});
		return l;
	}
	
	/*
	 * Returns the tokens that are repeated only once.
	 */
	public static int getOnlyOnce(Hashtable<String, Integer> listOfWords)
	{
		int count = 0;
		for(Integer val: listOfWords.values())
		{
			if(val == 1)
			{
				count ++;
			}
		}
		return count;
	}
	
	/*
	 * Returns the 30 most frequent tokens from the sorted ArrayList.
	 */
	public static void ThirtyMostFrequent(ArrayList<Map.Entry<String, Integer>> l)
	{
		System.out.println("4. The 30 most frequent words in the text collection and their frequencies are: ");
		for(int i=0;i<30;i++)
		{
			System.out.println("\t" + (i+1) + "." + " " + l.get(i));
		}
	}
	
	/*
	 * Returns the average number of words per document.
	 */
	public static int AvgTokensPerDoc(int tokens)
	{
		return tokens/no_of_docs;
	}
}