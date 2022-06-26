package it.polito.tdp.yelp.model;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class Adiacenza {
	private Business b1;
	private Business b2;
	private LatLng l1;
	private LatLng l2;
	public Adiacenza(Business b1, Business b2, LatLng l1, LatLng l2) {
		super();
		this.b1 = b1;
		this.b2 = b2;
		this.l1 = l1;
		this.l2 = l2;
	}
	public Business getB1() {
		return b1;
	}
	public void setB1(Business b1) {
		this.b1 = b1;
	}
	public Business getB2() {
		return b2;
	}
	public void setB2(Business b2) {
		this.b2 = b2;
	}
	public LatLng getL1() {
		return l1;
	}
	public void setL1(LatLng l1) {
		this.l1 = l1;
	}
	public LatLng getL2() {
		return l2;
	}
	public void setL2(LatLng l2) {
		this.l2 = l2;
	}

	public double getDistanza() {
		return LatLngTool.distance(l2, l1, LengthUnit.KILOMETER);
	}
		
}
