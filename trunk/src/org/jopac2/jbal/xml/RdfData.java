package org.jopac2.jbal.xml;

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

/*
* @author	Romano Trampus
* @version 17/02/2005
*/

import java.util.Vector;

import org.jopac2.utils.BookSignature;

/**
 * @author romano
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

@SuppressWarnings("unchecked")
public class RdfData {
	private Vector autori=new Vector();
	private Vector editori=new Vector();
	private Vector soggetti=new Vector();
	private Vector classificazioni=new Vector();
	private Vector pubDate=new Vector();
	private Vector pubPlace=new Vector();
	private Vector titolo=new Vector();
	private Vector language=new Vector();
	private Vector identifier=new Vector();
	private Vector isPartOf=new Vector();
	private Vector about=new Vector();
	private Vector collocazioni=new Vector();
	private Vector citations=new Vector();
	private Vector description=new Vector();

	private Vector linkUp=new Vector();
	private Vector linkDown=new Vector();
	private Vector linkSerie=new Vector();
	private Vector source=new Vector();
	
	
	public void addAuthor(String Author) {autori.addElement(Author);}
	public void addSubject(String Subject) {soggetti.addElement(Subject);}
	public void addClassification(String Classification) {classificazioni.addElement(Classification);}
	public void addEditor(String Editor) {editori.addElement(Editor);}
	public void addPublicationPlace(String Place) {pubPlace.addElement(Place);}
	public void addPublicationDate(String Date) {pubDate.addElement(Date);}
	public void addSignature(BookSignature Signature) {collocazioni.addElement(Signature);}
	public void addTitle(String Title) {titolo.addElement(Title);}
	public void addLanguage(String Language) {language.addElement(Language);}
	public void addLinkUp(RdfData Data) {linkUp.addElement(Data);}
	public void addLinkDown(RdfData Data) {linkDown.addElement(Data);}
	public void addLinkSerie(RdfData Data) {linkSerie.addElement(Data);}
	public void addIdentifier(String Identifier) {identifier.addElement(Identifier);}
	public void addIsPartOf(String IsPartOf) {isPartOf.addElement(IsPartOf);}
	public void addCitation(String Citation) {citations.addElement(Citation);}
	public void addAbout(String About) {about.addElement(About);}
	public void addDescription(String Description) {description.addElement(Description);}
	public void addSource(String Source) {source.addElement(Source);}
	
	public Vector getAuthors() {return autori;}
	public Vector getAbout() {return about;}
	public Vector getSubjects() {return soggetti;}
	public Vector getClassifications() {return classificazioni;}
	public Vector getEditors() {return editori;}
	public Vector getPublicationPlace() {return pubPlace;}
	public Vector getPublicationDate() {return pubDate;}
	public Vector getSignatures() {return collocazioni;}
	public Vector getLanguage() {return language;}
	public Vector getTitle() {return titolo;}
	public Vector getIdentifier() {return identifier;}
	public Vector getISBD() {return titolo;}
	public Vector getLinkUp() {return linkUp;}
	public Vector getLinkDown() {return linkDown;}
	public Vector getLinkSerie() {return linkSerie;}
	public Vector getIsPartOf() {return isPartOf;}
	public Vector getCitations() {return citations;}
	public Vector getDescription() {return description;}
	public Vector getSource() {return source;}
}
