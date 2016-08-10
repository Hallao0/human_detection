package imageLib;


import java.util.LinkedList;
import java.util.List;


class Vect {
	public int dw;
	public int dh;
	
	public double magnitude;
	public double direction;
	
	public Vect(int dw, int dh) {
		this.dw = dw;
		this.dh = dh;
		
		magnitude = Math.sqrt(dw * dw + dh * dh);
		direction = Math.atan2(dw, dh);
		if (direction < 0) {
			direction = direction + Math.PI;
		}
		direction = 180 * direction / Math.PI;
	}
	
}


class Vote {
	public int bin;
	public double magnitude;
	public Vote(int bin, double magnitude) {
		this.bin = bin;
		this.magnitude = magnitude;
	}
	public double amount(double arg) {
		int center = bin * 20 + 10;
		if (arg <= center - 20 || arg >= center + 20) {
			return 0;
		}
		if (arg <= center) {
			return magnitude * (arg - (center - 20)) / 20.0;
		}
		return magnitude * ((center + 20) - arg) / 20.0;
	}
	
}

public class Cell {
	public static int dim = 10;
	
	public int x;
	public int y;
	public Image img;
	
	public Cell(int x, int y, Image img) {
		this.x = x;
		this.y = y;
		this.img = img;
	}
	
	public Vect gradient(int x, int y) throws Exception {
		//Ix(r, c) = I(r, c + 1) − I(r, c − 1) and Iy(r, c) = I(r − 1, c) − I(r + 1, c)
		int ix = img.getIntensity(x + 1, y) - img.getIntensity(x - 1, y);
		int iy = img.getIntensity(x, y - 1) - img.getIntensity(x, y + 1);
		return new Vect(ix, iy);
	}
	
	public List<Double> cellHistogram() {
		double [] hist = new double[9];
		for (int i = 0; i < 9; i++) {
			hist[i] = 0;
		}
		for (int i = x; i < x + dim; i++) {
			for (int j = y; j < y + dim; j++) {
				try {
					Vect cur = gradient(i, j);
					for (int k = 0; k < 9; k++) {
						Vote vote = new Vote(k, cur.magnitude);
						hist[k] = hist[k] + vote.amount(cur.direction);
					}
				} catch (Exception e) {
					//System.out.println("exc! " + i + ":" + j);
				}
			}
		}
		
		LinkedList<Double> res = new LinkedList<>();
		for (int i = 0; i < 9; i++) {
			res.add(hist[i]);
		}
		return res;
	}
	
}
