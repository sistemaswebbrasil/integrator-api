package br.com.siswbrasil.integrator.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;
import com.auth0.net.Request;

@Service
public class Auth0UserService {

    private final ManagementAPI managementAPI;

    @Autowired
    public Auth0UserService(ManagementAPI managementAPI) {
        this.managementAPI = managementAPI;
    }

    public List<User> getAllUsers(int page, int perPage) throws Auth0Exception {
        UserFilter filter = new UserFilter()
            .withPage(page, perPage);
        
        Request<UsersPage> request = managementAPI.users().list(filter);
        // Fix: First get the UsersPage from the Response object
        UsersPage usersPage = request.execute().getBody();
        return usersPage.getItems();
    }
    
    public User getUserById(String userId) throws Auth0Exception {
        Request<User> request = managementAPI.users().get(userId, null);
        // Fix: Get the User from the Response object
        return request.execute().getBody();
    }
    
    public User createUser(String email, String password, String connection) throws Auth0Exception {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setConnection(connection);
        
        Request<User> request = managementAPI.users().create(user);
        // Fix: Get the User from the Response object
        return request.execute().getBody();
    }
    
    public void updateUser(String userId, Map<String, Object> userData) throws Auth0Exception {
        User user = new User();
        
        // Check for specific known User properties first
        if (userData.containsKey("email")) {
            user.setEmail((String) userData.get("email"));
        }
        if (userData.containsKey("name")) {
            user.setName((String) userData.get("name"));
        }
        if (userData.containsKey("nickname")) {
            user.setNickname((String) userData.get("nickname"));
        }
        if (userData.containsKey("picture")) {
            user.setPicture((String) userData.get("picture"));
        }
        
        // Handle user_metadata if present
        if (userData.containsKey("user_metadata")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> metadata = (Map<String, Object>) userData.get("user_metadata");
            user.setUserMetadata(metadata);
        }
        
        // Handle app_metadata if present
        if (userData.containsKey("app_metadata")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> metadata = (Map<String, Object>) userData.get("app_metadata");
            user.setAppMetadata(metadata);
        }
        
        Request<User> request = managementAPI.users().update(userId, user);
        request.execute();
    }
    
    public void deleteUser(String userId) throws Auth0Exception {
        Request request = managementAPI.users().delete(userId);
        request.execute();
    }
}