package JSites.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.TreeSet;

public class PageTree implements Comparable<PageTree>{
	
	private String rootName;
	private long rootPid;
	private TreeSet<PageTree> childs;
	private boolean isLeaf;
	
	public static String aggiungiPagina="[ aggiungi pagina ]", aggiungiLink="[ aggiungi link esterno ]";

	public TreeSet<PageTree> getChilds() {
		return childs;
	}

	public void setChilds(TreeSet<PageTree> childs) {
		if(rootName.startsWith("[ aggiungi "))
			this.childs = new TreeSet<PageTree>();
		else
			this.childs = childs;
	}
	
	public PageTree() {
		rootName = "";
		rootPid = 0;
		childs = new TreeSet<PageTree>();
		isLeaf = true;
	}

	public PageTree(TreeSet<PageTree> childs, boolean leaf, String name, long pid) {
		isLeaf = leaf;
		rootName = name;
		rootPid = pid;
		if(rootName.startsWith("[ aggiungi "))
			this.childs = new TreeSet<PageTree>();
		else
			this.childs = childs;
	}
	
	public PageTree(TreeSet<PageTree> childs, boolean leaf, long pid, Connection conn) {
		isLeaf = leaf;
		try {
			rootName = DBGateway.getPageName(pid, conn);
		} catch (SQLException e) {
			rootName ="Could not get page name from DB";
		}
		rootPid = pid;
		if(rootName.startsWith("[ aggiungi "))
			this.childs = new TreeSet<PageTree>();
		else
			this.childs = childs;
	}

	public boolean isLeaf() {
		if(rootName.startsWith("[ aggiungi ")){return true;}
		else
			return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		if(rootName.startsWith("[ aggiungi ")){}
		else
			this.isLeaf = isLeaf;
	}

	public String getRootName() {
		return rootName;
	}

	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	public long getRootPid() {
		return rootPid;
	}

	public void setRootPid(long rootPid) {
		this.rootPid = rootPid;
	}
	
	public String toString(){
		String s = rootPid + " " + rootName + " ";
		if(this.isLeaf())s=s+"is leaf";
		else s=s+"is not leaf, ";
		if(childs!=null)s=s + childs;
		return s + "\n";
	}

	public int compareTo(PageTree p) {
		int ret;
		if(rootName.startsWith("Altr") || rootName.equals(aggiungiPagina) || rootName.equals(aggiungiLink)) ret = 1;
		else if(rootName.equals(p.rootName))
			ret = (int) Math.signum(rootPid - p.getRootPid());
		else ret = rootName.compareTo(p.getRootName());
		return ret;
	}
	
	public void addChildren(TreeSet<PageTree> childs) {
		if(rootName.startsWith("[ aggiungi ")){}
		else
			this.addChildren(childs);
	}

	public void replaceChildren(TreeSet<PageTree> childs) {
		if(rootName.startsWith("[ aggiungi ")){}
		else{
			Iterator<PageTree> i = childs.iterator();
			PageTree ts = null;
			while(i.hasNext()){
				ts = i.next();
				if(this.childs.contains(ts)){
					this.childs.remove(ts);
					this.childs.add(ts);
				}
			}
		}
	}
	
	public void addAndReplaceChildren(TreeSet<PageTree> childs) {
		if(rootName.startsWith("[ aggiungi ")){}
		else{
			Iterator<PageTree> i = childs.iterator();
			PageTree ts = null;
			while(i.hasNext()){
				ts = i.next();
				if(this.childs.contains(ts)){
					this.childs.remove(ts);
				}
				this.childs.add(ts);
			}
		}
	}

}
