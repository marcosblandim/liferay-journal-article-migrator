package com.github.marcosblandim.portlet;

import com.github.marcosblandim.constants.JournalArticleMigratorPortletKeys;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import org.osgi.service.component.annotations.Component;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author marco
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.hidden",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"javax.portlet.display-name=JournalArticleMigrator",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + JournalArticleMigratorPortletKeys.JOURNALARTICLEMIGRATOR,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class JournalArticleMigratorPortlet extends MVCPortlet {
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		List<DDMStructure> journalArticleStructures = List.of();

		renderRequest.setAttribute("journalArticleStructures", journalArticleStructures);
		super.doView(renderRequest, renderResponse);
	}

	private static final Log _log = LogFactoryUtil.getLog(
			JournalArticleMigratorPortlet.class);
}