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

package uk.ac.york.its.vle.b2.admin.tools.cs.xidlocator.web.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;

import uk.ac.york.its.vle.b2.admin.tools.cs.xidlocator.domain.XidCommand;

/**
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */

public class XidCommandValidator {
	public void validate(XidCommand form, Errors errors) {
		String xid = form.getXid();
		if (StringUtils.isEmpty(xid)) {
			errors.rejectValue("xid", "required", "required");
		}
	}
}
