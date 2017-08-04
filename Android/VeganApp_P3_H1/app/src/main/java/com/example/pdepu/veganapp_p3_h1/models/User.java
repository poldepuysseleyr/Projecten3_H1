package com.example.pdepu.veganapp_p3_h1.models;

import java.util.Date;

/**
 * Created by pdepu on 1/08/2017.
 */

public class User {


    private String _id;
    private String username;
    private String name;
    private String surName;
    private int totalVeganScore;
    private User[] followingUsers;
    private Date dateOfCreation;
    private Challenge[] challenges;
    private boolean isAdmin;
    private String hash;
    private String salt;
    private String image;
    private String password;
    private Token token;

    public User(){

    }


    public User(String username, String name, String surName, Date dateOfCreation, boolean isAdmin, String password) {
        this.username = username;
        this.name = name;
        this.surName = surName;
        this.dateOfCreation = dateOfCreation;
        this.isAdmin = isAdmin;
        this.password = password;
    }

    public User(String _id,String username, String name, String surName, int totalVeganScore, User[] followingUsers, Date dateOfCreation, Challenge[] challenges, boolean isAdmin, String hash, String salt, String image) {
        this._id = _id;
        this.username = username;
        this.name = name;
        this.surName = surName;
        this.totalVeganScore = totalVeganScore;
        this.followingUsers = followingUsers;
        this.dateOfCreation = dateOfCreation;
        this.challenges = challenges;
        this.isAdmin = isAdmin;
        this.hash = hash;
        this.salt = salt;
        this.image = image;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public int getTotalVeganScore() {
        return totalVeganScore;
    }

    public void setTotalVeganScore(int totalVeganScore) {
        this.totalVeganScore = totalVeganScore;
    }

    public User[] getFollowingUsers() {
        return followingUsers;
    }

    public void setFollowingUsers(User[] followingUsers) {
        this.followingUsers = followingUsers;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Challenge[] getChallenges() {
        return challenges;
    }

    public void setChallenges(Challenge[] challenges) {
        this.challenges = challenges;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
