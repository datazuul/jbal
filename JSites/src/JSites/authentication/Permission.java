package JSites.authentication;

public class Permission {
	
	public static final byte ACCESSIBLE = 1;
	public static final byte EDITABLE = 2;
	public static final byte VALIDABLE = 4;
	
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
