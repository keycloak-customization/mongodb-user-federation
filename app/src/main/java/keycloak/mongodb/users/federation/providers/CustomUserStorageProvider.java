package keycloak.mongodb.users.federation.providers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


public class CustomUserStorageProvider implements UserStorageProvider, CredentialInputValidator, UserLookupProvider {
    private final KeycloakSession session;
    private final ComponentModel model;
    protected Map<String, UserModel> loadedUsers = new HashMap<>();
    private final List<User> users = DBUtil.getAllUsers();

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
            String password = users.stream().filter(mappedUser -> mappedUser.getUsername().equals(user.getUsername())).findFirst().get().getPassword();
            return credentialType.equals(PasswordCredentialModel.TYPE) && password != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
        if (!supportsCredentialType(credentialInput.getType())) return false;
        try {
            // here we can add a database treatment instead of a static hashmap
            String password = users.stream().filter(mappedUser -> mappedUser.getUsername().equals(user.getUsername())).findFirst().get().getPassword();
            if (password == null) return false;
            return password.equals(credentialInput.getChallengeResponse());
        } catch (Exception e) {
            e.printStackTrace();
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
            User user = checkUserByUsername(username,users);
            if (user != null) {
                adapter = createAdapter(realm, username);
                loadedUsers.put(username, adapter);
            }
        }
        return adapter;
    }

    public static User checkUserByUsername(String username, List<User> users) {
        if (users.stream().anyMatch(obj -> obj.getUsername().equals(username)))
            return users.stream().filter(obj -> obj.getUsername().equals(username)).findFirst().get();
        return null;
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
