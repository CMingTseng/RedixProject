package redix.booxtown.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 27/08/2016.
 */
public class InteractThread implements Serializable {

    private String interactThreadTitle;
    private String interactThreadCount;
    private String interactThreadAddBy;
    private String interactThreadContent;
    //private ArrayList<InteractComment> listInteractThreadDetails;
    private boolean status;

    public InteractThread() {
    }

    public InteractThread(String interactThreadTitle, String interactThreadCount, String interactThreadAddBy, String interactThreadContent, boolean status) {
        this.interactThreadTitle = interactThreadTitle;
        this.interactThreadCount = interactThreadCount;
        this.interactThreadAddBy = interactThreadAddBy;
        this.interactThreadContent = interactThreadContent;
        //this.listInteractThreadDetails = listInteractThreadDetails;
        this.status = status;
    }

    public String getInteractThreadContent() {
        return interactThreadContent;
    }

    public void setInteractThreadContent(String interactThreadContent) {
        this.interactThreadContent = interactThreadContent;
    }

//    public ArrayList<InteractComment> getListInteractThreadDetails() {
//        return listInteractThreadDetails;
//    }
//
//    public void setListInteractThreadDetails(ArrayList<InteractComment> listInteractThreadDetails) {
//        this.listInteractThreadDetails = listInteractThreadDetails;
//    }

    public String getInteractThreadTitle() {
        return interactThreadTitle;
    }

    public void setInteractThreadTitle(String interactThreadTitle) {
        this.interactThreadTitle = interactThreadTitle;
    }

    public String getInteractThreadCount() {
        return interactThreadCount;
    }

    public void setInteractThreadCount(String interactThreadCount) {
        this.interactThreadCount = interactThreadCount;
    }

    public String getInteractThreadAddBy() {
        return interactThreadAddBy;
    }

    public void setInteractThreadAddBy(String interactThreadAddBy) {
        this.interactThreadAddBy = interactThreadAddBy;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
