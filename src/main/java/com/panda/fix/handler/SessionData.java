package com.panda.fix.handler;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SessionData {

    private static final String SENDING_TIME_FORMAT = "yyyyMMdd-HH:mm:ss.SSS";
    private static final String DATA_FILE_PATH = "data";

    private String senderCompId;
    private String targetCompId;
    private int outSeq;
    private int inSeq;
    private boolean logout;
    private String sessionName;
    private String inSeqFileName;
    private String outSeqFileName;
    private String inDataFileName;
    private String outDataFileName;
    private BufferedWriter outDataWriter;
    private BufferedWriter inDataWriter;


    public SessionData(String senderCompId, String targetCompId) throws IOException {
        this.senderCompId = senderCompId;
        this.targetCompId = targetCompId;
        this.sessionName = senderCompId + "-" + targetCompId;
        this.inSeqFileName = DATA_FILE_PATH + "/" + sessionName + ".seq.in";
        this.outSeqFileName = DATA_FILE_PATH + "/" + sessionName + ".seq.out";
        this.inDataFileName = DATA_FILE_PATH + "/" + sessionName + ".data.in";
        this.outDataFileName = DATA_FILE_PATH + "/" + sessionName + ".data.out";


        File inSeqFile = new File(inSeqFileName);

        if(!inSeqFile.exists()){
            inSeq = 1;
            FileUtils.writeStringToFile(inSeqFile, "1", "UTF-8");

        }

        File outSeqFile = new File(outSeqFileName);
        if(!outSeqFile.exists()){
            outSeq = 1;
            FileUtils.writeStringToFile(outSeqFile, "1", "UTF-8");
        }

        BufferedReader file = new BufferedReader(new FileReader(outSeqFile));
        outSeq = Integer.parseInt(file.readLine());
        file.close();

        outDataWriter = new BufferedWriter(new FileWriter(outDataFileName, true));
        inDataWriter = new BufferedWriter(new FileWriter(inDataFileName, true));
    }


    public synchronized void increaseOutSeq() throws IOException{
        outSeq++;
        FileUtils.writeStringToFile(new File(outSeqFileName), String.valueOf(outSeq), "UTF-8");
    }

    public synchronized void updateInSeq(int seq) throws IOException{
        this.inSeq = seq;
        FileUtils.writeStringToFile(new File(inSeqFileName), String.valueOf(inSeq), "UTF-8");
    }



    public void setLogout(boolean b) {

    }

    public void writeToOutDataFile(String message) throws IOException {

        StringBuilder sb = new StringBuilder(String.valueOf(this.outSeq)).append(" ").append(message).append("\n");

        this.outDataWriter.write(sb.toString());
        this.outDataWriter.flush();
        increaseOutSeq();
    }

    public String createLogonMsg() {

        String sendingTime = formatDate(new Date(), SENDING_TIME_FORMAT);

        StringBuilder sb = new StringBuilder("35=A 49=").append(this.senderCompId).append(" 56=").append(this.targetCompId).append(" 34=").append(outSeq).append(" 52=").append(sendingTime).append(" 98=0 108=30 ");

        StringBuilder msg = new StringBuilder("8=FIX.4.2 9=").append(sb.length()).append(" ").append(sb);

        int sum = checksum(msg.toString().getBytes(), true);
        String sumStr = String.valueOf(sum);
        while(sumStr.length() < 3){
            sumStr = "0" + sumStr;
        }

        String logonMsg = msg.append("10=").append(sumStr).append(" ").toString();

        return logonMsg;
    }

    public static int checksum(byte[] data, boolean isEntireMessage){
        int sum = 0;
        int len = data.length;
        if(isEntireMessage && data[len -8 ] == '\001' && data[len - 7] == '1' && data[len -6] == '0' && data[len - 5] == '='){
            len = len -7;

        }
        for(int i =0; i < len; i++){
            sum += (data[i] &0xFF);
        }
        return sum& 0xFF;
    }

    public static String formatDate(Date d, String format){
        SimpleDateFormat customDf = new SimpleDateFormat(format);
        return customDf.format(d);
    }

    public String createLogoutMsg(){
        String sendingTime = formatDate(new Date(), SENDING_TIME_FORMAT);

        StringBuilder sb = new StringBuilder("35=5 49=").append(this.senderCompId).append(" 56=").append(this.targetCompId).append(" 34=").append(outSeq).append(" 52=").append(sendingTime).append(" 98=0 108=30 ");

        StringBuilder msg = new StringBuilder("8=FIX.4.2 9=").append(sb.length()).append(" ").append(sb);

        int sum = checksum(msg.toString().getBytes(), true);
        String sumStr = String.valueOf(sum);
        while(sumStr.length() < 3){
            sumStr = "0" + sumStr;
        }

        String logoutMsg = msg.append("10=").append(sumStr).append(" ").toString();

        return logoutMsg;
    }

    public String createHeartbeatMsg() {


        String sendingTime = formatDate(new Date(), SENDING_TIME_FORMAT);

        StringBuilder sb = new StringBuilder("35=0 49=").append(this.senderCompId).append(" 56=").append(this.targetCompId).append(" 34=").append(outSeq).append(" 52=").append(sendingTime).append(" 98=0 108=30 ");

        StringBuilder msg = new StringBuilder("8=FIX.4.2 9=").append(sb.length()).append(" ").append(sb);

        int sum = checksum(msg.toString().getBytes(), true);
        String sumStr = String.valueOf(sum);
        while(sumStr.length() < 3){
            sumStr = "0" + sumStr;
        }

        String hbMsg = msg.append("10=").append(sumStr).append(" ").toString();

        return hbMsg;
    }

    public int getSeqFromMessage(String msg) {
        int start = msg.indexOf(" 34=")+4;
        int end = msg.indexOf(' ', start);
        return Integer.parseInt(msg.substring(start, end));
    }



    public void writeToInDataFile(String message) throws IOException {

        StringBuilder sb = new StringBuilder(String.valueOf(this.inSeq)).append(" ").append(message).append("\n");
        this.inDataWriter.write(sb.toString());
        this.inDataWriter.flush();
    }

    public boolean isLogin(String inMsg) {
        return inMsg.indexOf("35=A") > -1;
    }

    public boolean isLoginAck(String msg){
        return msg.indexOf("35=A") > -1;
    }

    public boolean isLogout() {

        return logout;
    }

    public boolean isLogoutMsg(String inMsg) {
        return inMsg.indexOf("35=5") > -1;
    }

    public String getSenderCompId() {
        return senderCompId;
    }

    public void setSenderCompId(String senderCompId) {
        this.senderCompId = senderCompId;
    }

    public String getTargetCompId() {
        return targetCompId;
    }

    public void setTargetCompId(String targetCompId) {
        this.targetCompId = targetCompId;
    }

    public int getOutSeq() {
        return outSeq;
    }

    public void setOutSeq(int outSeq) {
        this.outSeq = outSeq;
    }

    public int getInSeq() {
        return inSeq;
    }

    public void setInSeq(int inSeq) {
        this.inSeq = inSeq;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getInSeqFileName() {
        return inSeqFileName;
    }

    public void setInSeqFileName(String inSeqFileName) {
        this.inSeqFileName = inSeqFileName;
    }

    public String getOutSeqFileName() {
        return outSeqFileName;
    }

    public void setOutSeqFileName(String outSeqFileName) {
        this.outSeqFileName = outSeqFileName;
    }

    public String getInDataFileName() {
        return inDataFileName;
    }

    public void setInDataFileName(String inDataFileName) {
        this.inDataFileName = inDataFileName;
    }

    public String getOutDataFileName() {
        return outDataFileName;
    }

    public void setOutDataFileName(String outDataFileName) {
        this.outDataFileName = outDataFileName;
    }

    public BufferedWriter getOutDataWriter() {
        return outDataWriter;
    }

    public void setOutDataWriter(BufferedWriter outDataWriter) {
        this.outDataWriter = outDataWriter;
    }

    public BufferedWriter getInDataWriter() {
        return inDataWriter;
    }

    public void setInDataWriter(BufferedWriter inDataWriter) {
        this.inDataWriter = inDataWriter;
    }
}
