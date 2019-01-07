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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
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
public class modelBooking {
    BookingDiscussionRoom view = new BookingDiscussionRoom();    
    DefaultTableModel tableBookingModel = new DefaultTableModel();
    Connection conn;
    Statement st;
    ResultSet rs,rs2;
    
    public static String kodeBooking, NIMBooking, ruanganBooking, startBooking, finishBooking;
    
    public static void modelBooking(JMenu menu, JPanel halamanMahasiswa, JPanel halamanRuangan, JPanel halamanBooking){
        menu.setBackground(Color.gray);
        halamanMahasiswa.setVisible(false); halamanMahasiswa.setEnabled(false);
        halamanRuangan.setVisible(false); halamanRuangan.setEnabled(false);
        halamanBooking.setVisible(true); halamanBooking.setEnabled(true);
    }
    
    public boolean verifikasi(String start, String finish, String ruangan){        
        boolean verified =false;
        if(!start.equals("") && !finish.equals("") && !ruangan.equals("")){
            ruangan = ruangan.substring(16);
            start = start+":00";
            finish = finish+":00";
            int startTime = Integer.valueOf((start.substring(0,2)+start.substring(3,5)+start.substring(6)));
            int finishTime = Integer.valueOf((finish.substring(0,2)+finish.substring(3,5)+finish.substring(6)));
            getKoneksi();
            if(startTime>100000 && finishTime<200000){
                try {
                    String sql = "SELECT * FROM booking WHERE kode_ruangan = '"+ruangan+"' AND Start BETWEEN '"+start+"' AND '"+finish+"' AND finish BETWEEN '"+start+"' AND '"+finish+"'";
                    rs = st.executeQuery(sql);
                    if(!rs.next()){
                        verified = true;
                    }else JOptionPane.showMessageDialog(view,"Ruangan tidak tersedia pada booking diatas");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }        
            }else JOptionPane.showMessageDialog(view,"Waktu diluar jam operasional 10:00 s/d 20:00");
        }else JOptionPane.showMessageDialog(view,"Form tidak boleh kosong");
        return verified;
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
    
    public void setRuanganBookingCB(JComboBox RuanganBooking){
        getKoneksi();
        try {
            rs = st.executeQuery("SELECT keterangan FROM ruangan");
            while(rs.next()){
                RuanganBooking.addItem(rs.getString("keterangan"));                
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void refreshTable(JTable table){
        getKoneksi();
        table.setModel(tableBookingModel);
        tableBookingModel.addColumn("kode");
        tableBookingModel.addColumn("Ruangan");
        tableBookingModel.addColumn("NIM");
        tableBookingModel.addColumn("Start");
        tableBookingModel.addColumn("Finish");
        try {
            //rs = st.executeQuery("SELECT *,b.kode AS KODEPESAN FROM booking b INNER JOIN ruangan r ON b.kode_ruangan = r.kode");
            rs = st.executeQuery("SELECT * FROM booking");
            while(rs.next()){
                Object[] obj = new Object[5];
                obj[0] = rs.getString("kode");
                obj[1] = rs.getString("kode_ruangan");
                obj[2] = rs.getString("NIM");
                obj[3] = rs.getString("Start");
                obj[4] = rs.getString("finish");                
                tableBookingModel.addRow(obj); 
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void setNamaBooking(String NIM, JTextField NamaBooking){
        if(!NIM.equals("")){
            getKoneksi();
            try {           
                rs = st.executeQuery("SELECT Nama FROM mahasiswa WHERE NIM = '"+NIM+"'");
                while(rs.next()) NamaBooking.setText(rs.getString("Nama"));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void setFinishBooking(String start, JTextField finishBooking){
        if(!start.equals("")){
            int StartTimeHour = Integer.valueOf(start.substring(0,2));
            String StartTimeMinute = start.substring(3,5);
            int FinishTimeHour = StartTimeHour + 1;
            String FinishTimeMinute = StartTimeMinute;
            String finish = String.valueOf(FinishTimeHour+":"+FinishTimeMinute);
            finishBooking.setText(finish);
        }        
    }
    
    public void setBookingnForm(int row, JTextField NIM, JTextField Nama, JComboBox Ruangan, JTextField start, JTextField finish, JTable table) {
        kodeBooking = (String) table.getValueAt(row, 0);
        System.out.println(kodeBooking);
        NIMBooking = (String) table.getValueAt(row, 2);
        NIM.setText(NIMBooking);
        try {
            getKoneksi();
            rs = st.executeQuery("SELECT * FROM mahasiswa WHERE NIM ='"+NIMBooking+"'");
            if(rs.next()) Nama.setText(rs.getString("Nama"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        ruanganBooking = (String) table.getValueAt(row, 1);
        Ruangan.setSelectedItem("Discussion Room "+ruanganBooking);
        startBooking = (String) table.getValueAt(row, 3);
        finishBooking = (String) table.getValueAt(row, 4);
        start.setText(startBooking.substring(0,5));
        finish.setText(finishBooking.substring(0,5));
    }
    
    public String getKodeBooking(){
        return kodeBooking;
    }    

    public void clearBookingForm(JTextField NIM, JTextField Nama, JComboBox Ruangan, JTextField Start, JTextField Finish){
        NIM.setText("");
        Nama.setText("");
        Ruangan.setSelectedItem("Discussion Room 01");
        Start.setText("00:00");
        Finish.setText("");
    }
    
    public void tambahBooking(String ruangan, String NIM, String start, String finish, boolean verified){
        start = start+":00";
        finish = finish+":00";
        ruangan = ruangan.substring(16);
        getKoneksi();
        if(verified){
            try {
                conn.createStatement();
                st.executeUpdate("INSERT INTO booking(kode_ruangan, NIM, Start, finish) VALUES('"+ruangan+"','"+NIM+"','"+start+"','"+finish+"')");
                JOptionPane.showMessageDialog(view,"Booking Berhasil");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view,"Booking tidak berhasil \n"+ex.getMessage());
            }
        }
    }
    
    public void updateBooking(String ruangan, String NIM, String start, String finish, boolean verified){
        start = start+":00";
        finish = finish+":00";
        ruangan = ruangan.substring(16);
        getKoneksi();
        if(verified){
                try {
                    conn.createStatement();
                    if(!ruangan.equals("Discussion Room "+ruangan)){
                        if(!NIM.equals(NIMBooking)){
                            if(!start.equals(startBooking) && !finish.equals(finishBooking)){
                                //update semua
                                st.executeUpdate("UPDATE booking SET kode_ruangan='"+ruangan+"', NIM='"+NIM+"', Start='"+start+"', finish='"+finish+"' WHERE kode = "+getKodeBooking());
                                JOptionPane.showMessageDialog(view,"update Booking Berhasil");                                
                            }else{
                                //update ruangan dan NIM
                                st.executeUpdate("UPDATE booking SET kode_ruangan='"+ruangan+"', NIM='"+NIM+"' WHERE kode = "+getKodeBooking());
                                JOptionPane.showMessageDialog(view,"update Booking Berhasil");   
                            }
                        }else{
                            if(!start.equals(startBooking) && !finish.equals(finishBooking)){
                                //update ruangan dan waktu
                                st.executeUpdate("UPDATE booking SET kode_ruangan='"+ruangan+"', Start='"+start+"', finish='"+finish+"' WHERE kode = "+getKodeBooking());
                                JOptionPane.showMessageDialog(view,"update Booking Berhasil");                                
                            }else{
                                //update ruangan
                                st.executeUpdate("UPDATE booking SET kode_ruangan='"+ruangan+"' WHERE kode = "+getKodeBooking());
                                JOptionPane.showMessageDialog(view,"update Booking Berhasil");      
                            }
                        }
                    }else{
                        if(!NIM.equals(NIMBooking)){
                            if(!start.equals(startBooking) && !finish.equals(finishBooking)){
                                //update NIM dan waktu
                                st.executeUpdate("UPDATE booking SET  NIM='"+NIM+"', Start='"+start+"', finish='"+finish+"' WHERE kode = "+getKodeBooking());
                                JOptionPane.showMessageDialog(view,"update Booking Berhasil");                                
                            }else{
                                //update NIM
                                st.executeUpdate("UPDATE booking SET  NIM='"+NIM+"' WHERE kode = "+getKodeBooking());
                                JOptionPane.showMessageDialog(view,"update Booking NIM");
                            }
                        }else{
                            if(!start.equals(startBooking) && !finish.equals(finishBooking)){
                                //update waktu
                                st.executeUpdate("UPDATE booking SET Start='"+start+"', finish='"+finish+"' WHERE kode = "+getKodeBooking());
                                JOptionPane.showMessageDialog(view,"update Booking Berhasil");                                
                            }else{
                                JOptionPane.showMessageDialog(view,"tidak ada perubahan data");      
                            }
                        }
                    }                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(view,"update Booking tidak berhasil \n"+ex.getMessage());
                }
        }
        kodeBooking=null;
        NIMBooking=null;
        ruanganBooking=null;
        startBooking=null;
        finishBooking=null;
    }
    
    public void deleteBooking(){
        getKoneksi();
        if(kodeBooking!=null){
            try {
                st.executeUpdate("DELETE FROM booking WHERE kode = "+getKodeBooking()+"");
                JOptionPane.showMessageDialog(view,"Booking Berhasil dihapus");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view,"Booking tidak Berhasil dihapus \n"+ex.getMessage());
            }      
        }else JOptionPane.showMessageDialog(view,"Booking yang ingin dihapus belum dipilih");
        kodeBooking=null;
        NIMBooking=null;
        ruanganBooking=null;
        startBooking=null;
        finishBooking=null;
    }


}
