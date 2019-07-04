<%--

    Copyright (C) 2019 University of York, UK.

    This project was initiated through a donation of source code by the
    University of York, UK. It contains free software; you can redistribute
    it and/or modify it under the terms of the GNU General Public License as
    published by the Free Software Foundation; either version 2 of the
    License, or any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

    For more information please contact:

    Web Services Group
    IT Service
    University of York
    YO10 5DD
    United Kingdom

--%>

<%--
 @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 @version $Revision$ $Date$
 --%>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- <%@ page errorPage="/error.jsp"%> --%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>

<%-- In order to allow them to be used by the Bb tags --%>
<fmt:message var="lSearch" key="xid.locator.label.search"/>
<fmt:message var="lFileName" key="xid.locator.label.file_name"/>
<fmt:message var="lFilePath" key="xid.locator.label.file_path"/>
<fmt:message var="lFileSizeM" key="xid.locator.label.file_size_m"/>
<fmt:message var="lFileSize" key="xid.locator.label.file_size"/>
<fmt:message var="lCreatedDate" key="xid.locator.label.create_date"/>
<fmt:message var="lModifiedDate" key="xid.locator.label.modified_date"/>
<fmt:message var="lPermanentId" key="xid.locator.label.permanent_id"/>
<fmt:message var="lPermanentUrl" key="xid.locator.label.permanent_url"/>
<fmt:message var="lError" key="xid.locator.label.error"/>
<fmt:message var="lViewUrl" key="xid.locator.label.view_url"/>
<fmt:message var="lFileType" key="xid.locator.label.file_type"/>
<fmt:message var="lFileContentType" key="xid.locator.label.file_content_type"/>
<fmt:message var="lFileMimeType" key="xid.locator.label.file_mime_type"/>
<fmt:message var="lFileSystemPath" key="xid.locator.label.file_system_path"/>
<fmt:message var="tSearch" key="xid.locator.title.step.search"/>
<fmt:message var="tSearchResult" key="xid.locator.title.step.search_result"/>


<c:choose>
	<c:when test="${courseTool}">
		<fmt:message var="entitlement" key="xid.locator.security.entitlement.course"/>
		<fmt:message var="title" key="xid.locator.title.course_tool"/>
	</c:when>
	<c:when test="${systemTool}">
		<fmt:message var="entitlement" key="xid.locator.security.entitlement.system"/>
		<fmt:message var="title" key="xid.locator.title.system_tool"/>
	</c:when>
	<c:otherwise>
		<fmt:message var="entitlement" key="xid.locator.security.entitlement.system"/>
		<fmt:message var="title" key="xid.locator.title"/>
	</c:otherwise>
</c:choose>

<bbNG:learningSystemPage authentication='Y' ctxId="ctx" entitlement="${entitlement}"
	title="${title}">

	<c:choose>
		<c:when test="${courseTool}">
			<bbNG:breadcrumbBar environment="CTRL_PANEL">
		        <bbNG:breadcrumb>${title}</bbNG:breadcrumb>
		    </bbNG:breadcrumbBar>
	    </c:when>
		<c:when test="${systemTool}">
			<bbNG:breadcrumbBar environment="SYS_ADMIN" navItem="admin_main">
		        <bbNG:breadcrumb> ${title}</bbNG:breadcrumb>
			</bbNG:breadcrumbBar>
		</c:when>
	</c:choose>
	<bbNG:pageHeader>
		<bbNG:pageTitleBar title="${title}">${title}</bbNG:pageTitleBar>
	</bbNG:pageHeader>

	<form:form action="" modelAttribute="form" method="post">
		<%-- putting if then else inside the bbNG:dataCollection element will not work --%>
		<c:choose>
			<c:when test="${not empty resource}">
				<bbNG:dataCollection>
					<%@ include file="/WEB-INF/include/form/search.jsp"%>
					<bbNG:step title="${tSearchResult}">
						
						<bbNG:dataElement label="${lFileName}">
							<p>${resource.baseName}</p>
						</bbNG:dataElement>
						
						<bbNG:dataElement label="${lFilePath}">
							<p>${resource.location}</p>
						</bbNG:dataElement>
						
						<bbNG:dataElement label="${lFileSizeM}">
							<p><fmt:formatNumber value="${resource.fileSize/1024/1024}" pattern="0.00" />M</p>
						</bbNG:dataElement>
						
						<bbNG:dataElement label="${lFileSize}">
							<p>${resource.fileSize}</p>
						</bbNG:dataElement>
						
						<bbNG:dataElement label="${lCreatedDate}">
							<p><fmt:formatDate value="${resource.createdDate.time}" type="both" dateStyle="short" /></p>
						</bbNG:dataElement>
						
						<bbNG:dataElement label="${lModifiedDate}">
							<p><fmt:formatDate value="${resource.modifiedDate.time}" type="both" dateStyle="short" /></p>
						</bbNG:dataElement>
						<bbNG:dataElement label="${lPermanentId}">
							<p>${resource.permanentId}</p>
						</bbNG:dataElement>
						<bbNG:dataElement label="${lPermanentUrl}">
							<p>${resource.permanentURL}</p>
						</bbNG:dataElement>
						
						<bbNG:dataElement label="${lViewUrl}">
							<p>${resource.viewURL}</p>
						</bbNG:dataElement>
						
						<bbNG:dataElement label="${lFileType}">
							<p>${resource.type}</p>
						</bbNG:dataElement>
						
						<%-- Xythos private API has been used here --%>
						<bbNG:dataElement label="${lFileContentType}">
							<p>${xythosFile.fileContentType}</p>
						</bbNG:dataElement>
						
						<bbNG:dataElement label="${lFileMimeType}">
							<p>${xythosFile.fileMimeType}</p>
						</bbNG:dataElement>
						
						<bbNG:dataElement label="${lFileSystemPath}">
							<p>${xythosFile.fileVersionExternalStorageFileName}</p>
						</bbNG:dataElement>
						
					</bbNG:step>
					<bbNG:stepSubmit />
				</bbNG:dataCollection>
			</c:when>
			<c:when test="${resourceNotFound}">
				<bbNG:dataCollection>
					<%@ include file="/WEB-INF/include/form/search.jsp"%>
					<bbNG:step title="${tSearchResult}">
						<bbNG:dataElement label="${lError}">
							<p><strong><fmt:message key="xid.locator.message.resource_not_found"/></strong></p>
						</bbNG:dataElement>
					</bbNG:step>
					<bbNG:stepSubmit />
				</bbNG:dataCollection>
			</c:when>
			<c:otherwise>
				<bbNG:dataCollection>
					<%@ include file="/WEB-INF/include/form/search.jsp"%>
					<bbNG:stepSubmit />
				</bbNG:dataCollection>
			</c:otherwise>
		</c:choose>
	</form:form>
	
</bbNG:learningSystemPage>

