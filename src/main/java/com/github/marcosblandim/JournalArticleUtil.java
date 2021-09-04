package com.github.marcosblandim;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.journal.constants.JournalFolderConstants;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.service.JournalFolderLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.ServiceContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


// TODO: stop using static localServiceUtils
public class JournalArticleUtil {
    static public List<DDMStructure> fetchJournalArticleStructuresOfGroup(ServiceContext serviceContext,
                                                                          ClassNameLocalService _classNameLocalService,
                                                                          DDMStructureLocalService _ddmStructureLocalService) {
        long scopeGroupId = serviceContext.getScopeGroupId();
        long companyId = serviceContext.getCompanyId();

        long journalArticleClassNameId = _classNameLocalService.getClassNameId(JournalArticle.class);

        List<DDMStructure> journalArticleStructuresOfCompany = _ddmStructureLocalService
                .getClassStructures(companyId, journalArticleClassNameId);

        return journalArticleStructuresOfCompany.stream()
                .filter(journalArticleStructure -> journalArticleStructure.getGroupId() == scopeGroupId)
                .collect(Collectors.toList());
    }

    public static String removeUTF8BOM(String s) {
        final String UTF8_BOM = "\uFEFF";

        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }

    public static String getJournalFolderPathBreadcrumb(long journalFolderId) throws PortalException {
        ArrayList<String> journalFoldersNameList = new ArrayList<>();

        while (journalFolderId != JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
            JournalFolder journalFolder = JournalFolderLocalServiceUtil.getJournalFolder(journalFolderId);
            journalFoldersNameList.add(journalFolder.getName());

            journalFolderId = journalFolder.getParentFolderId();
        }

        journalFoldersNameList.add("Home");
        Collections.reverse(journalFoldersNameList);

        return String.join(" > ", journalFoldersNameList);
    }
}
