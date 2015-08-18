package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

	public final StringProperty id, login, password, fullname, group_id, group_name;
	
	public User() {
		this(null, null, null, null, null, null);
	}

	public User(String id, String login, String password, String fullname, String group_id, String group_name) {
		this.id = new SimpleStringProperty(id);
		this.login = new SimpleStringProperty(login);
		this.password = new SimpleStringProperty(password);
		this.fullname = new SimpleStringProperty(fullname);
		this.group_id = new SimpleStringProperty(group_id);
		this.group_name = new SimpleStringProperty(group_name);
	}

	public String getId() {
		return id.get();
	}

	public StringProperty idProperty() {
		return id;
	}

	public void setId(String id) {
		this.id.set(id);
	}

	public String getLogin() {
		return login.get();
	}

	public StringProperty loginProperty() {
		return login;
	}

	public void setLogin(String login) {
		this.login.set(login);
	}

	public String getPassword() {
		return password.get();
	}

	public StringProperty passwordProperty() {
		return password;
	}

	public void setPassword(String password) {
		this.password.set(password);
	}

	public String getFullname() {
		return fullname.get();
	}

	public StringProperty fullnameProperty() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname.set(fullname);
	}

	public String getGroup_id() {
		return group_id.get();
	}

	public StringProperty group_idProperty() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id.set(group_id);
	}

	public String getGroup_name() {
		return group_name.get();
	}

	public StringProperty group_nameProperty() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name.set(group_name);
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", login=" + login +
				", password=" + password +
				", fullname=" + fullname +
				", group_id=" + group_id +
				", group_name=" + group_name +
				'}';
	}
}
