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

public class Parametro implements Comparable<Object>
{
    private Object chiave;
    private Object valore;
    
    public Parametro(){};
    
    public Parametro(Object c,Object v)
    {
        chiave = c;
        valore = v;
    }
		
    public void set(Object c,Object v)
    {
	chiave = c;
	valore = v;
    }
	
    public Object getChiave(){return chiave;}
    public Object getValore(){return valore;}
    
    public String toString() {
        return "("+chiave.toString()+":"+valore.toString()+")";
      }
    
    public int compareTo(Object arg0) {
        Parametro p = (Parametro) arg0;
        return ((String)valore).compareTo((String)p.getValore());
    }
}