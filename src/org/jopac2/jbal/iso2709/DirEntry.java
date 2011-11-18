package org.jopac2.jbal.iso2709;

import org.jopac2.utils.JOpac2Exception;

public class DirEntry {
	private String tag;
	private int fieldLength=-1, startPosition=-1;
	
	
	public DirEntry(String entryString,int entryLengthOfFieldLength,int entryStartPositionLength,int entryImplementationDefinedLength,int entryReservedFutureUse) throws JOpac2Exception {
        int entryLength = entryLengthOfFieldLength + entryStartPositionLength + entryImplementationDefinedLength + 3; // 3 = tag length
        if(entryString==null || entryString.length()!=entryLength) throw new JOpac2Exception("DirEntry is null or invalid length");
        
        setTag(entryString.substring(0,3)); // tag is 3 byte length
        
        String t=entryString.substring(3,3+entryLengthOfFieldLength);
	    setFieldLength(Integer.parseInt(t));
	    t=entryString.substring(3+entryLengthOfFieldLength,3+entryLengthOfFieldLength+entryStartPositionLength);
	    setStartPosition(Integer.parseInt(t) + 1);
	}


	public void setTag(String tag) {
		this.tag = tag;
	}


	public String getTag() {
		return tag;
	}


	public void setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
	}


	public int getFieldLength() {
		return fieldLength;
	}


	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}


	public int getStartPosition() {
		return startPosition;
	}
}
