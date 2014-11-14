import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
/**
 * Copyright 2014 Author: hpPlayer
 *
 * All Right Reserved
 *
 * Created on Nov 13, 2014 12:51:22 PM
 */
public class search {
	  public static void main (String[] args)
	    {
	        Connection conn = null;

	        try
	        {
	            //connecting to the mysql server
	            String userName = "root";
	            String password = "123";
	            String url = "jdbc:mysql://localhost:3306/hpplayer-608_proj2";
	   	        Class.forName("com.mysql.jdbc.Driver").newInstance();
	            conn = DriverManager.getConnection (url, userName, password);
	            System.out.println("Database connection established. Welcome Aboard!");    //Display connection established!
	            System.out.println();
	            //MENU: A list of queries for user
	            System.out.println("***********************************************************************************************************");
	            System.out.println("|                                      E-Commerce Management System                                       |");
	            System.out.println("***********************************************************************************************************");
	            System.out.println("|1.Insert a new transaction.                                                                              |");
	            System.out.println("|2.Query the top seller in each platform and update their rating.                                         |");
	            System.out.println("|3.Compare the total sells of all platforms                                                               |");
	            System.out.println("|4.Update any existing item information                                                                   |");
	            System.out.println("|5.Delete fans whose age is larger than a specific number and teamName is equivalent to the user input.   |");
	            System.out.println("|6.Quit                                                                                                   |");
	            System.out.println("***********************************************************************************************************");

	            boolean quit = false;
	            while (true) {
	                System.out.print("Please choose a number to query the database: ");
	                Scanner scan_num = new Scanner(System.in);
	                int num = scan_num.nextInt();

	                //issuing queries
	                search srh = new search();
	                switch (num) {
	                    case 1:
	                        //insert a new transaction
	                        srh.insert_trans(conn);
	                        break;
	                    case 2:
	                        //query the top seller in each platform and decide whether update their rating by 1. 
	                        srh.queryTop(conn);
	                        break;
	                    case 3:
	                        //Compare the total sells of all platforms
	                    	srh.Compare(conn);
	                        break;
	                    case 4:
	                    	updateItem(conn);
	                        break;
	                    case 5:
	                        //Delete fans whose age is larger than user input and teamName is equivalent to the user input
	                      //  nba.delete_teams(conn);
	                        break;
	                    case 6:
	                        //Quit the query
	                        quit = true;
	                        break;
	                    default:
	                       // break;
	                    	System.out.println("Wrong input, try again, choose 1 - 6");
	                }
	                if (quit == true) {
	                    break;
	                }
	                                                                   //Quit the query
	            }

	        } catch (SQLException e) {
	            System.err.println ("Error message: " + e.getMessage ());     //catch error message
	            System.err.println ("Error number: " + e.getErrorCode ());    //catch numeric error code
	            System.err.println("SQLState: " + e.getSQLState());
	        } catch (Exception e) {
	        	  System.err.println ("Oops! we got an error: " + e.getMessage()); 
	        } finally
	        {
	            if (conn != null)
	            {
	                try
	                {
	                    conn.close ();
	                    System.out.println ("Database connection terminated. Good Bye!");       //Display connection terminated!
	                }
	                catch (Exception e) { System.err.println("Database termination error."); }
	            }
	            
	            
	        }
	    }//end main method
	  

	  //insert a new arena record
	    public void insert_trans(Connection conn) throws SQLException {
	    	int maxID = 0, Amount, SellerID, BuyerID, ItemID, count;
	    	String Platform, Time, paymentType;
	        Scanner scan = new Scanner(System.in);
	        //get maxID
	        Statement st = conn.createStatement();
	        String sql = "SELECT MAX(TransactionID) AS max FROM transaction";
	        st.executeQuery(sql);
	        ResultSet rs = st.getResultSet();
	        if(rs.next()){
		        maxID = rs.getInt("max");
	        }
		    st.close();
		    //get current time and date  
		    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    Calendar cal = Calendar.getInstance();
		    Time = dateFormat.format(cal.getTime());
		    //input information
	        System.out.println("Please input necessary information in this transaction:");
	        System.out.print("Platform: ");
	        Platform = scan.next();
	        System.out.print("Amount: ");
	        Amount = scan.nextInt();
	        System.out.print("paymentType: ");
	        paymentType = scan.next();
	        System.out.print("SellerID: ");
	        SellerID = scan.nextInt();
	        System.out.print("BuyerID: ");
	        BuyerID = scan.nextInt();
	        System.out.print("ItemID: ");
	        ItemID = scan.nextInt();

	        Statement st2 = conn.createStatement();
	        count = st2.executeUpdate(
	                "INSERT INTO transaction"
	                + " VALUES"
	                + "('" + (maxID +1) + "','" + Platform + "','" + Time + "','" + Amount + "','" + paymentType + "','" +
	                SellerID + "','" + BuyerID + "','" + ItemID + "')");
	        st2.close();
	        System.out.println(count + " row(s) was/were inserted");

	    }
	    
	    public void queryTop(Connection conn) throws SQLException {
	    	ArrayList<String> platform = new ArrayList<String>();
	    	ArrayList<Integer> Seller = new ArrayList<Integer>(); 
	    	//Query top seller first
	    	 Statement st = conn.createStatement();
		     String sql = "select SellerID, Platform, Rating from seller group by Platform having max(NumOfSells)";
		     st.executeQuery(sql);
		     ResultSet rs = st.getResultSet();
		     while(rs.next()){
		    	 platform.add(rs.getString("Platform"));
		    	 Seller.add(rs.getInt("SellerID"));
			     System.out.println("Platform: " + rs.getString("Platform") + " Top SellerID: " + rs.getInt("SellerID") + " current Rating: " +
		    	 rs.getInt("Rating"));
		     }
		     while(true){
		     System.out.println("Upgrade Top Sellers' Rating by 1 ? (y/n)");
	    	 Scanner scan_num = new Scanner(System.in);
             String boo = scan_num.next();
             if(boo.equals("y")){
            	 int count = 0;
            	 while(!Seller.isEmpty()){
            		 Statement s = conn.createStatement();
            		 s.executeUpdate("update seller set rating = rating +1 where sellerID = " + Seller.remove(0));
            		 count++;
            		 s.close();
            	 }
            	 System.out.println(count + " row(s) was/were successfully updated");
            	 break;
             }
             else if (boo.equals("n")){
            	 System.out.println("You choose \"n\" and no action is required at this time");
            	 break;
             }
             else{
            	 System.out.println("invalid input, try again");
             }
		     }
		     st.close();
	    }
	    
	    public void Compare(Connection conn) throws SQLException {
		     ArrayList<platform> pl = new ArrayList<platform>();
		     
	    	 Statement st = conn.createStatement();
		     String sql = "select platform, sum(Amount) as sells from transaction group by Platform";
		     st.executeQuery(sql);
		     ResultSet rs = st.getResultSet();
		     while(rs.next()){
		    	 pl.add(new platform(rs.getString("Platform"), rs.getInt("sells")));
			     System.out.println("Platform: " + rs.getString("Platform") + " Total Sells: " + rs.getInt("sells"));
		     }
		     new chart(pl).print();
		     st.close();
	    }
	    
	    public static void updateItem(Connection conn) throws SQLException{
	    	 System.out.println("please input the itemID");
	     	 Scanner scan_num = new Scanner(System.in);
             int itemID = scan_num.nextInt();
             System.out.println("please input the platform");
         	 Scanner scan_num3 = new Scanner(System.in);
             String plat = scan_num3.next();
             Statement st = conn.createStatement();
		     String sql = "select * from item where Itemid = " + itemID + " AND Platform = '" + plat + "';";
		     st.executeQuery(sql);
		     ResultSet rs = st.getResultSet();
		     System.out.println();
		     System.out.println("The current tuple:");
		     while(rs.next()){
			     System.out.println("ItemID: " + rs.getInt("ItemId") + ", Platform: " + rs.getString("Platform")
			    		 + ", ManfName: "+rs.getString("ManfName") + ", Category: " + rs.getString("Category")
			    		 + ", Model: " + rs.getString("Model") + ", Title: " + rs.getString("Title") + ", Price: "+
			    		 rs.getInt("Price") + ", ImageURL: " + rs.getString("ImageUrl") + ", Description: " + rs.getString("Description"));
		     }
		     rs.close();
		    System.out.println("************************************  Result successfully fetched!   ********************************************");
            System.out.println("|1.ManfName                                                                                                      |");
            System.out.println("|2.Category                                                                                                      |");
            System.out.println("|3.Model                                                                                                         |");
            System.out.println("|4.Title                                                                                                         |");
            System.out.println("|5.Price                                                                                                         |");
            System.out.println("|6.ImageURL                                                                                                      |");
            System.out.println("|7.Description                                                                                                   |");
            System.out.println("|8.exit                                                                                                          |");
            System.out.println("******************************************************************************************************************");
            outerloop:	
            while(true){
            	System.out.println("please choose a number to update attribute");
            	Scanner scan_num2 = new Scanner(System.in);
                int num2 = scan_num.nextInt();
                String sql2, select;
                ResultSet rs2;
                int count;
                Scanner temp;
                Statement stm;
                boolean quit = false;
                switch (num2) {
                    case 1:
                    		stm = conn.createStatement();
                    		select = "select * from item where Itemid = " + itemID + " AND Platform = '" + plat + "';";
                    		stm.executeQuery(select);
                    		rs2 = stm.getResultSet();
                    		while(rs2.next()){
                    		System.out.println("Current ManfName: " + rs2.getString("ManfName"));
                    		}
                    		System.out.println("Input new ManfName(must be an existing ManfName in DB): ");
                    		temp = new Scanner(System.in);
                            String ManfName = temp.next();
                            sql2 = "update item set ManfName = '" + ManfName +"' where ItemId = " +
                            itemID + " AND Platform = '" + plat +"'";
                            count = stm.executeUpdate( sql2);
                            stm.close();
                            System.out.println(count + " row(s) was/were updated");
                            break; 
                    case 2:
                    	stm = conn.createStatement();
                   		select = "select * from item where Itemid = " + itemID + " AND Platform = '" + plat + "';";
                		stm.executeQuery(select);
                		rs2 = stm.getResultSet();
                		while(rs2.next()){
                			System.out.println("Current Categoty: " + rs2.getString("Category"));
                		}	
                		System.out.println("Input new Categoty: ");
                		temp = new Scanner(System.in);
                        String Categoty = temp.next();
                        sql2 = "update item set Category = '" + Categoty +"' where ItemId = " +
                        itemID + " AND Platform = '" + plat +"'";
                        count = stm.executeUpdate( sql2);
                        stm.close();
                        System.out.println(count + " row(s) was/were updated");
                        break;
                    case 3:
                    	stm = conn.createStatement();
                 		select = "select * from item where Itemid = " + itemID + " AND Platform = '" + plat + "';";
                		stm.executeQuery(select);
                		rs2 = stm.getResultSet();
                		while(rs2.next()){
                		 	System.out.println("Current Model: " + rs2.getString("Model"));
                		}
                		System.out.println("Input new Model: ");
                		temp = new Scanner(System.in);
                        String Model = temp.next();
                        sql2 = "update item set Model = '" + Model +"' where ItemId = " +
                        itemID + " AND Platform = '" + plat +"'";
                        count = stm.executeUpdate(sql2);
                        stm.close();
                        System.out.println(count + " row(s) was/were updated");
                        break;
                    case 4:
                    	stm = conn.createStatement();
                    	select = "select * from item where Itemid = " + itemID + " AND Platform = '" + plat + "';";
                		stm.executeQuery(select);
                		rs2 = stm.getResultSet();
                		while(rs2.next()){
                           	System.out.println("Current Title: " + rs2.getString("Title"));
                		}
                		System.out.println("Input new Title: ");
                		temp = new Scanner(System.in);
                        String Title = temp.next();
                        sql2 = "update item set Title = '" + Title +"' where ItemId = " +
                        itemID + " AND Platform = '" + plat +"'";
                        count = stm.executeUpdate(sql2);
                        stm.close();
                        System.out.println(count + " row(s) was/were updated");
                        break;
                    case 5:
                    	stm = conn.createStatement();
                    	select = "select * from item where Itemid = " + itemID + " AND Platform = '" + plat + "';";
                		stm.executeQuery(select);
                		rs2 = stm.getResultSet();
                		while(rs2.next()){
                          	System.out.println("Current Price: " + rs2.getInt("Price"));
                		}
                		System.out.println("Input new Price: ");
                		temp = new Scanner(System.in);
                        int Price = temp.nextInt();
                        sql2 = "update item set Price = '" + Price +"' where ItemId = " +
                        itemID + " AND Platform = '" + plat +"'";
                        count = stm.executeUpdate(sql2);
                        stm.close();
                        System.out.println(count + " row(s) was/were updated");
                        break;
                    case 6:
                    	stm = conn.createStatement();
                    	select = "select * from item where Itemid = " + itemID + " AND Platform = '" + plat + "';";
                		stm.executeQuery(select);
                		rs2 = stm.getResultSet();
                		while(rs2.next()){
                        	System.out.println("Current ImageURL: " + rs2.getString("ImageUrl"));
                		}
                		System.out.println("Input new ImageURL: ");
                		temp = new Scanner(System.in);
                        String ImageUrl = temp.next();
                        sql2 = "update item set ImageUrl = '" + ImageUrl +"' where ItemId = " +
                        itemID + " AND Platform = '" + plat +"'";
                        count = stm.executeUpdate(sql2);
                        stm.close();
                        System.out.println(count + " row(s) was/were updated");
                        break;
                    case 7:
                    	stm = conn.createStatement();
                    	select = "select * from item where Itemid = " + itemID + " AND Platform = '" + plat + "';";
                		stm.executeQuery(select);
                		rs2 = stm.getResultSet();
                		while(rs2.next()){
                        	System.out.println("Current Description: " + rs2.getString("Description"));
                		}
                		System.out.println("Input new Description: ");
                		temp = new Scanner(System.in);
                        String Description = temp.next();
                        sql2 = "update item set Description = '" + Description +"' where ItemId = " +
                        itemID + " AND Platform = '" + plat +"'";
                        count = stm.executeUpdate(sql2);
                        stm.close();
                        System.out.println(count + " row(s) was/were updated");
                        break;
                    case 8://print updated tuple before exit
                    	stm = conn.createStatement();
                    	select = "select * from item where Itemid = " + itemID + " AND Platform = '" + plat + "';";
                		stm.executeQuery(select);
                		rs2 = stm.getResultSet();
                    	 while(rs2.next()){
                    		 System.out.println("The updated tuple:");
                    		 System.out.println("ItemID: " + rs2.getInt("ItemId") + ", Platform: " + rs2.getString("Platform")
            			    		 + ", ManfName: "+rs2.getString("ManfName") + ", Category: " + rs2.getString("Category")
            			    		 + ", Model: " + rs2.getString("Model") + ", Title: " + rs2.getString("Title") + ", Price: "+
            			    		 rs2.getInt("Price") + ", ImageURL: " + rs2.getString("ImageUrl") + ", Description: " + rs2.getString("Description"));
            		     }
                    	 stm.close();
            		  //   st.close();
                    	 quit=true;
                    	break outerloop;
                    default:
       
                    	System.out.println("Wrong input, try again, choose 1 - 8");
                }
		    }
		  
        
	    }
	    
}
