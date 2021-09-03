package com.github.marcosblandim.portlet;

import com.github.marcosblandim.constants.JournalArticleMigratorPortletKeys;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

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
}