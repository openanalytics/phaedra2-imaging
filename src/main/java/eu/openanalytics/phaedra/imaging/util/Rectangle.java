package eu.openanalytics.phaedra.imaging.util;

public class Rectangle {

	public int x;
	public int y;
	public int width;
	public int height;

	public Rectangle(int x, int y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Rectangle intersect(Rectangle other) {
		java.awt.Rectangle r1 = new java.awt.Rectangle(this.x, this.y, this.width, this.height);
		java.awt.Rectangle r2 = new java.awt.Rectangle(other.x, other.y, other.width, other.height);
		java.awt.Rectangle intersection = r1.intersection(r2);
		return of(intersection.x, intersection.y, intersection.width, intersection.height);
	}

	public int[] getCoordinates() {
		return new int[] { x, y, width, height };
	}
	
	public static Rectangle of(int x, int y, int width, int height) {
		return new Rectangle(x, y, width, height);
	}
	
	public static Rectangle of(int[] coordinates) {
		if (coordinates.length == 2) {
			return new Rectangle(0, 0, coordinates[0], coordinates[1]);
		} else if (coordinates.length == 4) {
			return new Rectangle(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
		}
		return null;
	}
}
