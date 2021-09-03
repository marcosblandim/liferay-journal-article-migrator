package com.github.marcosblandim.application.list;

import com.github.marcosblandim.constants.JournalArticleMigratorPortletKeys;
import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.portal.kernel.model.Portlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
    immediate = true,
    property = {
            "panel.app.order:Integer=101",
            "panel.category.key=" + PanelCategoryKeys.SITE_ADMINISTRATION_CONTENT
    },
    service = PanelApp.class
)
public class JournalArticleMigratorPanelApp  extends BasePanelApp {
    @Override
    public String getPortletId() {
        return JournalArticleMigratorPortletKeys.JOURNALARTICLEMIGRATOR;
    }

    @Override
    @Reference(
            target = "(javax.portlet.name=" + JournalArticleMigratorPortletKeys.JOURNALARTICLEMIGRATOR + ")",
            unbind = "-"
    )
    public void setPortlet(Portlet portlet) {
        super.setPortlet(portlet);
    }
}
