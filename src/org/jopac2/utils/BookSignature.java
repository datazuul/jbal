/*
 * BookSignature.java
 *
 * Created on 16 settembre 2004, 13.41
 */

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

/**
* @author	Romano Trampus
* @version	16/09/2004
*/
/**
 *
 * @author  romano
 */
public class BookSignature implements Comparable<BookSignature> {
    private String libraryId,libraryName,bookNumber,bookLocalization;
    private String bookCons; // consistenza del periodico se periodico
    
    /** Creates a new instance of BookSignature */
    public BookSignature(String libraryId, String libraryName, String bookNumber, String bookLocalization) {
        init(libraryId, libraryName, bookNumber, bookLocalization, null);
    }
    
    public BookSignature(String libraryId, String libraryName, String bookNumber, String bookLocalization, String bookCons) {
        init(libraryId, libraryName, bookNumber, bookLocalization, bookCons);
    }
    
    private void init(String libraryId, String libraryName, String bookNumber, String bookLocalization, String bookCons) {
        this.libraryId=libraryId;
        this.libraryName=libraryName;
        this.bookNumber=bookNumber;
        this.bookLocalization=bookLocalization;
        this.bookCons=bookCons;
    }
    
    public String getLibraryId() {return libraryId!=null?libraryId:"";}
    public String getLibraryName() {return libraryName!=null?libraryName:"";}
    public String getBookNumber() {return bookNumber!=null?bookNumber:"";}
    public String getBookLocalization() {return bookLocalization!=null?bookLocalization:"";}
    public String getBookCons() {return bookCons!=null?bookCons:"";}
    
	public boolean equals(Object obj) {
		if(obj instanceof BookSignature) {
			BookSignature t=(BookSignature) obj;
			boolean r=compare(this.getLibraryId(),t.getLibraryId())&&
				compare(this.getLibraryName(),t.getLibraryName())&&
				compare(this.getBookNumber(),t.getBookNumber())&&
				compare(this.getBookLocalization(),t.getBookLocalization())&&
				compare(this.getBookCons(),t.getBookCons());
			return r;
		}
		else
			return false;
	}
	
	private boolean compare(String s, String d) {
		boolean r=false;
		if((s==null&&d==null)||(s!=null&&d!=null&&s.equals(d))) r=true;
		return r;
	}

	public int compareTo(BookSignature bs) {
		return this.toString().compareTo(bs.toString());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return libraryId + " " + libraryName + " " + bookNumber + " " + bookLocalization;
	}
	
	




 }
