/**
 *  Copyright (C) 2019 University of York, UK.
 *
 *  This project was initiated through a donation of source code by the
 *  University of York, UK. It contains free software; you can redistribute
 *  it and/or modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation; either version 2 of the
 *  License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *  For more information please contact:
 *
 *  Web Services Group
 *  IT Service
 *  University of York
 *  YO10 5DD
 *  United Kingdom
 */

package uk.ac.york.its.vle.b2.admin.tools.cs.xidlocator.service.impl;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xythos.common.api.XythosException;
import com.xythos.storageServer.api.FileSystemEntry;

import uk.ac.york.its.vle.b2.admin.tools.cs.xidlocator.service.ContentSystemServiceManager;

import blackboard.cms.filesystem.CSContext;
import blackboard.cms.filesystem.CSEntry;
import blackboard.cms.filesystem.CSFile;
import blackboard.cms.filesystem.security.UserPrincipal;
import blackboard.data.user.User;
import blackboard.persist.PersistenceException;
import blackboard.platform.contentsystem.data.Resource;
import blackboard.platform.contentsystem.manager.DocumentManager;
import blackboard.platform.contentsystem.service.ContentSystemService;
import blackboard.platform.contentsystem.service.ContentSystemServiceFactory;

/**
 * @author Kelvin Hai {@link <a
 *         href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */

@Service
public class ContentSystemServiceManagerImpl implements
		ContentSystemServiceManager {
	private static final Logger logger = LoggerFactory.getLogger(ContentSystemServiceManagerImpl.class);

	private static final String CONTENT_SYSTEM_BASE_LOCATION = ContentSystemService.WEBDAV_SERVLET_PATH+"/";

	// @Autowired not working 
	private DocumentManager documentManager = null;

	// @Autowired not working 
	private blackboard.platform.contentsystem.manager.SecurityManager cmsSecurityManager = null;

	public ContentSystemServiceManagerImpl(){
		ContentSystemService contentSystemService = ContentSystemServiceFactory.getInstance();
		documentManager = contentSystemService.getDocumentManager();
		cmsSecurityManager = contentSystemService.getSecurityManager();
	}
	
	// Get a content system resource based on an entered xid
	public Resource getResource(String xid) {
		if (StringUtils.isNotEmpty(xid)) {
			xid = "xid-" + xid.toLowerCase().replaceAll("xid-", "");
			String resourceLocation = CONTENT_SYSTEM_BASE_LOCATION + xid;
			try {
				return documentManager.loadResource(resourceLocation);
			} catch (PersistenceException e) {
				logger.error(e.getMessage());
			}
		}
		return null;
	}

	// Check if user has permissions to access the resource
	public boolean isAuthorised(User user, Resource resource) {
		if (user != null && resource != null) {
			try {
				String principalId = UserPrincipal.calculatePrincipalID(user);
				String locationString = resource.getLocation();

				return cmsSecurityManager
						.principalIdHasPermissions(
								principalId,
								locationString,
								blackboard.platform.contentsystem.manager.SecurityManager.PermissionType.Readable);

			} catch (PersistenceException e) {
				logger.error(e.getMessage());
			}
		}
		return false;
	}
	
	
	@Override
	public com.xythos.fileSystem.Directory getXythosFileSystemDirectory(String csEntryPath) {
		return (com.xythos.fileSystem.Directory)getXythosFileSystemEntry(csEntryPath);
	}

	@Override
	public com.xythos.fileSystem.File getXythosFileSystemFile(String csEntryPath) {
		return (com.xythos.fileSystem.File)getXythosFileSystemEntry(csEntryPath);
	}

	// [WARN]: this is using the private api. xsscore.jar is required
	// [INFO]: Some methods are listed below 
	// XythosFileSystemFile.getFileVersionExternalStorageFileName() );
	// xythosFileSystemFile.getFileMimeType()
    // XythosFileSystemFile.getFileContentType()
	// csEntryPath = blackboard.platform.contentsystem.data.Resource.getLocation()
	private FileSystemEntry getXythosFileSystemEntry(String csEntryPath) {
		FileSystemEntry fileSystemEntry = null;
		if(StringUtils.isNotEmpty(csEntryPath)){
			CSContext csctx = null;
			try {
				csctx = CSContext.getContext();
				CSEntry ce = csctx.findEntry(csEntryPath);
				CSFile cf = (CSFile) ce;
				
				fileSystemEntry=cf.getFileSystemEntry();
			} catch (Exception e) {
				logger.error(e.getMessage());
			} finally {
				if (csctx != null) {
					try {
						csctx.commit();
					} catch (XythosException e) {
						logger.error(e.getMessage());
					}
				}
			}
		}
		return fileSystemEntry;
	}

}
