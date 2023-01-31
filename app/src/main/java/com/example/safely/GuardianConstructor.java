package com.example.safely;

public class GuardianConstructor {

    String guardianName, guardianPhone;


    public GuardianConstructor() {
    }

    public GuardianConstructor(String guardianName, String guardianPhone) {
        this.guardianName= guardianName;
        this.guardianPhone= guardianPhone;

    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianPhone() {
        return guardianPhone;
    }

    public void setGuardianPhone(String guardianPhone) {
        this.guardianPhone = guardianPhone;
    }
}
