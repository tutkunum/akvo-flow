/*
 *  Copyright (C) 2016 Stichting Akvo (Akvo Foundation)
 *
 *  This file is part of Akvo FLOW.
 *
 *  Akvo FLOW is free software: you can redistribute it and modify it under the terms of
 *  the GNU Affero General Public License (AGPL) as published by the Free Software Foundation,
 *  either version 3 of the License or any later version.
 *
 *  Akvo FLOW is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Affero General Public License included below for more details.
 *
 *  The full license text can also be seen at <http://www.gnu.org/licenses/agpl.html>.
 */

package org.waterforpeople.mapping.app.web.rest;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.waterforpeople.mapping.app.web.dto.ApprovalStepDTO;

import com.gallatinsystems.survey.dao.ApprovalStepDAO;
import com.gallatinsystems.survey.domain.ApprovalStep;

@Controller
@RequestMapping("/approval_step")
public class ApprovalStepRestService {

    @Inject
    private ApprovalStepDAO approvalStepDao;

    /**
     * Create a new ApprovalStep from posted payload.
     *
     * @param requestPayload
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "")
    @ResponseBody
    public Map<String, ApprovalStepDTO> createApprovalStep(
            @RequestBody ApprovalStepDTO approvalStepPayload) {
        final Map<String, ApprovalStepDTO> response = new HashMap<String, ApprovalStepDTO>();
        final ApprovalStep step = approvalStepPayload.getApprovalStep();

        if (step.getTitle() == null || step.getTitle().trim().isEmpty()) {
            return null;
        }

        response.put("approval_step", new ApprovalStepDTO(approvalStepDao.save(step)));
        return response;
    }

}
