package org.jopac2.utils;

/*******************************************************************************
*
*  JOpac2 (C) 2002-2008 JOpac2 project
*
*     This file is part of JOpac2. http://jopac2.sourceforge.net
*
*  JOpac2 is free software; you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation; either version 2 of the License, or
*  (at your option) any later version.
*
*  JOpac2 is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with JOpac2; if not, write to the Free Software
*  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*
*******************************************************************************/


public final class JbalHelper {
	  public static final String SEPARATORI_PAROLE=" ,.;()/-'\\:=@%$&!?[]#<>\016\017\n\t";

	  
	  // determina se eliminare parole (inizia per ESC+H e ESC+I o *)
	  /**
	   * TODO: dovrebbe essere in MARC o ISO2709 piuttosto?
	   * 07/03/2003 - R.T.
	   *      Cambiato: ai fini dell'indicizzazione vanno inserite anche le
	   *      parole prima della parte significativa del titolo.
	   *      Quindi semplicemente se c'è l'asterisco lo tolgo e se ci sono
	   *      i marcatori li tolgo MA LASCIO LE PAROLE INCLUSE
	   */
	  public static String processaMarcatori(String valore) {
	    //int posiz_asterisco=valore.indexOf("*");
	    String returnValue=valore;
	    //if((posiz_asterisco>0)&&(posiz_asterisco<=MAX_POSIZIONE_ASTERISCO)) {
	      //returnValue=returnValue.replaceFirst("\\*","");
	    //}
	    returnValue=returnValue.replaceAll((String.valueOf((char)0x1b)+"H"),"");
	    returnValue=returnValue.replaceAll((String.valueOf((char)0x1b)+"I"),"");

	    return returnValue;
	  }
}
