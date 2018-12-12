/*
 *  Copyright (C) 2018 Stichting Akvo (Akvo Foundation)
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

package org.waterforpeople.mapping.app.web.rest.security;

import com.gallatinsystems.user.dao.UserDao;
import com.gallatinsystems.user.domain.User;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeycloakFlowAuthenticationProvider extends KeycloakAuthenticationProvider {
    private static final Logger log = Logger.getLogger(KeycloakFlowAuthenticationProvider.class.getName());

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;

        UserDao userDao = new UserDao();
        String email = token.getAccount().getKeycloakSecurityContext().getIdToken().getEmail();
        User user = userDao.findUserByEmail(email);

        if (user == null) {
            log.log(Level.WARNING, "User entity with email <" + email + "> not found in Datastore");
            return null;
        }

        int authority = getAuthorityLevel(user);
        Set<AppRole> roles = EnumSet.noneOf(AppRole.class);

        if (authority == AppRole.NEW_USER.getLevel()) {
            roles.add(AppRole.NEW_USER);
        } else {
            for (AppRole r : AppRole.values()) {
                if (authority <= r.getLevel()) {
                    roles.add(r);
                }
            }
        }

        return new KeycloakFlowAuthenticationToken(token.getAccount(), token.isInteractive(), roles, user.getKey().getId());
    }

    private int getAuthorityLevel(User user) {

        if (user.isSuperAdmin()) {
            return AppRole.SUPER_ADMIN.getLevel();
        }

        try {
            final int level = Integer.parseInt(user.getPermissionList());
            return level;
        } catch (Exception e) {
            log.log(Level.WARNING, "Error getting role level, setting USER role", e);
        }

        return AppRole.USER.getLevel();
    }
}
