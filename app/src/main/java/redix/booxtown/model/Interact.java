package redix.booxtown.model;

import java.io.Serializable;

/**
 * Created by Administrator on 27/08/2016.
 */
public class Interact implements Serializable {

    private String interactTitle;
    private String interactCount;
    private String interactUpdatetime;
    private boolean status;
    public Interact(){

    }

    public Interact(String interactTitle, String interactCount, String interactUpdatetime,boolean status) {
        this.interactTitle = interactTitle;
        this.interactCount = interactCount;
        this.interactUpdatetime = interactUpdatetime;
        this.status=status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getInteractTitle() {
        return interactTitle;
    }

    public void setInteractTitle(String interactTitle) {
        this.interactTitle = interactTitle;
    }

    public String getInteractCount() {
        return interactCount;
    }

    public void setInteractCount(String interactCount) {
        this.interactCount = interactCount;
    }

    public String getInteractUpdatetime() {
        return interactUpdatetime;
    }

    public void setInteractUpdatetime(String interactUpdatetime) {
        this.interactUpdatetime = interactUpdatetime;
    }
}
