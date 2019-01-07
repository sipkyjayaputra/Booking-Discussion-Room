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
public class modelMahasiswa {
    BookingDiscussionRoom view = new BookingDiscussionRoom();    
    DefaultTableModel tableMahasiswaModel = new DefaultTableModel();
    Connection conn;
    Statement st;
    ResultSet rs;
    
    public static void modelMahasiswa(JMenu menu, JPanel halamanMahasiswa, JPanel halamanRuangan, JPanel halamanBooking){
        menu.setBackground(Color.gray);
        halamanMahasiswa.setVisible(true); halamanMahasiswa.setEnabled(true);
        halamanRuangan.setVisible(false); halamanRuangan.setEnabled(false);
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
        table.setModel(tableMahasiswaModel);
        tableMahasiswaModel.addColumn("NIM");
        tableMahasiswaModel.addColumn("Nama");
        tableMahasiswaModel.addColumn("Jurusan");
        try {
            rs = st.executeQuery("SELECT * FROM "+Nama);
            while(rs.next()){
                Object[] obj = new Object[table.getColumnCount()];
                for(int i=0;i<table.getColumnCount();i++){
                    obj[i] = rs.getString(i+1);
                }
                tableMahasiswaModel.addRow(obj); 
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }   
    
    public void setMahasiswaForm(int row, JTextField NIM, JTextField Nama, JTextField Jurusan, JTable table){
        NIM.setText((String) table.getValueAt(row, 0));
        Nama.setText((String) table.getValueAt(row, 1));
        Jurusan.setText((String) table.getValueAt(row, 2));
    }

    public void clearMahasiswaForm(JTextField NIM, JTextField Nama, JTextField Jurusan){
        NIM.setText("");
        Nama.setText("");
        Jurusan.setText("");
    }
    
    public void tambahMahasiswa(String NIM, String Nama, String Jurusan){
        getKoneksi();
        if(!NIM.equals("") && !Nama.equals("") && !Jurusan.equals("")){
            try {
                conn.createStatement();
                st.executeUpdate("INSERT INTO mahasiswa VALUES('"+NIM+"','"+Nama+"','"+Jurusan+"')");
                JOptionPane.showMessageDialog(view,"Mahasiswa Berhasil ditambahkan");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view,"Mahasiswa tidak berhasil ditambahkan \n"+ex.getMessage());
            }
        }else JOptionPane.showMessageDialog(view,"NIM, Nama dan Jurusan tidak boleh kosong");
    }
    
    public void updateMahasiswa(String NIM, String Nama, String Jurusan){
        getKoneksi();
        if(!NIM.equals("") && !Nama.equals("") && !Jurusan.equals("")){
            try {
                if(!Nama.equals("")){ 
                    if(!Jurusan.equals("")){
                       st.executeUpdate("UPDATE MAHASISWA SET Nama = '"+Nama+"',Jurusan = '"+Jurusan+"' WHERE NIM = '"+NIM+"'");
                    }else{
                        st.executeUpdate("UPDATE MAHASISWA SET Nama = '"+Nama+"' WHERE NIM = '"+NIM+"'");                    
                    }
                }else{
                    if(!Jurusan.equals("")){
                        st.executeUpdate("UPDATE MAHASISWA SET Jurusan = '"+Jurusan+"' WHERE NIM = '"+NIM+"'");
                    }
                }
                JOptionPane.showMessageDialog(view,"Mahasiswa Berhasil diupdate");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view,"Mahasiswa tidak Berhasil diupdate \n"+ex.getMessage());
            }
        }JOptionPane.showMessageDialog(view,"NIM, Nama, dan Jurusan tidak boleh kosong");
    }
    
    public void deleteMahasiswa(String NIM){
        getKoneksi();
        if(!NIM.equals("")){
            try {
                st.executeUpdate("DELETE FROM MAHASISWA WHERE NIM = '"+NIM+"'");
                JOptionPane.showMessageDialog(view,"Mahasiswa Berhasil dihapus");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view,"Mahasiswa tidak Berhasil dihapus"+ex.getMessage());
            }
        }else JOptionPane.showMessageDialog(view,"Mahasiswa yang ingin dihapus belum dipilih"); 
    }

}
