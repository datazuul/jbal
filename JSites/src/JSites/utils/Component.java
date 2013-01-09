package JSites.utils;

public class Component {
	private int cid,pacid,state,ordernumber;
	
	public static int WORKING=1;
	public static int PENDING=2;
	public static int ACTIVE=3;
	public static int OLD=4;
	public static int DELETED=5;

	public Component(int cid, int pacid, int state, int ordernumber) {
		super();
		this.cid = cid;
		this.pacid = pacid;
		this.state = state;
		this.ordernumber = ordernumber;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getPacid() {
		return pacid;
	}

	public void setPacid(int pacid) {
		this.pacid = pacid;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(int ordernumber) {
		this.ordernumber = ordernumber;
	}

}
