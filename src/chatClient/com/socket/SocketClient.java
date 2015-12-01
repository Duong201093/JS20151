package chatClient.com.socket;

import java.io.*;
import java.net.*;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import chatClient.com.gui.clientFrame;

public class SocketClient implements Runnable{
    
    public int port;
    public String serverAddr;
    public Socket socket;
    public clientFrame gui;
    public ObjectInputStream In;
    public ObjectOutputStream Out;
    public History hist;
    
    public SocketClient(clientFrame frame) throws IOException{
        gui = frame; this.serverAddr = gui.serverAddr; this.port = gui.port;
        socket = new Socket(InetAddress.getByName(serverAddr), port);
            
        Out = new ObjectOutputStream(socket.getOutputStream());
        Out.flush();
        In = new ObjectInputStream(socket.getInputStream());
        
        hist = gui.hist;
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
	@Override
    public void run() {
        boolean keepRunning = true;
        while(keepRunning){
            try {
                Message msg = (Message) In.readObject();
                System.out.println("Incoming : "+msg.toString());
                
                if(msg.type.equals("message")){
                    if(msg.recipient.equals(gui.username)){
                        gui.jTextArea1.append("["+msg.sender +" > Me] : " + msg.content + "\n");
                    }
                    else{
                        gui.jTextArea1.append("["+ msg.sender +" > "+ msg.recipient +"] : " + msg.content + "\n");
                    }
                                            
                    if(!msg.content.equals(".bye") && !msg.sender.equals(gui.username)){
                        String msgTime = (new Date()).toString();
                        
                        try{
                            hist.addMessage(msg, msgTime);
                            DefaultTableModel table = (DefaultTableModel) gui.historyFrame.jTable1.getModel();
                            table.addRow(new Object[]{msg.sender, msg.content, "Me", msgTime});
                        }
                        catch(Exception ex){}  
                    }
                }
                else if(msg.type.equals("login")){
                    if(msg.content.equals("TRUE")){
                        gui.jButton2.setEnabled(false); gui.jButton3.setEnabled(false);                        
                        gui.jButton4.setEnabled(true); gui.jButton5.setEnabled(true);
                        gui.jTextArea1.append("[SERVER > Me] : Login Successful\n");
                        gui.jTextField3.setEnabled(false); gui.jPasswordField1.setEnabled(false);
                    }
                    else{
                        gui.jTextArea1.append("[SERVER > Me] : Login Failed\n");
                    }
                }
                else if(msg.type.equals("test")){
                    gui.jButton1.setEnabled(false);
                    gui.jButton2.setEnabled(true); gui.jButton3.setEnabled(true);
                    gui.jTextField3.setEnabled(true); gui.jPasswordField1.setEnabled(true);
                    gui.jTextField1.setEditable(false); gui.jTextField2.setEditable(false);
                    gui.jButton7.setEnabled(true);
                }
                else if(msg.type.equals("newuser")){
                    if(!msg.content.equals(gui.username)){
                        boolean exists = false;
                        for(int i = 0; i < gui.model.getSize(); i++){
                            if(gui.model.getElementAt(i).equals(msg.content)){
                                exists = true; break;
                            }
                        }
                        if(!exists){ gui.model.addElement(msg.content); }
                    }
                }
                else if(msg.type.equals("signup")){
                    if(msg.content.equals("TRUE")){
                        gui.jButton2.setEnabled(false); gui.jButton3.setEnabled(false);
                        gui.jButton4.setEnabled(true); gui.jButton5.setEnabled(true);
                        gui.jTextArea1.append("[SERVER > Me] : Singup Successful\n");
                    }
                    else{
                        gui.jTextArea1.append("[SERVER > Me] : Signup Failed\n");
                    }
                }
                else if(msg.type.equals("signout")){
                    if(msg.content.equals(gui.username)){
                        gui.jTextArea1.append("["+ msg.sender +" > Me] : Bye\n");
                        gui.jButton1.setEnabled(true); gui.jButton4.setEnabled(false); 
                        gui.jTextField1.setEditable(true); gui.jTextField2.setEditable(true);
                        
                        for(int i = 1; i < gui.model.size(); i++){
                            gui.model.removeElementAt(i);
                        }
                        
                        gui.clientThread.stop();
                    }
                    else{
                        gui.model.removeElement(msg.content);
                        gui.jTextArea1.append("["+ msg.sender +" > All] : "+ msg.content +" has signed out\n");
                    }
                }
                else if(msg.type.equals("upload_req")){
                    
                    if(JOptionPane.showConfirmDialog(gui, ("Accept '"+msg.content+"' from "+msg.sender+" ?")) == 0){
                        
                        JFileChooser jf = new JFileChooser();
                        jf.setSelectedFile(new File(msg.content));
                        int returnVal = jf.showSaveDialog(gui);
                       
                        String saveTo = jf.getSelectedFile().getPath();
                        if(saveTo != null && returnVal == JFileChooser.APPROVE_OPTION){
                            Download dwn = new Download(saveTo, gui);
                            Thread t = new Thread(dwn);
                            t.start();
                            send(new Message("upload_res", gui.username, (""+dwn.port), msg.sender));
                        }
                        else{
                            send(new Message("upload_res", gui.username, "NO", msg.sender));
                        }
                    }
                    else{
                        send(new Message("upload_res", gui.username, "NO", msg.sender));
                    }
                }
                else if(msg.type.equals("upload_res")){
                    if(!msg.content.equals("NO")){
                        int port  = Integer.parseInt(msg.content);
                        String addr = msg.sender;
                        
                        gui.jButton5.setEnabled(false); gui.jButton6.setEnabled(false);
                        Upload upl = new Upload(addr, port, gui.file, gui);
                        Thread t = new Thread(upl);
                        t.start();
                    }
                    else{
                        gui.jTextArea1.append(" [SERVER > Me] : "+msg.sender+" rejected file request\n ");
                    }
                }
                else{
                    gui.jTextArea1.append("[SERVER > Me] : Unknown message type\n");
                }
            }
            catch(Exception ex) {
                keepRunning = false;
                gui.jTextArea1.append("[Application > Me] : Connection Failure\n");
                gui.jButton1.setEnabled(true); gui.jTextField1.setEditable(true); gui.jTextField2.setEditable(true);
                gui.jButton4.setEnabled(false); gui.jButton5.setEnabled(false); gui.jButton5.setEnabled(false);
                
                for(int i = 1; i < gui.model.size(); i++){
                    gui.model.removeElementAt(i);
                }
                
                gui.clientThread.stop();
                
                System.out.println("Exception SocketClient run()");
                ex.printStackTrace();
            }
        }
    }
    
    public void send(Message msg){
        try {
            Out.writeObject(msg);
            Out.flush();
            System.out.println("Outgoing : "+msg.toString());
            
            if(msg.type.equals("message") && !msg.content.equals(".bye")){
                String msgTime = (new Date()).toString();
                try{
                    hist.addMessage(msg, msgTime);               
                    DefaultTableModel table = (DefaultTableModel) gui.historyFrame.jTable1.getModel();
                    table.addRow(new Object[]{"Me", msg.content, msg.recipient, msgTime});
                }
                catch(Exception ex){}
            }
        } 
        catch (IOException ex) {
            System.out.println(" Exception SocketClient send() ");
        }
    }
    
    public void closeThread(Thread t){
        t = null;
    }
}
