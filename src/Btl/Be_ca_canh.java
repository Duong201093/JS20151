package Btl;


import java.util.*;

class Ca{
	
	String tenloai;
	int doPHtoithieu;
	int doPhtoida;
	String nongdomuoi; //don vi %
	String nhietdo;     // 1 la han doi, 0 la nhiet doi
	String dactinh; // co hung du hay khong?
}
class Be{

	String loaibe; /* Loai 1: Be chung
	                  Loai 2: be loai 
                      Loai 3: be don */
	int soluongca;
	int soluongmax = 20;
	int PHbe;
	String nongdomoibe;
	String nhieudobe;
}
public class Be_ca_canh {


	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner input = new Scanner(System.in);
        System.out.println("Nhap so luong ca: ");
        int n = input.nextInt();
		
        ArrayList<Ca> danhSach = new ArrayList();

       for (int i = 1; i < n + 1; i++) {
            input.nextLine(); 
            Ca ca = new Ca();
            System.out.println("Thong tin ca thu : " +i) ;
            System.out.println("Ten loai ca: ") ;
            ca.tenloai = input.nextLine();
            System.out.println("Do PH toi thieu: ");
            ca.doPHtoithieu = input.nextInt();
            System.out.println("Do PH toi da:");
            ca.doPhtoida = input.nextInt();
            System.out.println("Nong do muoi: ");
            ca.nongdomuoi = input.nextLine();
            input.nextLine();
            System.out.println("Nhiet do: ");
            ca.nhietdo = input.nextLine();
            System.out.println("Dac tinh: ");
            ca.dactinh = input.nextLine();
            danhSach.add(ca);
        }
       for (int i = 1; i < 4; i++) {
           input.nextLine(); 
           Be be = new Be(); 
           System.out.println("Thong tin be loai: " + i) ;
           System.out.println("So luong ca trong be: "); 
           be.soluongca = input.nextInt();
           System.out.println("Nong do PH be: ") ;
           be.PHbe = input.nextInt();
           System.out.println("Nong do muoi be:") ;
           be.nongdomoibe = input.nextLine();
           System.out.println(" Nhiet do be: ")  ;
           be.nhieudobe = input.nextLine();
          } 
       
	}
}
