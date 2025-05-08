public class Notification {
    private String recieverName;
    private String transmitterName;
    private String timeStamp;
    private String imageID;
    Notification(String recieverName, String transmitterName, String timeStamp)
    {
        this.recieverName = recieverName;
        this.transmitterName = transmitterName;
        this.timeStamp = timeStamp;
        this.imageID = null;
    }

    Notification(String recieverName, String transmitterName, String timeStamp, String imageID)
    {
        this.recieverName = recieverName;
        this.transmitterName = transmitterName;
        this.timeStamp = timeStamp;
        this.imageID = imageID;
    }

    public String getRecieverName() {
        return recieverName;
    }

    public String getImageID() {
        return imageID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getTransmitterName() {
        return transmitterName;
    }

    public void setRecieverName(String recieverName) {
        this.recieverName = recieverName;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setTransmitterName(String transmitterName) {
        this.transmitterName = transmitterName;
    }
}
