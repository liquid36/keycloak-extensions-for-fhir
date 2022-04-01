/*
(C) Copyright IBM Corp. 2021

SPDX-License-Identifier: Apache-2.0
*/
package org.alvearie.keycloak;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating AudienceValidator instances.
 */
public class QueryToSessionNoteFactory implements AuthenticatorFactory {

    private static final String PROVIDER_ID = "query-session-note";

    static final String QUERY_PROP_NAME = "query-param-name";
    private static final String QUERY_PROP_LABEL = "Query Params";
    private static final String QUERY_PROP_DESCRIPTION = "Name of the query params";

    static final String SESSION_PROP_NAME = "session-note-name";
    private static final String SESSION_PROP_LABEL = "Session Note Name";
    private static final String SESSION_PROP_DESCRIPTION = "Name of session note params";

    static final String PREFIX_PROP_NAME = "prefix-name";
    private static final String PREFIX_PROP_LABEL = "Prefix";
    private static final String PREFIX_PROP_DESCRIPTION = "Prefix to be added to value";

    @Override
    public String getDisplayType() {
        return "Query to Session Note";
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    public static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.DISABLED
    };

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Map a query params to a session note, to later inject into acess token";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        List<ProviderConfigProperty> list = new ArrayList<ProviderConfigProperty>();
        list.add(new ProviderConfigProperty(QUERY_PROP_NAME, QUERY_PROP_LABEL, QUERY_PROP_DESCRIPTION,
                ProviderConfigProperty.STRING_TYPE, null));
        list.add(new ProviderConfigProperty(SESSION_PROP_NAME, SESSION_PROP_LABEL, SESSION_PROP_DESCRIPTION,
                ProviderConfigProperty.STRING_TYPE, null));
        list.add(new ProviderConfigProperty(PREFIX_PROP_NAME, PREFIX_PROP_LABEL, PREFIX_PROP_DESCRIPTION,
                ProviderConfigProperty.STRING_TYPE, null));
        return list;
    }

    @Override
    public void close() {
        // NOOP
    }

    @Override
    public Authenticator create(KeycloakSession session) {
        return new QueryToSessionNote(session);
    }

    @Override
    public void init(Config.Scope config) {
        // NOOP
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // NOOP
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
