package keycloak.mongodb.users.federation.factories;


import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.UserStorageProviderFactory;

import keycloak.mongodb.users.federation.providers.CustomUserStorageProvider;

public class CustomUserStorageProviderFactory implements UserStorageProviderFactory<CustomUserStorageProvider> {

    private final String providerName = "mongodb_user_storage_provider";

    @Override
    public CustomUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        return new CustomUserStorageProvider(session, model);
    }

    @Override
    public String getId() {
        return providerName;
    }

    @Override
    public String getHelpText() {
        return UserStorageProviderFactory.super.getHelpText();
    }

    @Override
    public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config)
            throws ComponentValidationException {
        UserStorageProviderFactory.super.validateConfiguration(session, realm, config);
    }
}
