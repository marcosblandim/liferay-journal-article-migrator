package com.github.marcosblandim.portlet.action;

import com.github.marcosblandim.constants.JournalArticleMigratorPortletKeys;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.journal.service.JournalArticleServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

// TODO: check user permission for the structure and output folder

@Component(
    property = {
            "javax.portlet.name=" + JournalArticleMigratorPortletKeys.JOURNALARTICLEMIGRATOR,
            "mvc.command.name=/journal_article/import"
    },
    service = MVCActionCommand.class
)
public class ImportJournalArticlesMVCActionCommand extends BaseMVCActionCommand {
    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);

        File importFile = uploadPortletRequest.getFile("import-file");

        long structureId = ParamUtil.getLong(actionRequest, "structure-id");
        long outputFolderId = ParamUtil.getLong(actionRequest, "output-folder-id");
        boolean defaultFolder = ParamUtil.getBoolean(actionRequest, "default-folder");
        boolean resetFolder = ParamUtil.getBoolean(actionRequest, "reset-folder");

        List<String> invalidInputNames = getInvalidInputNames(importFile, structureId);
        if (!invalidInputNames.isEmpty()) {
            _log.warn("invalid form field");

            actionRequest.setAttribute("invalidInputName", invalidInputNames.get(0));
            SessionErrors.add(actionRequest, "invalid-form-input");

            return;
        }

        // output folder -> folderId
        long folderId = outputFolderId;

        // get content xml -> contentXML
        String emptyContent = getStructureContent(structureId);

        // iterate over json file, creating the journals
        JSONArray journalsJSONArray = JSONFactoryUtil.createJSONArray(FileUtil.read(importFile));
        for (int i = 0; i < journalsJSONArray.length(); i++) {
            JSONObject journalJSON = journalsJSONArray.getJSONObject(i);
            String journalContent = fillContent(journalJSON, emptyContent);


        }
    }

    private List<String> getInvalidInputNames(File importFile, long structureId) {
        List<String> invalidInputNames = new ArrayList<>();

        String importFileExtension = FileUtil.getExtension(importFile.getName());
        if (!importFile.exists() || !importFileExtension.equals("json")) {
            invalidInputNames.add("import-file");
        }
        if (Validator.isNull(structureId)) {
            invalidInputNames.add("structure-id");
        }

        return invalidInputNames;
    }

    private String getStructureContent(long structureId) throws PortalException {
        // TODO: implement nested and duplicated fields
        return "";

    }

    private String fillContent(JSONObject journalJSON, String emptyContent) {
        return "";
    }

    private static final Log _log = LogFactoryUtil.getLog(
            ImportJournalArticlesMVCActionCommand.class);

    @Reference
    private DDMStructureLocalService _ddmStructureLocalService;

    @Reference
    private JournalArticleLocalService _journalArticleLocalService;
}
