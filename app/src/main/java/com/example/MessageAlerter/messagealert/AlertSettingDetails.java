package com.example.MessageAlerter.messagealert;

public class AlertSettingDetails {

    public int id;
    public String matchingText;
    public String soundFileLocation;
    public int order;


    public AlertSettingDetails(int pId, String pMatchingText, String pSoundFileLocation, int pOrder){
        id = pId;
        matchingText = pMatchingText;
        soundFileLocation = pSoundFileLocation;
        order = pOrder;
    }
}