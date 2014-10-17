import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;


public class data {
	//the number of sell and buy are random number, other numbers are defined below
	public final static int LENGTH = 5;
	public final static int numofpla = 5;
	public final static int PRICElen = 4;
	public final static int ITEM = 100;
	public final static int BUYER = 100;
	public final static int SELLER = 100;
	public final static int manf = 100;
	public final static int Tran =1000;
	public static String[] platform = new String[numofpla];// we have 10 platforms 

	
	//generate a random string with LENGTH defined above
	public data(){
		platform = genKeyS(numofpla);
	}
	public static String genString(int length){
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
		for (int i = 0; i < length; i++){
			int num = random.nextInt(str.length());
			buf.append(str.charAt(num));
		}
		return buf.toString();
	}
	
	//generate a random date
	public static String genDate(){
		long rangebegin = Date.valueOf("1930-01-01").getTime();
		long rangeend = Date.valueOf("2014-12-31").getTime();
		long diff = rangeend - rangebegin + 1;
		Date rand = new Date(rangebegin + (long)(Math.random() * diff));
		return rand.toString();
	}
	
	
	//generate a random int with LENGTH defined above
	public static int genInt(int length){
		Random rn = new Random();
		int rd= rn.nextInt((int)Math.pow(10, length)-(int)Math.pow(10, length-1)) + (int)Math.pow(10, length-1);
		return rd;
	}
	
	//generate key with one attribute for tables
	public static String[] genKeyS(int num){
		HashSet<String> hs = new HashSet<String>();
		
		while(hs.size()< num){
			hs.add(genString(LENGTH));
		}
		
		Iterator it = hs.iterator();
		String[] key = new String[num];
		for(int i = 0; i < num; i++){
			key[i] = (String) it.next();
		}
		
		return key;
	}
	
	
	public static String[] genplat(){
		return platform;
	}
	
	//shuffle array
	public static void ShuffleArray(int[] array)
	{
	    int index;
	    Random random = new Random();
	    for (int i = array.length - 1; i > 0; i--)
	    {
	        index = random.nextInt(i + 1);
	        if (index != i)
	        {
	            array[index] ^= array[i];
	            array[i] ^= array[index];
	            array[index] ^= array[i];
	        }
	    }
	}
	
	//for simplicity we assume each platform has same number of item
	public static String[][] genKeyIS(int num){
		HashMap<String, HashSet<Integer>> hs = new HashMap<String, HashSet<Integer>>();
		for (int i = 0; i < numofpla; i++){
			HashSet<Integer> numofitems= new HashSet<Integer>();
			while(numofitems.size() < (num/numofpla)){
				numofitems.add(genInt(LENGTH ));
			}
			hs.put(platform[i], numofitems);
		}
		//done value creation
		
	String[][] key = new String[num][2];
	int count = 0;

	   for(int i = 0; i < numofpla; i++){
		   Integer[] tempo =  hs.get(platform[i]).toArray(new Integer[num/numofpla]);
			for(int j = 0; j < (num/numofpla); j++){
				key[count][0]= tempo[j].toString();
				key[count][1]= platform[i];
				count ++;
			}
		
		}
	

	   return key;
	
	}
	/*
	//generate key with two attributes(int, String) for tables
	public static String[][] genKeyIS(int num){
		HashMap<Integer, String> hs = new HashMap<Integer, String>();
		ArrayList<Integer> key1 = new ArrayList<Integer>();
		while(hs.size()< num){
			int in = genInt(LENGTH);
			if(!hs.containsKey(in)){
				hs.put(in, genString(LENGTH));
				key1.add(in);
			}

		}
		
		String[][] key = new String[num][2];
		Iterator it = key1.iterator();
		for(int i = 0; i<num; i++){
			Integer tempo = (Integer) it.next();
			key[i][0]= tempo.toString();
			key[i][1] = (String) hs.get(tempo);
		}
		
		return key;
	}
	
*/
	

	public static void genTxt() throws IOException{
	     PrintWriter TXTitem = new PrintWriter("item.txt", "UTF-8");
	     PrintWriter TXTbuyer = new PrintWriter("buyer.txt", "UTF-8");
	     PrintWriter TXTseller = new PrintWriter("seller.txt", "UTF-8");
	     PrintWriter TXTtran = new PrintWriter("Transaction.txt", "UTF-8");
	     PrintWriter TXTmanf = new PrintWriter("Manufacturer.txt", "UTF-8");
	     PrintWriter TXTsell = new PrintWriter("sell.txt", "UTF-8");
	     PrintWriter TXTinterest = new PrintWriter("interest.txt", "UTF-8");
	     
	     String[][] itemKey = genKeyIS(ITEM);
	     String[][] buyerKey = genKeyIS(BUYER);
	     String[][] sellerKey = genKeyIS(SELLER);
	     String[][] TranKey = genKeyIS(Tran);
	     String[] manfKey = genKeyS(manf);
	     

	 	 HashMap<String, Integer> hsSeller = new HashMap<String, Integer>();
		 HashMap<String, Integer> hsBuyer = new HashMap<String, Integer>();
	     for (int i =0; i < manf; i++){
	    	 TXTmanf.println(manfKey[i] + "\t" + genString(LENGTH) + "\t" + genDate() + "\t" + genInt(3)+"-"+genInt(3)+"-"+genInt(4));
	     }
	     
	     for(int i = 0; i < ITEM; i++){
	    	 TXTitem.println(itemKey[i][0] + "\t" + itemKey[i][1] +"\t" + manfKey[new Random().nextInt(manf)] + "\t"
	    	 		+ genString(LENGTH) + "\t" + genString(LENGTH) + "\t" + genInt(PRICElen) + "\t" +genString(LENGTH) + "\t"
	    	 		+ genString(LENGTH) + "\t"+genString(LENGTH));
	     }
	     
	     //generate transaction. ( for each platform, generate a random buyID, a random SellerID, random a ItemID) 
	     for(int i = 0; i < Tran; i++){
	    	 String pl =TranKey[i][1];
	    	 int itemINDEX = new Random().nextInt(ITEM);
	    	 while(!itemKey[itemINDEX][1].equals(pl)){
	    		 itemINDEX = new Random().nextInt(ITEM);
	    	 }
	    
	    	 Integer sellerINDEX = new Random().nextInt(SELLER);
	    	 while(!sellerKey[sellerINDEX][1].equals(pl)){
	    		 sellerINDEX  = new Random().nextInt(SELLER);
	    	 }
	    	 if(!hsSeller.containsKey(sellerKey[sellerINDEX][0])){
	    		 hsSeller.put(sellerKey[sellerINDEX][0], 1);
	    	 }else{
	    		 hsSeller.put(sellerKey[sellerINDEX][0], hsSeller.get(sellerKey[sellerINDEX][0])+1);
	    	
	    	 }

	    	 Integer buyerINDEX = new Random().nextInt(BUYER);
	    	 while(!buyerKey[buyerINDEX][1].equals(pl)){
	    		 buyerINDEX  = new Random().nextInt(BUYER);
	    	 }
	    	 if(!hsBuyer.containsKey(buyerKey[buyerINDEX][0])){
	    		 hsBuyer.put(buyerKey[buyerINDEX][0], 1);
	    	 }else{
	    		 hsBuyer.put(buyerKey[buyerINDEX][0], hsBuyer.get(buyerKey[buyerINDEX][0])+1);
	    	 }
	    	 
	    
	    	 
	    	 TXTtran.println(TranKey[i][0] + "\t" + TranKey[i][1] + "\t" + genDate() + "\t" + new Random().nextInt(10000)+"\t" + 
	    	  genString(LENGTH)+ '\t' + sellerKey[sellerINDEX][0] + "\t" + buyerKey[buyerINDEX][0] + "\t" + itemKey[itemINDEX][0]);
	     
	     }
	     
	   //  System.out.println(hsBuyer.entrySet().toString()); 
	   //  System.out.println(hsSeller.entrySet().toString()); 
	     
	     for(int i = 0; i < BUYER; i++){
	    	 TXTbuyer.println(buyerKey[i][0] + "\t" + buyerKey[i][1] +"\t" + genString(LENGTH) + "\t"
	    	 		+ genDate() + "\t"  + hsBuyer.get(buyerKey[i][0]));
	     }
	     
	     for(int i = 0; i < SELLER; i++){
	    	 TXTseller.println(sellerKey[i][0] + "\t" + sellerKey[i][1] +"\t" + genString(LENGTH) + "\t"
	    	 		+ new Random().nextInt(10) +"\t" + genDate() + "\t"  +hsSeller.get(sellerKey[i][0]));
	     }
	     
	    
	     
	     //generate sell from sellerkey and itemkey. so they can be referenced
	     for(int i = 0; i < SELLER; i++){
	    	 String plat = sellerKey[i][1];
	    	 String seller = sellerKey[i][0];
	    	 int numofsell = new Random().nextInt(ITEM/numofpla); 
	    	 HashSet hs = new HashSet();
	    	 while(hs.size()<numofsell){
	    		 int itemID = new Random().nextInt(ITEM);
	    		 if(itemKey[itemID][1].equals(plat)){
	    			 hs.add(itemKey[itemID][0]);
	    		 }
	    	 }
	    	 
	    	Iterator it = hs.iterator();
	 		for(int j = 0; j < numofsell; j++){
	 			 TXTsell.println(seller + "\t" + plat + "\t" + it.next());
	 		}
	    	
	     }
	     
	   //generate interest from buyerkey and itemkey. so they can be referenced 
	     for(int i = 0; i < BUYER; i++){
	    	 String plat = buyerKey[i][1];
	    	 String buyer = buyerKey[i][0];
	    	 int numofinterests = new Random().nextInt(ITEM/numofpla); 
	    	 HashSet hs = new HashSet();
	    	 while(hs.size()<numofinterests){
	    		 int itemID = new Random().nextInt(ITEM);
	    		 if(itemKey[itemID][1].equals(plat)){
	    			 hs.add(itemKey[itemID][0]);
	    		 }
	    	 }
	    	 
	    	Iterator it = hs.iterator();
	 		for(int j = 0; j < numofinterests; j++){
	 			TXTinterest.println(buyer + "\t" + plat + "\t" + it.next());
	 		}
	    	
	     }
	  
	     
	     TXTinterest.close();
	     TXTsell.close();
	     TXTbuyer.close();
	     TXTitem.close();
	     TXTseller.close();
	     TXTtran.close();
	     TXTmanf.close();
	}
	

	public static void main(String[] args) throws IOException{
		data x= new data();
		x.genTxt();
	}
	
	
}
