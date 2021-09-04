<%@ page import="com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil" %>
<%@ page import="com.liferay.asset.kernel.model.AssetRenderer" %>
<%@ page import="com.liferay.asset.kernel.model.AssetRendererFactory" %>
<%@ page import="com.liferay.journal.model.JournalFolder" %>
<%@ page import="com.liferay.journal.service.JournalArticleLocalServiceUtil" %>
<%@ page import="com.liferay.journal.service.JournalFolderLocalServiceUtil" %>
<%@ page import="com.liferay.portal.kernel.portlet.LiferayWindowState" %>
<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %>
<%@ page import="com.liferay.portal.kernel.util.ParamUtil" %>
<%@ page import="com.liferay.portal.kernel.workflow.WorkflowConstants" %>
<%@ page import="javax.portlet.PortletURL" %>
<%@ page import="com.liferay.petra.string.StringPool" %>
<%@ page import="com.github.marcosblandim.JournalArticleUtil" %>
<%@ page import="com.liferay.journal.constants.JournalFolderConstants" %>
<%@ include file="../init.jsp" %>

<%
    long folderId = ParamUtil.getLong(request, "folderId", JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID);

    if (folderId == JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
        request.setAttribute("folderId", JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID);
    }

    long parentFolderId = folderId != JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID ?
            JournalFolderLocalServiceUtil.getJournalFolder(folderId).getParentFolderId() : JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID;

    PortletURL portletURL = renderResponse.createRenderURL();

    portletURL.setParameter("mvcPath", "/select_folder.jsp");
    portletURL.setParameter("folderId", String.valueOf(folderId));

    String folderPathBreadCrumb = JournalArticleUtil.getJournalFolderPathBreadcrumb(folderId);
    request.setAttribute("folderPathBreadCrumb", folderPathBreadCrumb);
%>

<div class="container-fluid-1280 mt-2">
    <span>
        <%= folderPathBreadCrumb %>
    </span>

    <aui:button-row>
        <liferay-portlet:renderURL varImpl="goBackURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
            <portlet:param name="mvcPath" value="/select_folder.jsp" />
            <portlet:param name="folderId"
                           value="<%= String.valueOf(parentFolderId) %>"/>
        </liferay-portlet:renderURL>

        <aui:button cssClass="selector-button" value="select-this-folder"
                    onClick="updateFolderIdInput(${folderId}, '${folderPathBreadCrumb}')" />
        <c:if test="<%= folderId != JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID %>">
            <aui:button href="<%= (goBackURL != null) ? goBackURL.toString() : StringPool.BLANK %>" value="go-to-previous-folder"/>
        </c:if>
    </aui:button-row>

    <liferay-ui:search-container
            iteratorURL="<%= portletURL %>"
            total="<%= JournalFolderLocalServiceUtil.getFoldersCount(scopeGroupId, folderId) %>"
            emptyResultsMessage="no-web-content-folder-found"
    >
        <liferay-ui:search-container-results
                results="<%= JournalFolderLocalServiceUtil.getFolders(scopeGroupId, folderId, searchContainer.getStart(),
        searchContainer.getEnd()) %>"
        />

        <liferay-ui:search-container-row
                className="com.liferay.journal.model.JournalFolder"
                keyProperty="folderId"
                modelVar="curFolder"
        >
            <liferay-portlet:renderURL varImpl="rowURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
                <portlet:param name="mvcPath" value="/select_folder.jsp" />
                <portlet:param name="folderId" value="<%= String.valueOf(curFolder.getFolderId()) %>" />
            </liferay-portlet:renderURL>

            <%
                AssetRendererFactory<?> assetRendererFactory =
                        AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(JournalFolder.class.getName());

                AssetRenderer<?> assetRenderer = assetRendererFactory.getAssetRenderer(curFolder.getFolderId());

                int fileEntriesCount = JournalArticleLocalServiceUtil.getArticlesCount(scopeGroupId,
                        curFolder.getFolderId(), WorkflowConstants.STATUS_APPROVED);
                int foldersCount = JournalFolderLocalServiceUtil.getFoldersCount(scopeGroupId, curFolder.getFolderId());

                if (foldersCount == 0) {
                    rowURL = null;
                }

                String curFolderPathBreadCrumb = JournalArticleUtil.getJournalFolderPathBreadcrumb(curFolder.getFolderId());
                request.setAttribute("curFolderPathBreadCrumb", curFolderPathBreadCrumb);
            %>

            <liferay-ui:search-container-column-text
                    cssClass="table-cell-expand table-cell-minw-200 table-title"
                    name="folder"
            >
                <liferay-ui:icon
                        icon="<%= assetRenderer.getIconCssClass() %>"
                        label="<%= true %>"
                        localizeMessage="<%= false %>"
                        markupView="lexicon"
                        message="<%= HtmlUtil.escape(curFolder.getName()) %>"
                        url="<%= (rowURL != null) ? rowURL.toString() : StringPool.BLANK %>"
                />
            </liferay-ui:search-container-column-text>

            <liferay-ui:search-container-column-text
                    cssClass="table-cell-expand-smallest table-column-text-end"
                    name="folders"
                    value="<%= String.valueOf(foldersCount) %>"
            />

            <liferay-ui:search-container-column-text
                    cssClass="table-cell-expand-smallest table-column-text-end"
                    name="entries"
                    value="<%= String.valueOf(fileEntriesCount) %>"
            />

            <liferay-ui:search-container-column-text>
                <aui:button cssClass="selector-button" value="select"
                            onClick="updateFolderIdInput(${curFolder.getFolderId()}, '${curFolderPathBreadCrumb}')"/>
            </liferay-ui:search-container-column-text>
        </liferay-ui:search-container-row>

        <liferay-ui:search-iterator
                markupView="lexicon"
        />
    </liferay-ui:search-container>
</div>

<script>
    const closePopUp = () => {
        const closeContentButton = $('.close', parent.window.document);

        closeContentButton.click();
        closeContentButton.click()
    };

    const updateFolderIdInput = (folderId, folderPath) => {
        const folderIdInput = $('#<portlet:namespace/>output-folder-id', parent.window.document)[0];
        const folderPathInput = $('#<portlet:namespace/>output-folder-path', parent.window.document)[0];

        folderIdInput.value = folderId;
        folderPathInput.value = folderPath;

        closePopUp()
    };
</script>