package JSites.authentication;

/*******************************************************************************
*
*  JOpac2 (C) 2002-2009 JOpac2 project
*
*     This file is part of JOpac2. http://www.jopac2.org
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
*  Please, see NOTICE.txt AND LEGAL directory for more info. Different licences
*  may apply for components included in JOpac2.
*
*******************************************************************************/

public class Permission {
	
	public static final byte ACCESSIBLE = 1;
	public static final byte EDITABLE = 2;
	public static final byte VALIDABLE = 4;
	public static final byte SFA = 8; // super figo administrator
	
	private static final byte DEFAULT = 0;
	
	private byte stato;
	
	public Permission(){
		stato = DEFAULT;
	}
	
	public void setPermission(byte p){
		stato |= p;
	}
	
	public void removePermission(byte p){
		stato |= (255^p);
	}
	
	public boolean hasPermission(byte p){
		return (stato & p) == p;
	}
	
	public void erasePermissions(){
		stato = DEFAULT;
	}
	
	public void setPermissionCode(byte b){
		stato = b;
	}
	
	public byte getPermissionCode(){
		return stato;
	}
	
}
