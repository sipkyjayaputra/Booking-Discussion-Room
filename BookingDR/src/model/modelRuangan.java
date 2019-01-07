/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import bookingdr.BookingDiscussionRoom;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author vitky
 */
public class modelRuangan {
    BookingDiscussionRoom view = new BookingDiscussionRoom();    
    DefaultTableModel tableRuanganModel = new DefaultTableModel();
    Connection conn;
    Statement st;
    ResultSet rs;

    public static void modelRuangan(JMenu menu, JPanel halamanMahasiswa, JPanel halamanRuangan, JPanel halamanBooking){        
        menu.setBackground(Color.gray);
        halamanMahasiswa.setVisible(false); halamanMahasiswa.setEnabled(false);
        halamanRuangan.setVisible(true); halamanRuangan.setEnabled(true);
        halamanBooking.setVisible(false); halamanBooking.setEnabled(false);
    }
    
    public void getKoneksi(){
        try {
            String url ="jdbc:mysql://localhost:3307/bookingdiscussionroom";
            String user="root";
            String pass="";
            Class.forName("com.mysql.jdbc.Driver");
            conn =DriverManager.getConnection(url,user,pass);
            st = conn.createStatement();
        } catch (Exception e) {
            System.err.println("koneksi gagal " +e.getMessage());
        }
    }
    
    public void refreshTable(JTable table, String Nama){
        getKoneksi();
        table.setModel(tableRuanganModel);
        tableRuanganModel.addColumn("Kode");
        tableRuanganModel.addColumn("Keterangan");
        try {
            rs = st.executeQuery("SELECT * FROM "+Nama);
            while(rs.next()){
                Object[] obj = new Object[table.getColumnCount()];
                for(int i=0;i<table.getColumnCount();i++){
                    obj[i] = rs.getString(i+1);
                }
                tableRuanganModel.addRow(obj); 
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }   
    
    public void setRuanganForm(int row, JTextField Kode, JTextField Keterangan, JTable table){
        Kode.setText((String) table.getValueAt(row, 0));
        Keterangan.setText((String) table.getValueAt(row, 1));
    }

    public void clearRuanganForm(JTextField Kode, JTextField Keterangan){
        Kode.setText("");
        Keterangan.setText("");
    }
    
    public void tambahRuangan(String Kode, String Keterangan){
        getKoneksi();
        if(!Kode.equals("") && !Keterangan.equals("")){
            try {
                conn.createStatement();
                st.executeUpdate("INSERT INTO ruangan VALUES('"+Kode+"','"+Keterangan+"')");
                JOptionPane.showMessageDialog(view,"Ruangan Berhasil ditambahkan");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view,"Ruangan tidak berhasil ditambahkan \n"+ex.getMessage());
            }
        }else JOptionPane.showMessageDialog(view,"Kode dan Keterangan tidak boleh kosong");
    }
    
    public void updateRuangan(String Kode, String Keterangan){
        getKoneksi();
        if(!Keterangan.equals("") && !Kode.equals("")){ 
            try {                
                st.executeUpdate("UPDATE ruangan SET keterangan = '"+Keterangan+"' WHERE kode = '"+Kode+"'");
                JOptionPane.showMessageDialog(view,"Ruangan Berhasil diupdate");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view,"Ruangan tidak Berhasil diupdate \n"+ex.getMessage());
            }
        }else JOptionPane.showMessageDialog(view,"Kode dan Keterangan tidak boleh kosong");
    }
    
    public void deleteRuangan(String Kode){
        getKoneksi();
        if(!Kode.equals("")){
            try {
                st.executeUpdate("DELETE FROM ruangan WHERE kode = '"+Kode+"'");
                JOptionPane.showMessageDialog(view,"Ruangan Berhasil dihapus");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view,"Ruangan tidak Berhasil dihapus"+ex.getMessage());
            }      
        }JOptionPane.showMessageDialog(view,"Kode tidak boleh kosong");
    }

    
}
