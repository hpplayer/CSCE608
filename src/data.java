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
	public final static int total_Tran =1000;//total number of number * sell or number * purchase
	public static int Tran; //we assume each platform has same amount of transactions. so tran%numofpla should be 0;
	public static String[] platform = new String[numofpla];// we have 10 platforms 
	public static ArrayList amount;
	
	//generate a random string with LENGTH defined above
	public data(){
		platform = genKeyS(numofpla);
		int sum = 0;
		amount = new ArrayList();
		int num = 10;
		while(true){
			if(total_Tran - sum < 10){		
				amount.add(total_Tran - sum);
				break;
			}	
			int a = new Random().nextInt(num);
			sum += a;
			amount.add(a);
		}
		Tran = amount.size();
		
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
	
	
	//for simplicity we assume each platform has same number of item
	public static String[][] genKeyIS(int num){
		// key in hs is platformID, and values are itemID sold in that platformID
		HashMap<String, HashSet<Integer>> hs = new HashMap<String, HashSet<Integer>>();
		for (int i = 0; i < numofpla; i++){
			HashSet<Integer> hs2= new HashSet<Integer>();
			while(hs2.size() < (num/numofpla)){
				hs2.add(genInt(LENGTH ));
			}
			hs.put(platform[i], hs2);
		}
	
		
	String[][] key = new String[num][2];
	int count = 0;
	   for(int i = 0; i < numofpla; i++){
		   //tempo array store all itemID sold in platform[i]
		   Integer[] tempo =  hs.get(platform[i]).toArray(new Integer[num/numofpla]);
			for(int j = 0; j < (num/numofpla); j++){
				key[count][0]= tempo[j].toString();
				key[count][1]= platform[i];
				count ++;
			}
		
		}
	
	   return key;
	
	}
	
	

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
	   //  String[][] TranKey = genKeyIS(total_Tran);
	     String[][] TranKey = genKeyIS(Tran+(5-Tran%5));
	     String[] manfKey = genKeyS(manf);
	     
		/*use two hashmaps to generate random num of purchases of buyer and num of sells of seller on same platform in each 
		transaction generation. This method guarantees that the total number of purchases equals total number of sells, which also equals total number of transactions. This method also simulate the real situation that some buyer or seller may 
		not has start their purchase or sell yet. So we only have their registered information without any transaction history.
		*/
	 	 HashMap<String, Integer> hsSeller = new HashMap<String, Integer>();
		 HashMap<String, Integer> hsBuyer = new HashMap<String, Integer>();
	     for (int i =0; i < manf; i++){
	    	 TXTmanf.println(manfKey[i] + "\t" + genString(LENGTH) + "\t" + genDate() + "\t" + genInt(3)+"-"+genInt(3)+"-"+genInt(4));
	     }
	     
	     for(int i = 0; i < ITEM; i++){
	    	 TXTitem.println(itemKey[i][0] + "\t" + itemKey[i][1] +"\t" + manfKey[new Random().nextInt(manf)] + "\t"
	    	 		+ genString(LENGTH) + "\t" + genString(LENGTH) + "\t"   +genString(LENGTH) + "\t" + genInt(PRICElen) + "\t"
	    	 		+ genString(LENGTH) + "\t"+genString(LENGTH));
	     }
	     
	     //generate transaction. ( for each platform, generate a random buyID, a random SellerID, random a ItemID) 
	     for(int i = 0; i < Tran; i++){
	    	// System.out.println(Tran);
	    	// System.out.println("Im in cycle " + i);
	    	 String pl =TranKey[i][1];
	    	 int itemINDEX = new Random().nextInt(ITEM);
	    	 while(!itemKey[itemINDEX][1].equals(pl)){
	    		 itemINDEX = new Random().nextInt(ITEM);
	    		// System.out.println(itemKey[itemINDEX][1] + " " + pl);
	    	 }
	    //	 System.out.println("problem  item" + i);
	    	 
	    	 Integer sellerINDEX = new Random().nextInt(SELLER);
	    	 while(!sellerKey[sellerINDEX][1].equals(pl)){
	    		 sellerINDEX  = new Random().nextInt(SELLER);
	    	 }
	    	 if(!hsSeller.containsKey(sellerKey[sellerINDEX][0])){
	    		 hsSeller.put(sellerKey[sellerINDEX][0], (Integer) amount.get(i));
	    	 }else{
	    		 hsSeller.put(sellerKey[sellerINDEX][0], hsSeller.get(sellerKey[sellerINDEX][0])+(Integer) amount.get(i));
	    	
	    	 }
	    	// System.out.println("problem  seller" + i);
	    	 
	    	 Integer buyerINDEX = new Random().nextInt(BUYER);
	    	 while(!buyerKey[buyerINDEX][1].equals(pl)){
	    		 buyerINDEX  = new Random().nextInt(BUYER);
	    	 }
	    	 if(!hsBuyer.containsKey(buyerKey[buyerINDEX][0])){
	    		 hsBuyer.put(buyerKey[buyerINDEX][0], (Integer) amount.get(i));
	    	 }else{
	    		 hsBuyer.put(buyerKey[buyerINDEX][0], hsBuyer.get(buyerKey[buyerINDEX][0])+(Integer) amount.get(i));
	    	 }
	    	// System.out.println("problem  buter" + i);
	    	 
	    	 TXTtran.println(TranKey[i][0] + "\t" + TranKey[i][1] + "\t" + genDate() + "\t" + amount.get(i)+"\t" + 
	    	  genString(LENGTH)+ '\t' + sellerKey[sellerINDEX][0] + "\t" + buyerKey[buyerINDEX][0] + "\t" + itemKey[itemINDEX][0]);
	     }
	     //System.out.println("Im done");
	
	     for(int i = 0; i < BUYER; i++){
	    	 Integer purchase = hsBuyer.get(buyerKey[i][0]);
	    	 if(purchase == null){
	    		 purchase = 0;
	    	 }
	    	 
	    	 TXTbuyer.println(buyerKey[i][0] + "\t" + buyerKey[i][1] +"\t" + genString(LENGTH) + "\t"
	    	 		+ genDate() + "\t"  + purchase);
	     }
	     
	     for(int i = 0; i < SELLER; i++){
	    	 Integer sell = hsSeller.get(sellerKey[i][0]);
	    	 if(sell == null){
	    		 sell = 0;
	    	 }
	    	 TXTseller.println(sellerKey[i][0] + "\t" + sellerKey[i][1] +"\t" + genString(LENGTH) + "\t"
	    	 		+ new Random().nextInt(10) +"\t" + genDate() + "\t"  +sell);
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
