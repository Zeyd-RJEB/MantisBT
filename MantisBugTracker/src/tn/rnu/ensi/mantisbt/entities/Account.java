package tn.rnu.ensi.mantisbt.entities;

public class Account {
User user;
String os;
String description;
String plateform;
public String getOs() {
	return os;
}
public void setOs(String os) {
	this.os = os;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
}
public String getPlateform() {
	return plateform;
}
public void setPlateform(String plateform) {
	this.plateform = plateform;
}

}
