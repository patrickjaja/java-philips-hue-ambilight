/**
 * 
 */
package hue.Windows;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

import java.awt.Robot;
import java.io.IOException;
import java.util.*;

import nl.q42.jue.Group;
import nl.q42.jue.HueBridge;
import nl.q42.jue.StateUpdate;
import nl.q42.jue.exceptions.ApiException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 *
 */
public class App {

	/**
	 * 
	 */
	public App() {
		
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws AWTException 
	 * @throws ApiException 
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable toRun = new Runnable() {
            @Override
			public void run() {
            	System.out.println("Code wird ausgeführt");

            	
					try {
						App.desktopAverage();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
            }
        };
        ScheduledFuture<?> handle = scheduler.scheduleAtFixedRate(toRun, 1, 1, TimeUnit.SECONDS);
	}
	

	public static void setRandomColor()  throws AWTException, IOException, ApiException{
		Robot rb = new Robot();
		PointerInfo a = MouseInfo.getPointerInfo();
		Point b = a.getLocation();
		int x = (int) b.getX();
		int y = (int) b.getY();
		Color color = rb.getPixelColor(x, y);
		Random rand = new Random();
		float r2 = rand.nextFloat();
		float g2 = rand.nextFloat();
		float b2 = rand.nextFloat();
		Color randomColor = new Color(r2, g2, b2);
		int rdm=App.randInt(0,10);
//        Color tmpColor=arlc.get(rdm);
//        if (color.getRed() != 0 && color.getGreen() != 0 && color.getBlue() != 0) {
		System.out.println("ZUFALL:"+rdm);
	        List<Float> colors=App.getRGBtoXY(randomColor);
	        System.out.println("Lese x=" + x + ",y="+y+". Farbe: " + color.getRed() +","+color.getGreen() + ","+ color.getBlue());
	        System.out.println("Wurde convertiert zu " + colors.get(0) + ","+colors.get(1));
	        HueBridge bridge = new HueBridge("192.168.11.2", "newdeveloper");
	//        for (Light light : bridge.getLights()) {
	//            System.out.println(light.getName());
	//            
	//        }
	        Group all = bridge.getAllGroup();
	        StateUpdate update = new StateUpdate().turnOn().setXY(colors.get(0), colors.get(1));
	        bridge.setGroupState(all, update);
//        }
	}
	
	public static void setDesktopAverage() throws AWTException, IOException, ApiException {
//		Color cl=App.desktopAverage();
//		List<Float> colors=App.getRGBtoXY(cl);
//		HueBridge bridge = new HueBridge("192.168.11.2", "newdeveloper");
//		Group all = bridge.getAllGroup();
//        StateUpdate update = new StateUpdate().turnOn().setXY(colors.get(0), colors.get(1));
//        bridge.setGroupState(all, update);
	}
	
	public static void desktopAverage() throws IOException
	{
			Robot robby;
			try {
				robby = new Robot();
				int pixel; //ARGB variable with 32 int bytes where
				//sets of 8 bytes are: Alpha, Red, Green, Blue
				float r=0;
				float g=0;
				float b=0;
			
				//get screenshot into object "screenshot" of class BufferedImage
				BufferedImage screenshot = robby.createScreenCapture(new Rectangle(new Dimension(1368,928)));
				//1368*928 is the screen resolution
//				int rdm=App.randInt(1, 1000000);
//				File outputfile = new File("saved"+rdm+".png");
//			    ImageIO.write(screenshot, "png", outputfile);
//			
				int i=0;
				int j=0;
				//1368*928
				//I skip every alternate pixel making my program 4 times faster
				for(i =0;i<1368; i=i+2){
				for(j=0; j<928;j=j+2){
				pixel = screenshot.getRGB(i,j); //the ARGB integer has the colors of pixel (i,j)
				r = r+(255&(pixel>>16)); //add up reds
				g = g+(255&(pixel>>8)); //add up greens
				b = b+(255&(pixel)); //add up blues
				}
				}
				r=r/(684*464); //average red (remember that I skipped ever alternate pixel)
				g=g/(684*464); //average green
				b=b/(684*464); //average blue
//				ArrayList<Float> al = new ArrayList<>();
//			    al.add(r);
//			    al.add(g);
//			    al.add(b);
//				Color cl=new Color (r,g,b);
				
				
//				
				if (Float.NaN != r && Float.NaN != g && Float.NaN != b ) {
					System.out.println("Farbe gefunden: " + r +","+g + ","+ b);
					List<Float> colors=App.getRGBtoXY(r,g,b);
					
					float capitalX = (float) (1.076450 * r - 0.237662 * g + 0.161212 * b);
					float capitalY = (float) (0.410964 * r + 0.554342 * g + 0.034694 * b);
					float capitalZ = (float) (-0.010954 * r - 0.013389 * g + 1.024343 * b);
					
					float calcX = capitalX / (capitalX + capitalY + capitalZ);
					float calcY = capitalY / (capitalX + capitalY + capitalZ);
					System.out.println("Farbe berechnet: " +calcX + ","+calcY);
	//				System.out.println("Farbe berechnet: " +colors.get(0) + ","+colors.get(1));
					HueBridge bridge;
					
					try {
						bridge = new HueBridge("192.168.11.2", "newdeveloper");
						Group all = bridge.getAllGroup();
				        StateUpdate update = new StateUpdate().turnOn().setXY(calcX, calcY);
				        bridge.setGroupState(all, update);
					} catch (IOException | ApiException e) {
						// TODO Auto-generated catch block
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
				}
				
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
			
			
			
			
//		    return new Color(r,g,b);
	}
	

	
	//http://stackoverflow.com/questions/22564187/rgb-to-philips-hue-hsb
	public static ArrayList<Float> getRGBtoXY(Color c) {
	    // For the hue bulb the corners of the triangle are:
	    // -Red: 0.675, 0.322
	    // -Green: 0.4091, 0.518
	    // -Blue: 0.167, 0.04
	    double[] normalizedToOne = new double[3];
	    float cred, cgreen, cblue;
	    cred = c.getRed();
	    cgreen = c.getGreen();
	    cblue = c.getBlue();
	    normalizedToOne[0] = (cred / 255);
	    normalizedToOne[1] = (cgreen / 255);
	    normalizedToOne[2] = (cblue / 255);
	    float red, green, blue;

	    // Make red more vivid
	    if (normalizedToOne[0] > 0.04045) {
	        red = (float) Math.pow(
	                (normalizedToOne[0] + 0.055) / (1.0 + 0.055), 2.4);
	    } else {
	        red = (float) (normalizedToOne[0] / 12.92);
	    }

	    // Make green more vivid
	    if (normalizedToOne[1] > 0.04045) {
	        green = (float) Math.pow((normalizedToOne[1] + 0.055)
	                / (1.0 + 0.055), 2.4);
	    } else {
	        green = (float) (normalizedToOne[1] / 12.92);
	    }

	    // Make blue more vivid
	    if (normalizedToOne[2] > 0.04045) {
	        blue = (float) Math.pow((normalizedToOne[2] + 0.055)
	                / (1.0 + 0.055), 2.4);
	    } else {
	        blue = (float) (normalizedToOne[2] / 12.92);
	    }

	    float X = (float) (red * 0.649926 + green * 0.103455 + blue * 0.197109);
	    float Y = (float) (red * 0.234327 + green * 0.743075 + blue * 0.022598);
	    float Z = (float) (red * 0.0000000 + green * 0.053077 + blue * 1.035763);

	    float x = X / (X + Y + Z);
	    float y = Y / (X + Y + Z);

	    double[] xy = new double[2];
	    xy[0] = x;
	    xy[1] = y;
//	    List<Double> xyAsList = Doubles.asList(xy);
	    ArrayList<Float> al = new ArrayList<>();
	    al.add(x);
	    al.add(y);
	    return al;
	}
	
	public static ArrayList<Float> getRGBtoXY(float r,float g, float b) {
	    // For the hue bulb the corners of the triangle are:
	    // -Red: 0.675, 0.322
	    // -Green: 0.4091, 0.518
	    // -Blue: 0.167, 0.04
	    double[] normalizedToOne = new double[3];
	    float cred, cgreen, cblue;
	    cred = r;
	    cgreen = g;
	    cblue = b;
	    normalizedToOne[0] = (cred / 255);
	    normalizedToOne[1] = (cgreen / 255);
	    normalizedToOne[2] = (cblue / 255);
	    float red, green, blue;

	    // Make red more vivid
	    if (normalizedToOne[0] > 0.04045) {
	        red = (float) Math.pow(
	                (normalizedToOne[0] + 0.055) / (1.0 + 0.055), 2.4);
	    } else {
	        red = (float) (normalizedToOne[0] / 12.92);
	    }

	    // Make green more vivid
	    if (normalizedToOne[1] > 0.04045) {
	        green = (float) Math.pow((normalizedToOne[1] + 0.055)
	                / (1.0 + 0.055), 2.4);
	    } else {
	        green = (float) (normalizedToOne[1] / 12.92);
	    }

	    // Make blue more vivid
	    if (normalizedToOne[2] > 0.04045) {
	        blue = (float) Math.pow((normalizedToOne[2] + 0.055)
	                / (1.0 + 0.055), 2.4);
	    } else {
	        blue = (float) (normalizedToOne[2] / 12.92);
	    }

	    float X = (float) (red * 0.649926 + green * 0.103455 + blue * 0.197109);
	    float Y = (float) (red * 0.234327 + green * 0.743075 + blue * 0.022598);
	    float Z = (float) (red * 0.0000000 + green * 0.053077 + blue * 1.035763);

	    float x = X / (X + Y + Z);
	    float y = Y / (X + Y + Z);

	    double[] xy = new double[2];
	    xy[0] = x;
	    xy[1] = y;
//	    List<Double> xyAsList = Doubles.asList(xy);
	    ArrayList<Float> al = new ArrayList<>();
	    al.add(x);
	    al.add(y);
	    return al;
	}
	
	/**
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}

}
