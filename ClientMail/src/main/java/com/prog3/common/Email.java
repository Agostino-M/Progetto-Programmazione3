package com.prog3.common;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Email implements Serializable {
    private Integer id;
    private String sender;
    private ArrayList<String> receivers;
    private String subject;
    private String message;
    private Date date;

    public Email() {
        this.sender = "";
        this.receivers = new ArrayList<>();
        this.subject = "";
        this.message = "";
        this.date = new Date();
    }

    public Email(String sender, ArrayList<String> receivers, String subject, String message, Date date) {
        this.sender = sender;
        this.receivers = receivers;
        this.subject = subject;
        this.message = message;
        this.date = date;
    }


    public Integer getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public ArrayList<String> getReceivers() {
        return receivers;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceivers(ArrayList<String> receivers) {
        this.receivers = receivers;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy, HH:mm");

        return id +
                ":" + sender +
                ":" + subject +
                ":" + receivers +
                ":" + message +
                ":" + format.format(date);
    }
}
/**
 * Versione non funzionante con property
 * <p>
 * impossibile serializzare Property e ObservableList!
 */
//public class Email implements Serializable {
//    private static int count = 1;
//
//    private SimpleIntegerProperty id = new SimpleIntegerProperty();
//    private SimpleStringProperty sender = new SimpleStringProperty();
//    private SimpleListProperty<String> receivers = new SimpleListProperty();
//    private SimpleStringProperty subject = new SimpleStringProperty();
//    private SimpleStringProperty message = new SimpleStringProperty();
//    private SimpleStringProperty date = new SimpleStringProperty();
//
//
//    public Email() {
//        setId(count++);
//        setSender("");
//        setMessage("");
//        setSubject("");
//        setDate("");
//
//    }
//
//    public Email(String sender, ObservableList<String> receivers, String subject, String message, String date) {
//        setId(count++);
//        setSender(sender);
//        setMessage(message);
//        setSubject(subject);
//        setDate(date);
//        setReceivers(receivers);
//    }
//
//    public final int getId() {
//        return id.get();
//    }
//
//    public SimpleIntegerProperty idProperty() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id.set(id);
//    }
//
//    public final String getSender() {
//        return sender.get();
//    }
//
//    public SimpleStringProperty senderProperty() {
//        return sender;
//    }
//
//    public void setSender(String sender) {
//        this.sender.set(sender);
//    }
//
//    public final ObservableList<String> getReceivers() {
//        return receivers.get();
//    }
//
//    public SimpleListProperty<String> receiversProperty() {
//        return receivers;
//    }
//
//    public void setReceivers(ObservableList<String> receivers) {
//        this.receivers.set(receivers);
//    }
//
//    public final String getSubject() {
//        return subject.get();
//    }
//
//    public SimpleStringProperty subjectProperty() {
//        return subject;
//    }
//
//    public void setSubject(String subject) {
//        this.subject.set(subject);
//    }
//
//    public final String getMessage() {
//        return message.get();
//    }
//
//    public SimpleStringProperty messageProperty() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message.set(message);
//    }
//
//    public final String getDate() {
//        return date.get();
//    }
//
//    public SimpleStringProperty dateProperty() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date.set(date);
//    }
//
//    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
//        out.writeInt(getId());
//        out.writeUTF(getSender());
//        out.writeObject(getReceivers());
//        out.writeUTF(getSubject());
//        out.writeUTF(getMessage());
//        out.writeObject(getDate());
//    }
//
//    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
//        try {
//            Field field = this.getClass().getDeclaredField("id");
//            field.setAccessible(true);
//            field.set(this, new SimpleIntegerProperty());
//
//            field = this.getClass().getDeclaredField("sender");
//            field.setAccessible(true);
//            field.set(this, new SimpleStringProperty());
//
//            field = this.getClass().getDeclaredField("receivers");
//            field.setAccessible(true);
//            field.set(this, new SimpleListProperty<Email>());
//
//            field = this.getClass().getDeclaredField("subject");
//            field.setAccessible(true);
//            field.set(this, new SimpleStringProperty());
//
//            field = this.getClass().getDeclaredField("message");
//            field.setAccessible(true);
//            field.set(this, new SimpleStringProperty());
//
//            field = this.getClass().getDeclaredField("date");
//            field.setAccessible(true);
//            field.set(this, new SimpleStringProperty());
//
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            throw new IOException(e);
//        }
//
//        setId(in.readInt());
//        setSender(in.readUTF());
//        setReceivers((ObservableList<String>) in.readObject());
//        setSubject(in.readUTF());
//        setMessage(in.readUTF());
//        setDate(in.readUTF());
//    }
//
//    @Override
//    public String toString() {
//        return "Email{" +
//                "id='" + getId()
//                ", sender='" + getSender()
//                ", receivers=" + getReceivers() +
//                ", subject='" + getSubject()
//                ", message='" + getMessage()
//                ", date='" + getDate()
//                '}';
//    }
//}
