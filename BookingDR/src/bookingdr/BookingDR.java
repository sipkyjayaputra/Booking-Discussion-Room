/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookingdr;
import model.modelBooking;
import model.modelMahasiswa;
import model.modelRuangan;

/**
 *
 * @author vitky
 */
public class BookingDR {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        BookingDiscussionRoom view = new BookingDiscussionRoom();
        /*modelMahasiswa modelMhs = new modelMahasiswa();
        modelRuangan modelR = new modelRuangan();
        modelBooking modelB =  new modelBooking();
        modelMhs.refreshTable(view.tableMahasiswa, "mahasiswa");
        modelR.refreshTable(view.tableRuangan, "ruangan");
        modelB.refreshTable(view.tableBooking);
        modelB.setRuanganBookingCB(view.RuanganBooking);
        view.startBooking.setText("00:00");*/
        view.setVisible(true); 
        view.BookingDiscussionRooms();
    }
    
}
