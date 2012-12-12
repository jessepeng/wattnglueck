package org.fu.swphcc.wattnglueck;

import java.util.Date;

/**
 * Bietet die M�glichkeit Z�hlerst�nde zu speichern und 
 * zus�tzlich diverse statische Methoden um Z�hlerst�nde zu verarbeiten
 * 
 * @author Necros
 *
 */
public class Zaehlerstand {
	
	private Double Zaehlerstand;
	private Date date;
	
	public Double getZaehlerstand() {
		return Zaehlerstand;
	}
	public void setZaehlerstand(Double zaehlerstand) {
		Zaehlerstand = zaehlerstand;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
