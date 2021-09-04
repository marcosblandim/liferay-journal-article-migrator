<%@ page import="com.liferay.portal.kernel.portlet.LiferayWindowState" %>
<%@ include file="/init.jsp" %>


<liferay-portlet:actionURL name="/journal_article/import" var="importJournalArticleURL" />
<liferay-portlet:renderURL var="selectFolder" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
    <liferay-portlet:param name="mvcPath" value="/journal_article/select_folder.jsp" />
</liferay-portlet:renderURL>


<liferay-ui:error key="internal-server-error" message="internal-server-error"/>
<liferay-ui:error key="invalid-form-input" message="invalid-form-input"/>
<%-- TODO: inform which input is invalid --%>
<liferay-ui:success key="imported-successfully" message="imported-successfully"/>


<aui:form action="<%= importJournalArticleURL %>" method="post" name="fm">

    <aui:input name="import-file" type="file" accept="application/JSON"
               helpMessage="import-file-help-message" required="<%= true %>"/>

    <aui:select name="journal-article-structure" helpMessage="journal-article-structure-help-message" required="<%= true %>">
        <aui:option label="select" value="" disabled="<%= true %>" selected="<%= true %>" />

        <c:forEach items="${journalArticleStructures}" var="journalArticleStructure" >
            <aui:option label="${journalArticleStructure.getName(locale)}" value="${journalArticleStructure.getStructureId()}" />
        </c:forEach>
    </aui:select>

    <aui:input name="output-folder-id" required="<%= true %>" type="hidden"/>
    <aui:input name="output-folder-path" required="<%= true %>" helpMessage="output-folder-path-help-message"
               type="resource" value="" />
    <aui:button href="<%= selectFolder %>" useDialog="true" value="select-folder"/>

    <aui:input name="default-folder" type="checkbox" helpMessage="default-folder-help-message"
               checked="<%= false %>" />

    <%-- TODO: disable output-folder selector if default-folder checkbox is checked --%>
    <%-- TODO: update language.properties --%>

    <aui:input name="reset-folder" type="checkbox" helpMessage="reset-folder-help-message"
               checked="<%= false %>" />

    <aui:button value="execute" type="submit"/>
</aui:form>
