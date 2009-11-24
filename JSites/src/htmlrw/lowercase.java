package htmlrw;


import java.io.File;


public class lowercase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dir="/eut.1";
		subdir(dir);
		

	}
	
	public static void subdir(String dir) {
		File directory=new File(dir);
		if(directory.isDirectory()) {
			String[] cnt=directory.list();
			for(int i=0;i<cnt.length;i++) {
				//System.out.println(dir+"/"+cnt[i]);
				subdir(dir+"/"+cnt[i]);
			}
		}
		
		if(directory.isFile()) {
			fixfilename(dir);
		}
	}
	
	public static void fixfilename(String file) {
		String path=file.substring(0,file.lastIndexOf("/")+1);
		file=file.substring(file.lastIndexOf("/")+1);
		String extension="."+file.substring(file.lastIndexOf(".")+1);
		file=file.substring(0,file.lastIndexOf("."));
		String nfile=file;
		nfile=nfile.toLowerCase().replaceAll("ˆ", "a");
		nfile=nfile.replaceAll("˜", "o").replaceAll("", "u");
		nfile=nfile.replaceAll("\\W", "_");
		
		if(!file.equals(nfile)) {
			File nf=new File(path+nfile+extension);
			File of=new File(path+file+extension);
			
			if(!of.renameTo(nf)) {
				System.out.println("ERR "+nfile+extension);
			}

		}
		

	}


}
