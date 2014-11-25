

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;


public class chart {
	ChartPanel frame1;
	public  chart(ArrayList<platform> pl){

		CategoryDataset dataset = getDataSet(pl);
        JFreeChart chart = ChartFactory.createBarChart3D(
       		                "Comparsion of Sells", // title
                            "Platforms", // X axis
                            "Number of Sells", // Y axis
                            dataset, // dataset
                            PlotOrientation.VERTICAL, // horizontal, vertical
                            false,
                            true,
                            false           
                            );
        //code below is to adjust the font size and style
	    CategoryPlot plot=chart.getCategoryPlot();
	    CategoryAxis domainAxis=plot.getDomainAxis();         
	    domainAxis.setLabelFont(new Font("Comic Sans MS",Font.BOLD,15));        
	    domainAxis.setTickLabelFont(new Font("Comic Sans MS",Font.BOLD,15)); 
	    ValueAxis rangeAxis=plot.getRangeAxis();//
	    rangeAxis.setLabelFont(new Font("Comic Sans MS",Font.BOLD,15));  
        frame1=new ChartPanel(chart,true);       
         
	}
	   private static CategoryDataset getDataSet(ArrayList<platform> pl) {
           DefaultCategoryDataset dataset = new DefaultCategoryDataset();
           while(!pl.isEmpty()){
        	   platform temp = pl.remove(0);
        	   dataset.addValue(temp.totalSells, "Total Sells", temp.name);
           }
 
           return dataset;
}
public ChartPanel getChartPanel(){
	return frame1;
	
}
public void print(){
	//create JFrame first and adjust its size and position.
	JFrame frame=new JFrame("Total Sells");
	frame.setBounds(300,300,300,300);
	frame.setSize(1000, 600);     
	frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
	frame.setAlwaysOnTop (true);
	frame.setVisible(true);
	frame.add(getChartPanel()); 
}




}
