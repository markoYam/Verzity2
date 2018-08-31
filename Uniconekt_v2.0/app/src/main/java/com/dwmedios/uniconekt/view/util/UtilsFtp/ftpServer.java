package com.dwmedios.uniconekt.view.util.UtilsFtp;

import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ftpServer {
    private FTPClient mFtpClient = null;

    public ftpServer() {
        mFtpClient = new FTPClient();
        mFtpClient.setPassiveNatWorkaround(false);
        this.mFtpClient.setConnectTimeout(10 * 1000);
    }

    public void setFtpClient(FTPClient mFtpClient) {
        this.mFtpClient = mFtpClient;
    }

    public void useCompressedTransfer() {
        try {
            mFtpClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.COMPRESSED_TRANSFER_MODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
    }

    public String[] listName() throws Exception {
        try {
            return mFtpClient.listNames();
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean setWorkingDirectory(String dir) throws Exception {
        try {
            return mFtpClient.changeWorkingDirectory(dir);
        } catch (Exception e) {
            throw e;
        }
    }

    public FTPClient getFtpClient() {
        return mFtpClient;
    }

    public void setTimeout(int seconds) throws Exception {
        try {
            mFtpClient.setConnectTimeout(seconds * 1000);
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean makeDir(String dir) throws Exception {
        try {
            return mFtpClient.makeDirectory(dir);
        } catch (Exception e) {
            throw e;
        }
    }

    public void disconnect() {
        try {
            mFtpClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect(String ip, String userName, String pass) throws Exception {
        boolean status = false;
        try {
            try {
                mFtpClient.connect(ip);
            } catch (Exception e) {
                e.printStackTrace();
            }
            status = mFtpClient.login(userName, pass);
            Log.e("isEasyFTPConnected", String.valueOf(status));
        } catch (SocketException e) {
            throw e;
        } catch (UnknownHostException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    } //Passing InputStream and fileName

    public Response uploadFile(String uri) {
        Response mResponse = new Response();
        try {
            File file = new File(uri);
            mFtpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            FileInputStream srcFileStream = new FileInputStream(file);
            String format = file.getName();

            if (format.endsWith(".jpg")) format = ".jpg";
            else format = ".jpg";
            if (format.endsWith(".png")) format = ".png";
            else format = ".jpg";
            if (format.endsWith(".jpeg")) format = ".jpeg";
            else format = ".jpg";

            String nameFile = ramdomString() + format;
            boolean status = mFtpClient.storeFile(nameFile, srcFileStream);
            Log.e("Status", String.valueOf(status));
            srcFileStream.close();

            mResponse.status = 1;
            mResponse.patch = nameFile;

        } catch (Exception e) {
            mResponse.status = 0;
        }
        return mResponse;
    }

    public void uploadFile(InputStream srcFileStream, String name) throws Exception {
        try {
            mFtpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            boolean status = mFtpClient.storeFile(name, srcFileStream);
            Log.e("Status", String.valueOf(status));
            srcFileStream.close();
        } catch (Exception e) {
            throw e;
        }
    }

    public void downloadFile(String remoteFilePath, String dest) throws Exception {
        File downloadFile = new File(dest);
        File parentDir = downloadFile.getParentFile();
        if (!parentDir.exists())
            parentDir.mkdir();
        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
            mFtpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            boolean status = mFtpClient.retrieveFile(remoteFilePath, outputStream);
            Log.e("Status", String.valueOf(status));
        } catch (Exception e) {
            throw e;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
    }

    public static class Response {
        public int status;
        public String patch;

        public Response() {
            status = 0;
            patch = null;
        }
    }

    public String ramdomString() {
        String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }
}
