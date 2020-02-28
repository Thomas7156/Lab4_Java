/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class MyCalculusRunnable implements Runnable {
    private Socket sock;
   
    public MyCalculusRunnable(Socket s) {
        sock = s;
    }
   
    @Override
    public void run() {
       
        try {
            DataInputStream dis = new DataInputStream(sock.getInputStream());
            DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
           
            // read op1, op2 and the opreation to make
            float op1 = dis.readFloat();
            char op = dis.readChar();
            float op2 = dis.readFloat();
           
            float res = CalculatorServer.doOp(op1, op, op2);
           
            // send back result
            dos.writeFloat(res);
           
            dis.close();
            dos.close();
            sock.close();
           
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
    }
   
}

public class CalculatorServer {
       
       
    public static float doOp(float op1, char op, float op2) throws Exception
    {
        switch (op) {
       
        case '+':
            return op1 + op2;
           
        case '-':
            return op1 - op2;
           
        case'x':
            return op1 * op2;
           
        case '/':
            if (op2 != 0)
                return op1 / op2;
            else
                throw new Exception();   
       
        default:
            throw new Exception();
        }
       
    }
   
   
   
    public static void main(String[] args) throws Exception {
               
        // Example of a distant calculator
        ServerSocket ssock = new ServerSocket(9876);
       
        while (true) { // infinite loop
            Socket comm = ssock.accept();
            System.out.println("connection established");
           
            new Thread(new MyCalculusRunnable(comm)).start();   
           
        }
           
    }
   
}
