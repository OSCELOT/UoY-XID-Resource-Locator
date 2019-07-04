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

package uk.ac.york.its.vle.b2.admin.tools.cs.xidlocator.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import uk.ac.york.its.vle.b2.admin.tools.cs.xidlocator.domain.XidCommand;
import uk.ac.york.its.vle.b2.admin.tools.cs.xidlocator.service.ContentSystemServiceManager;
import uk.ac.york.its.vle.b2.admin.tools.cs.xidlocator.web.validator.XidCommandValidator;

import blackboard.data.user.User;
import blackboard.platform.contentsystem.data.Resource;
import blackboard.platform.security.SecurityUtil;
import blackboard.platform.spring.beans.annotations.ContextValue;

/**
 * @author Kelvin Hai {@link <a
 *         href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */

@Controller
@SessionAttributes("form")
@RequestMapping("/admin/tools/cs/xid_locator/{type}")
public class XidLocatorController {
	private static final Logger logger = LoggerFactory
			.getLogger(XidLocatorController.class);

	@Autowired
	private ContentSystemServiceManager contentSystemServiceManager;

	// @ContextValue User user seems not working on 9.1 SP8
	@RequestMapping(method = RequestMethod.GET)
	public String setup(
			@ContextValue User user,
			@PathVariable("type") String type,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "course_id", required = false) String courseId,
			Model model) {

		// show error page if user is not authorised to access the tool
		if (!isAuthorised()) {
			logger.error("Get Mapping! Not Authorised.");
			return "error";
		}

		// Tell the jsp page which type of tool it is
		setType(model,type);

		// Add Command object for spring <form:form/>
		model.addAttribute("form", new XidCommand());
		
		if (StringUtils.isNotEmpty(id)) {
			Resource resource = contentSystemServiceManager.getResource(id.trim());

			// Use contentSystemServiceManager.isAuthorised(user, resource) only post 9.1 SP8
			if (resource != null && contentSystemServiceManager.isAuthorised(user, resource)) {
				model.addAttribute("resource", resource);
				if (resource.getType().equals(Resource.Type.FILE)) {
					model.addAttribute("xythosFile",
							contentSystemServiceManager
									.getXythosFileSystemFile(resource
											.getLocation()));
				} else if (resource.getType().equals(Resource.Type.FOLDER)) {
					model.addAttribute("xythosDirectory",
							contentSystemServiceManager
									.getXythosFileSystemDirectory(resource
											.getLocation()));
				}
			} else {
				model.addAttribute("resourceNotFound", true);
			}
		}

		return "xid_locator";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String process(
			Model model,
			@PathVariable("type") String type,
			@RequestParam(value = "course_id", required = false) String courseId,
			@ModelAttribute("form") XidCommand form, BindingResult result,
			SessionStatus status) {
		logger.info("Post Mapping!");

		// show error page if use is not authorised to access the tool
		if (!isAuthorised()) {
			return "error";
		}

		new XidCommandValidator().validate(form, result);
		if (result.hasErrors()) {
			setType(model,type);
			return "xid_locator";
		} else {
			status.setComplete();
			// e.g: /admin/tools/cs/xid_locator/system?id=xid-123456_1
			String path = "/admin/tools/cs/xid_locator/" + type + "?id="+ form.getXid();

			if (StringUtils.isNotEmpty(courseId)) {
				path += "&course_id=" + courseId;
			}
			
			return "redirect:" + path;
		}
	}

	private void setType(Model model, String type) {
		if (StringUtils.isNotEmpty(type)) {
			if (type.equals("course")) {
				model.addAttribute("courseTool", true);
			} else if (type.equals("system")) {
				model.addAttribute("systemTool", true);
			}
		}
	}

	private boolean isAuthorised() {
		String[] entitlements = { "course.control_panel.VIEW",
				"system.plugin.MODIFY" };
		return SecurityUtil.userHasAnyEntitlements(entitlements);
	}

}
