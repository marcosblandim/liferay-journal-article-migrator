package com.github.marcosblandim.util;

import com.liferay.journal.constants.JournalFolderConstants;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.service.JournalFolderLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.ArrayList;
import java.util.Collections;


// TODO: stop using static localServiceUtils
public class JournalArticleUtil {
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
