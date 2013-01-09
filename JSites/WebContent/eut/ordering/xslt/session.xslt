<?xml version="1.0" encoding="UTF-8"?>

<!-- CVS $Id: session.xslt,v 1.2 2007-01-31 17:34:46 romano Exp $ -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
							  xmlns:xi="http://www.w3.org/2001/XInclude"
							  xmlns:c="http://apache.org/cocoon/xpointer"
							  xmlns:sql="http://apache.org/cocoon/SQL/2.0"
							  xmlns:h="http://apache.org/cocoon/request/2.0"
							  xmlns:session="http://apache.org/cocoon/session/1.0">


	<xsl:template match="/">
		<content>
			<xsl:copy-of select="." />
			<session:createcontext name="orderContext"/>
			
			<!-- 
			<currentQuant>
				<session:getxml context="orderContext" path="/carrello/ordine[@rid={//h:request/h:requestParameters/h:parameter[@name='id']}]/quant/" />
			</currentQuant>
			 -->
			 <sessioncontent>
			 	<session:getxml context="orderContext" path="/" />
			 </sessioncontent>
		</content>
	</xsl:template>

</xsl:stylesheet>