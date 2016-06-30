package com.mmnaseri.utils.samples.spring.data.jpa.service;

import com.mmnaseri.utils.samples.spring.data.jpa.model.User;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/29/16, 4:06 PM)
 */
public interface UserService {

    User createUser(String username, String email, String password);

    void updatePassword(String handle, String oldPassword, String newPassword);

    void deleteUser(String handle);

    User lookup(String handle);

    User authenticate(String handle, String password);

}
