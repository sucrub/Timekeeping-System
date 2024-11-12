package com.example.demo.userprofile;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.util.Objects;

public class ProfileView {

    @FXML
    private Label nameLabel;
    @FXML private Label departmentLabel;
    @FXML private Label roleLabel;
    @FXML private Label birthdayLabel;
    @FXML private Label addressLabel;
    @FXML private Label emailLabel;

    ProfileController profileController;
    public ProfileView() {

    }

    public void setController(ProfileController profileController) {
        this.profileController = profileController;
    }

    @FXML
    public void initialize() throws SQLException {
        nameLabel.setText(profileController.getUserData().getName());
        departmentLabel.setText(profileController.getDepartmentName());
        switch(profileController.getUserData().getRoleId()) {
            case 1:
                roleLabel.setText("Quản lý nhân sự");
                break;
            case 2:
                roleLabel.setText("Trưởng đơn vị");
                break;
            case 3:
                if(Objects.equals(profileController.getDepartmentName(), "Đơn vị công nhân")) {
                    roleLabel.setText("Công nhân");
                }
                else {
                    roleLabel.setText("Nhân viên văn phòng");
                }
                break;
            default:
                roleLabel.setText("");
        }
        birthdayLabel.setText(profileController.getUserData().getBirthday().toString());
        addressLabel.setText(profileController.getUserData().getAddress());
        emailLabel.setText(profileController.getUserData().getEmail());
    }
}
