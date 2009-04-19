package org.jopac2.utils;
/*******************************************************************************
*
*  JOpac2 (C) 2002-2007 JOpac2 project
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
public class ClasseDettaglio implements Comparable<Object> {
	long id,idTipo,idClasse;
	String tag,dataElement;

	public ClasseDettaglio(long id, long idTipo, long idClasse, String tag, String dataElement) {
		super();
		this.id=id;
		this.idTipo=idTipo;
		this.idClasse=idClasse;
		this.tag=tag.trim();
		this.dataElement=dataElement.trim();
	}

	/**
	 * @return Returns the dataElement.
	 */
	public String getDataElement() {
		return dataElement;
	}

	/**
	 * @param dataElement The dataElement to set.
	 */
	public void setDataElement(String dataElement) {
		this.dataElement = dataElement.trim();
	}

	/**
	 * @return Returns the id.
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return Returns the idClasse.
	 */
	public long getIdClasse() {
		return idClasse;
	}

	/**
	 * @param idClasse The idClasse to set.
	 */
	public void setIdClasse(long idClasse) {
		this.idClasse = idClasse;
	}

	/**
	 * @return Returns the idTipo.
	 */
	public long getIdTipo() {
		return idTipo;
	}

	/**
	 * @param idTipo The idTipo to set.
	 */
	public void setIdTipo(long idTipo) {
		this.idTipo = idTipo;
	}

	/**
	 * @return Returns the tag.
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag The tag to set.
	 */
	public void setTag(String tag) {
		this.tag = tag.trim();
	}

	/**
	 * Confronta l'oggetto o con l'istanza e restistuisce l'ordine dell'ID
	 * @param o
	 * @return
	 */
	public int compareTo(Object o) {
		ClasseDettaglio cld=(ClasseDettaglio)o;
		long t=cld.getId()-this.id;
		if(t>0) t=1;
		if(t<0) t=-1;
		return (int)t;
	}
	
	/**
	 * @return Returns true if object has the same values as this except at -1 or null position.
	 */
	public boolean equals(Object object) {
		boolean ret=false;
		ClasseDettaglio cld=(ClasseDettaglio)object;
		
		if(((cld.getId()==-1)||(id==-1)||(cld.getId()==id))&&
				((cld.getIdTipo()==-1)||(idTipo==-1)||(cld.getIdTipo()==idTipo))&&
				((cld.getIdClasse()==-1)||(idClasse==-1)||(cld.getIdClasse()==idClasse))&&
				((cld.getTag()==null)||(tag==null)||(cld.getTag().equals(tag)))&&
				((cld.getDataElement()==null)||(dataElement==null)||(cld.getDataElement().equals(dataElement)))) 
			ret=true;
		
		
		return ret;
	}
}
