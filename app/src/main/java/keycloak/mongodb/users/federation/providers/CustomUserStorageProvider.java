package keycloak.mongodb.users.federation.providers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import keycloak.mongodb.users.federation.utils.HashUtil;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.keycloak.storage.user.UserLookupProvider;

import keycloak.mongodb.users.federation.dto.User;
import keycloak.mongodb.users.federation.utils.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CustomUserStorageProvider implements UserStorageProvider, CredentialInputValidator, UserLookupProvider {
    private final KeycloakSession session;
    private final ComponentModel model;
    protected Map<String, UserModel> loadedUsers = new HashMap<>();
    private final List<User> users = DBUtil.getAllUsers();
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserStorageProvider.class);

    public CustomUserStorageProvider(KeycloakSession session, ComponentModel model) {
        this.session = session;
        this.model = model;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        return credentialType.equals(PasswordCredentialModel.TYPE);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        try {
            Optional<User> storedUser = users.stream().filter(
                    mappedUser -> mappedUser.getUsername().equals(user.getUsername())
            ).findFirst();
            return storedUser.isPresent() && credentialType.equals(PasswordCredentialModel.TYPE) && storedUser.get().getPassword() != null;
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return false;
        }
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
        if (!supportsCredentialType(credentialInput.getType())) return false;
        try {
            Optional<User> storedUser = users.stream().filter(
                    mappedUser -> mappedUser.getUsername().equals(user.getUsername())
            ).findFirst();
            if (storedUser.isEmpty() || storedUser.get().getPassword() == null) return false;
            return storedUser.get().getPassword().equals(
                    HashUtil.hashData(credentialInput.getChallengeResponse())
            );
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return false;
        }
    }

    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        StorageId storageId = new StorageId(id);
        String username = storageId.getExternalId();
        return getUserByUsername(username, realm);
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        UserModel adapter = loadedUsers.get(username);
        if (adapter == null) {
            if (checkUserByUsername(username, users).isPresent()) {
                adapter = createAdapter(realm, username);
                loadedUsers.put(username, adapter);
            }
        }
        return adapter;
    }

    public static Optional<User> checkUserByUsername(String username, List<User> users) {
        if (users.stream().anyMatch(obj -> obj.getUsername().equals(username)))
            return users.stream().filter(obj -> obj.getUsername().equals(username)).findFirst();
        return Optional.empty();
    }

    private UserModel createAdapter(RealmModel realm, String username) {
        return new AbstractUserAdapter(this.session, realm, this.model) {
            @Override
            public String getUsername() {
                return username;
            }
        };
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
        return null;
    }
}
