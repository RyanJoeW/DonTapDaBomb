package com.ryan.dontapdabomb.dontapdabomb.controller;

import com.ryan.dontapdabomb.dontapdabomb.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ryan.dontapdabomb.dontapdabomb.entity.User;

import java.util.List;

@CrossOrigin(maxAge = 3360)
@RestController
public class UserController {


    @Autowired
    private IUserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User>  getUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.addUser(user));
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable("userId") Long userId) {
        User dtdbObj = userService.getUserById(userId);
        if (dtdbObj != null) {
            dtdbObj.setName(user.getName());
            dtdbObj.setPassword(user.getPassword());
            dtdbObj.setCash(user.getCash());
        }
        return ResponseEntity.ok(userService.updateUser(dtdbObj));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable("userId") Long userId) {

        User dtdbObj=userService.getUserById(userId);
        String deleteMsg=null;
        if(dtdbObj!=null) {
            deleteMsg=userService.deleteUser(dtdbObj);
        }
        return ResponseEntity.ok(deleteMsg);
    }


}
