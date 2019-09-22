package com.example.frizty.Model;

public class model {

    String name,email,comment,id;

    public model(){

    }

    public model(String id,String name, String email, String comment) {
        this.name = name;
        this.email = email;
        this.comment = comment;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getComment() {
        return comment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
