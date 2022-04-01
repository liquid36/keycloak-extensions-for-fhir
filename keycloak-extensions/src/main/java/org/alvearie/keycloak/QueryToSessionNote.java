/*
(C) Copyright IBM Corp. 2021

SPDX-License-Identifier: Apache-2.0
*/
package org.alvearie.keycloak;

import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

/**
 * Validate an incoming "aud" query parameter against a configured list of
 * acceptable audiences.
 */
public class QueryToSessionNote implements Authenticator {

    private static final Logger LOG = Logger.getLogger(QueryToSessionNote.class);

    public QueryToSessionNote(KeycloakSession session) {
        // NOOP
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {

        if (context.getAuthenticatorConfig() == null ||
                !context.getAuthenticatorConfig().getConfig()
                        .containsKey(QueryToSessionNoteFactory.QUERY_PROP_NAME)
                ||
                !context.getAuthenticatorConfig().getConfig()
                        .containsKey(QueryToSessionNoteFactory.SESSION_PROP_NAME)) {
            String msg = "The Keycloak Query to Session Note Extension must be configured with one or more allowed audiences";
            context.failure(AuthenticationFlowError.CLIENT_CREDENTIALS_SETUP_REQUIRED,
                    Response.status(302)
                            .header("Location", context.getAuthenticationSession().getRedirectUri() +
                                    "?error=server_error" +
                                    "&error_description=" + msg)
                            .build());
            return; // early exit
        }

        String queryParamName = context.getAuthenticatorConfig().getConfig()
                .get(QueryToSessionNoteFactory.QUERY_PROP_NAME);
        String sessionParamName = context.getAuthenticatorConfig().getConfig()
                .get(QueryToSessionNoteFactory.SESSION_PROP_NAME);

        String prefix = context.getAuthenticatorConfig().getConfig()
                .get(QueryToSessionNoteFactory.PREFIX_PROP_NAME);

        String queryParamsValue = context.getUriInfo().getQueryParameters()
                .getFirst(queryParamName);

        LOG.info(queryParamsValue);

        if (prefix != null) {
            context.getAuthenticationSession().setUserSessionNote(sessionParamName,
                    prefix + queryParamsValue);
        } else {
            context.getAuthenticationSession().setUserSessionNote(sessionParamName,
                    queryParamsValue);
        }
        context.success();
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        // NOOP
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        // NOOP
    }

    @Override
    public void close() {
        // NOOP
    }
}
